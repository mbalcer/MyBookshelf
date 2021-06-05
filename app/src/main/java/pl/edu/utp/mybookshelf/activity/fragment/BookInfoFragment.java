package pl.edu.utp.mybookshelf.activity.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import java.util.Optional;

import pl.edu.utp.mybookshelf.R;
import pl.edu.utp.mybookshelf.model.Book;

public class BookInfoFragment extends Fragment {

    private Book book;
    private LinearLayout layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_book_info, container, false);
        if (getActivity().getIntent().getExtras() != null && getActivity().getIntent().getExtras().getSerializable("book") != null) {
            book = (Book) getActivity().getIntent().getExtras().getSerializable("book");
        }

        layout = inflate.findViewById(R.id.book_info_layout);

        addInformation("Tytuł", book.getTitle());
        addInformation("Autor", book.getAuthor());
        addInformation("Opis", book.getDescription());
        if (Optional.ofNullable(book.getCategory()).isPresent()) {
            addInformation("Kategoria", book.getCategory().getName());
        }
        addInformation("ISBN", book.getIsbn());
        addInformation("Liczba stron", book.getPages());
        addInformation("Data wydania", book.getPublishDate());

        return inflate;
    }

    private void addInformation(String name, Object value) {
        if (value != null) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(50, 50, 0, 0);

            TextView titleText = new TextView(getContext());
            titleText.setTextSize(16);
            titleText.setAllCaps(true);
            titleText.setTypeface(null, Typeface.BOLD);
            titleText.setLayoutParams(params);
            titleText.setText(name);

            TextView valueText = new TextView(getContext());
            valueText.setTextSize(18);
            params.setMargins(50, 30, 0, 30);
            valueText.setLayoutParams(params);
            valueText.setText(value.toString());

            layout.addView(titleText);
            layout.addView(valueText);
        }
    }
}