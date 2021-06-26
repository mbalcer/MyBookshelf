package pl.edu.utp.mybookshelf.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Collections;

import pl.edu.utp.mybookshelf.R;
import pl.edu.utp.mybookshelf.activity.BookActivity;
import pl.edu.utp.mybookshelf.database.DBHelper;
import pl.edu.utp.mybookshelf.database.FirebaseBook;
import pl.edu.utp.mybookshelf.model.Book;
import pl.edu.utp.mybookshelf.model.BookState;
import pl.edu.utp.mybookshelf.model.Review;
import pl.edu.utp.mybookshelf.model.User;
import pl.edu.utp.mybookshelf.model.UserBook;

public class BookMyDataFragment extends Fragment {
    private DBHelper dbHelper;
    private Book book;

    private RatingBar reviewRatingBar;
    private TextInputEditText reviewInputText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DBHelper(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_book_my_data, container, false);
        if (getActivity().getIntent().getExtras() != null && getActivity().getIntent().getExtras().getSerializable("book") != null) {
            book = (Book) getActivity().getIntent().getExtras().getSerializable("book");
        }

        SwitchMaterial switchIsRead = inflate.findViewById(R.id.book_is_read);
        UserBook userBook = dbHelper.getUserBookByBookId(book.getId());
        if (BookState.READ.equals(userBook.getState()))
            switchIsRead.setChecked(true);
        else
            switchIsRead.setChecked(false);
        switchIsRead.setOnCheckedChangeListener(checkedIsBook());

        reviewRatingBar = inflate.findViewById(R.id.your_review_rating);
        reviewInputText = inflate.findViewById(R.id.your_review_text);

        Button addReviewButton = inflate.findViewById(R.id.add_review_to_book);
        addReviewButton.setOnClickListener(view -> addReview());

        return inflate;
    }

    private CompoundButton.OnCheckedChangeListener checkedIsBook() {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    dbHelper.setBookAsRead(book.getId());
                    Log.d(BookMyDataFragment.class.getName(), "You set book " + book.getTitle() + " as read");
                } else {
                    dbHelper.setBookAsToRead(book.getId());
                    Log.d(BookMyDataFragment.class.getName(), "You set book " + book.getTitle() + " as to read");
                }
            }
        };
    }

    private void addReview() {
        if (reviewRatingBar.getRating() == 0) {
            Toast.makeText(getContext(), "Wybierz ocenę, aby dodać recenzję", Toast.LENGTH_SHORT).show();
        } else if (userReviewExists()) {
            Toast.makeText(getContext(), "Twoja recenzja tej książki już istnieje", Toast.LENGTH_SHORT).show();
        } else {
            Review review = new Review();
            review.setRating(reviewRatingBar.getRating());
            String reviewText = reviewInputText.getText().toString();
            if (!reviewText.trim().isEmpty()) {
                review.setText(reviewText);
            }
            // TODO: setting user in review
            User reviewUser = new User(1L, "szymonbetlewski@wp.pl", "123", "Szymon Betlewski");
            review.setUser(reviewUser);

            if (book.getReviews() == null) {
                book.setReviews(Collections.singletonList(review));
            } else {
                book.getReviews().add(review);
            }
            FirebaseBook.update(book);
            Log.d(BookMyDataFragment.class.getName(), "Saved new review for book: " + book.getId());
            openBookReviewsFragment();
        }
    }

    private boolean userReviewExists() {
        // TODO: getting logged user
        String loggedEmail = "szymonbetlewski@wp.pl";
        if (book.getReviews() == null || book.getReviews().isEmpty()) {
            return false;
        } else {
            return book.getReviews().stream()
                    .anyMatch(review -> review.getUser() != null
                            && review.getUser().getEmail().equals(loggedEmail));
        }
    }

    private void openBookReviewsFragment() {
        Intent intent = new Intent(getContext(), BookActivity.class);
        intent.putExtra("book", book);
        intent.putExtra("tab", 2);
        startActivity(intent);
    }

}