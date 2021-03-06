package pl.edu.utp.mybookshelf.activity.fragment;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
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
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Collections;

import pl.edu.utp.mybookshelf.R;
import pl.edu.utp.mybookshelf.activity.BookActivity;
import pl.edu.utp.mybookshelf.database.FirebaseBook;
import pl.edu.utp.mybookshelf.database.LocalDatabase;
import pl.edu.utp.mybookshelf.database.dao.UserBookDao;
import pl.edu.utp.mybookshelf.model.Book;
import pl.edu.utp.mybookshelf.model.BookState;
import pl.edu.utp.mybookshelf.model.Quote;
import pl.edu.utp.mybookshelf.model.Review;
import pl.edu.utp.mybookshelf.model.User;
import pl.edu.utp.mybookshelf.model.UserBook;

public class BookMyDataFragment extends Fragment {
    private UserBookDao userBookDao;
    private Book book;
    private FirebaseAuth auth;
    private User loggedUser;

    private RatingBar reviewRatingBar;
    private TextInputEditText reviewInputText;
    private TextInputEditText quoteInputText;
    private TextInputLayout quoteInputTextLayout;
    private TextInputEditText quoteInputPage;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userBookDao = LocalDatabase.getInstance(getContext()).userBookDao();
        auth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = auth.getCurrentUser();
        loggedUser = new User(currentUser.getUid(), currentUser.getEmail(), currentUser.getDisplayName());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_book_my_data, container, false);
        if (getActivity().getIntent().getExtras() != null && getActivity().getIntent().getExtras().getSerializable("book") != null) {
            book = (Book) getActivity().getIntent().getExtras().getSerializable("book");
        }

        SwitchMaterial switchIsRead = inflate.findViewById(R.id.book_is_read);
        UserBook userBook = userBookDao.getUserBookByBookId(book.getId()).orElseThrow(Resources.NotFoundException::new);
        if (BookState.READ.equals(userBook.getState()))
            switchIsRead.setChecked(true);
        else
            switchIsRead.setChecked(false);
        switchIsRead.setOnCheckedChangeListener(checkedIsBook());

        reviewRatingBar = inflate.findViewById(R.id.your_review_rating);
        reviewInputText = inflate.findViewById(R.id.your_review_text);
        quoteInputText = inflate.findViewById(R.id.your_quote_text);
        quoteInputTextLayout = inflate.findViewById(R.id.your_quote_text_layout);
        quoteInputPage = inflate.findViewById(R.id.your_quote_page);

        Button addReviewButton = inflate.findViewById(R.id.add_review_to_book);
        addReviewButton.setOnClickListener(view -> addReview());

        Button addQuoteButton = inflate.findViewById(R.id.add_quote_to_book);
        addQuoteButton.setOnClickListener(view -> addQuote());

        return inflate;
    }

    private CompoundButton.OnCheckedChangeListener checkedIsBook() {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                UserBook userBook = userBookDao.getUserBookByBookId(book.getId()).orElseThrow(Resources.NotFoundException::new);
                if (isChecked) {
                    userBook.setState(BookState.READ);
                    Log.d(BookMyDataFragment.class.getName(), "You set book " + book.getTitle() + " as read");
                } else {
                    userBook.setState(BookState.TO_READ);
                    Log.d(BookMyDataFragment.class.getName(), "You set book " + book.getTitle() + " as to read");
                }
                userBookDao.update(userBook);
            }
        };
    }

    private void addReview() {
        if (reviewRatingBar.getRating() == 0) {
            Toast.makeText(getContext(), "Wybierz ocen??, aby doda?? recenzj??", Toast.LENGTH_SHORT).show();
        } else if (userReviewExists()) {
            Toast.makeText(getContext(), "Twoja recenzja tej ksi????ki ju?? istnieje", Toast.LENGTH_SHORT).show();
        } else {
            Review review = new Review();
            review.setRating(reviewRatingBar.getRating());
            String reviewText = reviewInputText.getText().toString();
            if (!reviewText.trim().isEmpty()) {
                review.setText(reviewText);
            }
            review.setUser(loggedUser);

            if (book.getReviews() == null) {
                book.setReviews(Collections.singletonList(review));
            } else {
                book.getReviews().add(review);
            }
            FirebaseBook.update(book);
            Log.d(BookMyDataFragment.class.getName(), "Saved new review for book: " + book.getId());
            openBookFragment(2);
        }
    }

    private boolean userReviewExists() {
        String loggedId = loggedUser.getId();
        if (book.getReviews() == null || book.getReviews().isEmpty()) {
            return false;
        } else {
            return book.getReviews().stream()
                    .anyMatch(review -> review.getUser() != null
                            && review.getUser().getId().equals(loggedId));
        }
    }

    private void addQuote() {
        if (checkQuoteData()) {
            Quote quote = new Quote();
            quote.setText(quoteInputText.getText().toString());
            String quotePage = quoteInputPage.getText().toString();
            if (!quotePage.trim().isEmpty()) {
                quote.setPage(quotePage);
            }
            quote.setUser(loggedUser);

            if (book.getQuotes() == null) {
                book.setQuotes(Collections.singletonList(quote));
            } else {
                book.getQuotes().add(quote);
            }
            FirebaseBook.update(book);
            Log.d(BookMyDataFragment.class.getName(), "Saved new quote for book: " + book.getId());
            openBookFragment(3);
        }
    }

    private boolean checkQuoteData() {
        quoteInputTextLayout.setError(null);
        boolean validData = true;

        Editable textEditable = quoteInputText.getText();
        if (textEditable == null || textEditable.toString().trim().isEmpty()) {
            quoteInputTextLayout.setError("To pole jest wymagane");
            validData = false;
        }
        return validData;
    }

    private void openBookFragment(Integer tabIndex) {
        Intent intent = new Intent(getContext(), BookActivity.class);
        intent.putExtra("book", book);
        intent.putExtra("tab", tabIndex);
        startActivity(intent);
    }

}