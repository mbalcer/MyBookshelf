package pl.edu.utp.mybookshelf.activity;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import pl.edu.utp.mybookshelf.R;
import pl.edu.utp.mybookshelf.adapter.BookViewPagerAdapter;
import pl.edu.utp.mybookshelf.model.Book;
import pl.edu.utp.mybookshelf.model.Review;

public class BookActivity extends AppCompatActivity {

    private Book book;
    private LinearLayout layout;
    private List<String> tabTitles = Arrays.asList("Moje dane", "Info", "Opinie");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);
        if (getIntent().getExtras() != null && getIntent().getExtras().getSerializable("book") != null) {
            book = (Book) getIntent().getExtras().getSerializable("book");
        }

        layout = findViewById(R.id.activity_book_layout);

        TextView titleText = findViewById(R.id.book_title);
        TextView authorText = findViewById(R.id.book_author);
        ImageView imageView = findViewById(R.id.book_image);

        titleText.setText(book.getTitle());
        authorText.setText(book.getAuthor());
        imageView.setImageResource(book.getImage());

//        addInformation("Tytuł", book.getTitle());
//        addInformation("Autor", book.getAuthor());
//        addInformation("Opis", book.getDescription());
//        if (Optional.ofNullable(book.getCategory()).isPresent()) {
//            addInformation("Kategoria", book.getCategory().getName());
//        }
//        addInformation("ISBN", book.getIsbn());
//        addInformation("Liczba stron", book.getPages());
//        addInformation("Data wydania", book.getPublishDate());
//        setRating();

        ViewPager2 viewPager = findViewById(R.id.viewpager);
        BookViewPagerAdapter pagerAdapter = new BookViewPagerAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager.setAdapter(pagerAdapter);

        TabLayout tabLayout = findViewById(R.id.book_details_menu);

        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(tabTitles.get(position))).attach();
    }

    private void addInformation(String name, Object value) {
        if (value != null) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(50, 50, 0, 0);

            TextView titleText = new TextView(getApplicationContext());
            titleText.setTextSize(16);
            titleText.setAllCaps(true);
            titleText.setTypeface(null, Typeface.BOLD);
            titleText.setLayoutParams(params);
            titleText.setText(name);

            TextView valueText = new TextView(getApplicationContext());
            valueText.setTextSize(18);
            params.setMargins(50, 30, 0, 30);
            valueText.setLayoutParams(params);
            valueText.setText(value.toString());

            layout.addView(titleText);
            layout.addView(valueText);
        }
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
}