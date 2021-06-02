package pl.edu.utp.mybookshelf.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import pl.edu.utp.mybookshelf.R;
import pl.edu.utp.mybookshelf.model.Book;

public class BookActivity extends AppCompatActivity {

    private Book book;
    private LinearLayout layout;

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
    }
}