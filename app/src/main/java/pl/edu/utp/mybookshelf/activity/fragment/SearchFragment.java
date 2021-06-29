package pl.edu.utp.mybookshelf.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import pl.edu.utp.mybookshelf.R;
import pl.edu.utp.mybookshelf.activity.AddBookActivity;
import pl.edu.utp.mybookshelf.activity.BookActivity;
import pl.edu.utp.mybookshelf.activity.ScannerActivity;
import pl.edu.utp.mybookshelf.adapter.SearchResultListAdapter;
import pl.edu.utp.mybookshelf.database.FirebaseBook;
import pl.edu.utp.mybookshelf.database.FirebaseCallback;
import pl.edu.utp.mybookshelf.model.Book;

public class SearchFragment extends Fragment {

    private List<Book> allBooks = new ArrayList<>();
    private List<Book> booksFound = new ArrayList<>();
    private ListView searchBookResultList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseBook.getAllBooks(new FirebaseCallback<Book>() {
            @Override
            public void getAll(List<Book> list) {
                allBooks = list.stream()
                        .sorted(Comparator.comparing(Book::getTitle))
                        .collect(Collectors.toList());
                booksFound = allBooks;
                setSearchResultAdapter();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_search, container, false);
        searchBookResultList = inflate.findViewById(R.id.search_book_result_list);

        if (getActivity().getIntent().getExtras() != null && getActivity().getIntent().getExtras().getSerializable("resultScanner") != null) {
            boolean resultScanner = getActivity().getIntent().getExtras().getBoolean("resultScanner");
            if (resultScanner) {
                Book scannedBook = (Book) getActivity().getIntent().getExtras().getSerializable("scannedBook");
                booksFound.add(scannedBook);
                setSearchResultAdapter();
            } else {
                setEmptySearchResultAdapter();
            }
            getActivity().getIntent().removeExtra("resultScanner");
            getActivity().getIntent().removeExtra("scannedBook");
        }

        FloatingActionButton scanBookButton = inflate.findViewById(R.id.scan_book_button);
        scanBookButton.setOnClickListener(view -> openActivity(ScannerActivity.class));

        TextInputEditText searchBookText = inflate.findViewById(R.id.search_book_text);
        searchBookText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().length() >= 3) {
                    booksFound = allBooks.stream()
                            .filter(book -> {
                                int indexTitle = book.getTitle().toLowerCase().indexOf(String.valueOf(s).toLowerCase());
                                int indexAuthor = book.getAuthor().toLowerCase().indexOf(String.valueOf(s).toLowerCase());
                                int indexIsbn = (book.getIsbn() != null) ? book.getIsbn().indexOf(String.valueOf(s)) : -1;

                                return (indexTitle != -1 || indexAuthor != -1 || indexIsbn != -1) ? true : false;
                            })
                            .collect(Collectors.toList());

                    if (booksFound.size() > 0) {
                        setSearchResultAdapter();
                    } else {
                        setEmptySearchResultAdapter();
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return inflate;
    }

    private void setEmptySearchResultAdapter() {
        SearchResultListAdapter adapter = new SearchResultListAdapter(getActivity(), Arrays.asList(new Book("Kliknij tutaj aby dodać nową książkę", "Nie znaleziono szukanej książki")));
        searchBookResultList.setAdapter(adapter);
        searchBookResultList.setOnItemClickListener(openAddBookForm());
    }

    private void setSearchResultAdapter() {
        SearchResultListAdapter adapter = new SearchResultListAdapter(getActivity(), booksFound);
        searchBookResultList.setAdapter(adapter);
        searchBookResultList.setOnItemClickListener(bookItemClick());
    }

    private AdapterView.OnItemClickListener bookItemClick() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openBookActivity(booksFound.get(position));
            }
        };
    }

    private AdapterView.OnItemClickListener openAddBookForm() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openActivity(AddBookActivity.class);
            }
        };
    }

    private void openBookActivity(Book book) {
        Intent intent = new Intent(getActivity().getApplicationContext(), BookActivity.class);
        intent.putExtra("book", book);
        startActivity(intent);
    }

    private void openActivity(Class<?> activity) {
        Intent intent = new Intent(getActivity().getApplicationContext(), activity);
        startActivity(intent);
    }
}