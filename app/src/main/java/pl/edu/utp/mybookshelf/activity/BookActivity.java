package pl.edu.utp.mybookshelf.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import pl.edu.utp.mybookshelf.R;
import pl.edu.utp.mybookshelf.activity.fragment.BookInfoFragment;
import pl.edu.utp.mybookshelf.activity.fragment.BookMyDataFragment;
import pl.edu.utp.mybookshelf.activity.fragment.BookQuotesFragment;
import pl.edu.utp.mybookshelf.activity.fragment.BookReviewsFragment;
import pl.edu.utp.mybookshelf.adapter.ViewPagerAdapter;
import pl.edu.utp.mybookshelf.database.LocalDatabase;
import pl.edu.utp.mybookshelf.database.dao.UserBookDao;
import pl.edu.utp.mybookshelf.model.Book;
import pl.edu.utp.mybookshelf.model.BookState;
import pl.edu.utp.mybookshelf.model.Review;
import pl.edu.utp.mybookshelf.model.UserBook;
import pl.edu.utp.mybookshelf.utils.ImageLoader;

public class BookActivity extends AppCompatActivity {

    private ActionBar toolbar;
    private UserBookDao userBookDao;

    private Book book;
    private List<String> titleTabs = new ArrayList<>();
    private List<Fragment> tabs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        toolbar = getSupportActionBar();
        userBookDao = LocalDatabase.getInstance(getApplicationContext()).userBookDao();
        if (getIntent().getExtras() != null && getIntent().getExtras().getSerializable("book") != null) {
            book = (Book) getIntent().getExtras().getSerializable("book");
            toolbar.setTitle(book.getTitle());
        }

        initTabs();

        TextView titleText = findViewById(R.id.book_title);
        TextView authorText = findViewById(R.id.book_author);
        ImageView imageView = findViewById(R.id.book_image);
        Button bookInBookshelfBtn = findViewById(R.id.book_in_bookshelf_button);

        if (userBookDao.getUserBookByBookId(book.getId()).isPresent()) {
            bookInBookshelfBtn.setText(R.string.delete_book_from_bookshelf_button);
            bookInBookshelfBtn.setOnClickListener(bookInBookshelfBtnClickListener(true));
            bookInBookshelfBtn.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.alizarin));
        } else {
            bookInBookshelfBtn.setOnClickListener(bookInBookshelfBtnClickListener(false));
        }
        titleText.setText(book.getTitle());
        authorText.setText(book.getAuthor());
        setRating();

        ImageLoader imageLoader = new ImageLoader(imageView, this);
        imageLoader.execute(book.getImage());

        ViewPager2 viewPager = findViewById(R.id.viewpager);
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle(), tabs);
        viewPager.setAdapter(pagerAdapter);

        if (getIntent().getExtras() != null && getIntent().getExtras().getSerializable("tab") != null) {
            int tabToOpen = (int) getIntent().getExtras().getSerializable("tab");
            if (titleTabs.size() > tabToOpen) {
                viewPager.setCurrentItem(tabToOpen);
            }
        }
        TabLayout tabLayout = findViewById(R.id.book_details_menu);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(titleTabs.get(position))).attach();
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                if (getSupportFragmentManager().getFragments().size() > position) {
                    Fragment fragment = getSupportFragmentManager().getFragments().get(position);
                    View view = fragment.getView();
                    int wMeasureSpec = View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY);
                    int hMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                    view.measure(wMeasureSpec, hMeasureSpec);

                    if (viewPager.getLayoutParams().height != view.getMeasuredHeight()) {
                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) viewPager.getLayoutParams();
                        layoutParams.height = view.getMeasuredHeight();
                        viewPager.setLayoutParams(layoutParams);
                    }
                }
            }
        });
    }

    private void setRating() {
        RatingBar ratingBar = findViewById(R.id.avg_rating_bar);
        TextView ratingText = findViewById(R.id.avg_rating_text);
        TextView ratingsNumber = findViewById(R.id.ratings_number_text);

        if (Optional.ofNullable(book.getReviews()).isPresent() && book.getReviews().size() > 0) {
            double avgRating = book.getReviews()
                    .stream()
                    .mapToDouble(Review::getRating)
                    .average()
                    .orElse(Double.NaN);

            ratingBar.setRating((float) (avgRating / 5.0));
            ratingText.setText(String.format("%.2f", avgRating));
            ratingsNumber.setText("(" + book.getReviews().size() + ")");
        }
    }

    private void initTabs() {
        titleTabs.add("Info");
        tabs.add(new BookInfoFragment());

        if (userBookDao.getUserBookByBookId(book.getId()).isPresent()) {
            titleTabs.add("Moje dane");
            tabs.add(new BookMyDataFragment());
        }

        titleTabs.add("Opinie");
        tabs.add(new BookReviewsFragment());

        titleTabs.add("Cytaty");
        tabs.add(new BookQuotesFragment());
    }

    private View.OnClickListener bookInBookshelfBtnClickListener(boolean delete) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delete) {
                    userBookDao.deleteById(book.getId());
                } else {
                    Optional<UserBook> userBook = userBookDao.getUserBookByBookId(book.getId());
                    if (userBook.isPresent()) {
                        userBook.get().setState(BookState.TO_READ);
                        userBookDao.update(userBook.get());
                    } else {
                        UserBook newUserBook = new UserBook(book.getId(), BookState.TO_READ);
                        userBookDao.insert(newUserBook);
                    }
                }
                reloadActivity();
            }
        };
    }

    private void reloadActivity() {
        finish();
        overridePendingTransition(0, 0);
        startActivity(getIntent());
        overridePendingTransition(0, 0);
    }
}