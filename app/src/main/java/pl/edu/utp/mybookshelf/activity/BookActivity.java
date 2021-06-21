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
import pl.edu.utp.mybookshelf.activity.fragment.BookReviewsFragment;
import pl.edu.utp.mybookshelf.adapter.ViewPagerAdapter;
import pl.edu.utp.mybookshelf.database.DBHelper;
import pl.edu.utp.mybookshelf.model.Book;
import pl.edu.utp.mybookshelf.model.Review;

public class BookActivity extends AppCompatActivity {

    private ActionBar toolbar;

    private Book book;
    private DBHelper dbHelper;
    private List<String> titleTabs = new ArrayList<>();
    private List<Fragment> tabs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        toolbar = getSupportActionBar();
        dbHelper = new DBHelper(getApplicationContext());
        if (getIntent().getExtras() != null && getIntent().getExtras().getSerializable("book") != null) {
            book = (Book) getIntent().getExtras().getSerializable("book");
            toolbar.setTitle(book.getTitle());
        }

        initTabs();

        TextView titleText = findViewById(R.id.book_title);
        TextView authorText = findViewById(R.id.book_author);
        ImageView imageView = findViewById(R.id.book_image);
        Button bookInBookshelfBtn = findViewById(R.id.book_in_bookshelf_button);

        if (dbHelper.getUserBookByBookId(book.getId()).getBookId() != null) {
            bookInBookshelfBtn.setText(R.string.delete_book_from_bookshelf_button);
            bookInBookshelfBtn.setOnClickListener(bookInBookshelfBtnClickListener(true));
        } else {
            bookInBookshelfBtn.setOnClickListener(bookInBookshelfBtnClickListener(false));
        }
        titleText.setText(book.getTitle());
        authorText.setText(book.getAuthor());
        imageView.setImageResource(book.getImage());
        setRating();

        ViewPager2 viewPager = findViewById(R.id.viewpager);
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), getLifecycle(), tabs);
        viewPager.setAdapter(pagerAdapter);
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

        if (Optional.ofNullable(book.getReviews()).isPresent() && book.getReviews().size() > 0) {
            Double avgRating = book.getReviews()
                    .stream()
                    .mapToInt(Review::getRating)
                    .average()
                    .orElse(Double.NaN);

            ratingBar.setRating(avgRating.floatValue());
            ratingText.setText(String.format("%.2f", avgRating));
        }
    }

    private void initTabs() {
        titleTabs.add("Info");
        tabs.add(new BookInfoFragment());

        titleTabs.add("Moje dane");
        tabs.add(new BookMyDataFragment());

        titleTabs.add("Opinie");
        tabs.add(new BookReviewsFragment());
    }

    private View.OnClickListener bookInBookshelfBtnClickListener(boolean delete) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (delete) {
                    dbHelper.deleteById(book.getId());
                } else {
                    dbHelper.setBookAsToRead(book.getId());
                }
                recreate();
            }
        };
    }
}