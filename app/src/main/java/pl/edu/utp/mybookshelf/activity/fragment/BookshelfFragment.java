package pl.edu.utp.mybookshelf.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import pl.edu.utp.mybookshelf.R;
import pl.edu.utp.mybookshelf.activity.BookActivity;
import pl.edu.utp.mybookshelf.adapter.BookshelfListAdapter;
import pl.edu.utp.mybookshelf.database.DBHelper;
import pl.edu.utp.mybookshelf.database.FirebaseBook;
import pl.edu.utp.mybookshelf.model.Book;
import pl.edu.utp.mybookshelf.model.BookState;

public class BookshelfFragment extends Fragment {

    private final HashMap<BookState, List<Book>> myBooks = new HashMap<>();
    private ExpandableListView bookshelfListView;
    private DBHelper dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DBHelper(getContext());

        myBooks.put(BookState.TO_READ, new ArrayList<>());
        myBooks.put(BookState.READ, new ArrayList<>());

        FirebaseBook.getAllBooks(books -> {
            dbHelper.getAllByBookState(BookState.TO_READ).forEach(local -> {
                Optional<Book> optionalBook = books.stream()
                        .filter(remote -> remote.getId().equals(local.getBookId())).findFirst();
                optionalBook.ifPresent(book -> myBooks.get(BookState.TO_READ).add(book));
            });

            dbHelper.getAllByBookState(BookState.READ).forEach(local -> {
                Optional<Book> optionalBook = books.stream()
                        .filter(remote -> remote.getId().equals(local.getBookId())).findFirst();
                optionalBook.ifPresent(book -> myBooks.get(BookState.READ).add(book));
            });

            BookshelfListAdapter adapter = new BookshelfListAdapter(getActivity(), myBooks);
            bookshelfListView.setAdapter(adapter);
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_bookshelf, container, false);

        bookshelfListView = inflate.findViewById(R.id.bookshelf_list);
        bookshelfListView.setOnChildClickListener(bookshelfItemClick());

        return inflate;
    }

    private ExpandableListView.OnChildClickListener bookshelfItemClick() {
        return new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Book book = myBooks.get(new ArrayList<>(myBooks.keySet()).get(groupPosition)).get(childPosition);
                Log.d(BookshelfFragment.class.getName(), "Click: " + childPosition + ", book: " + book);
                openBookActivity(book);
                return true;
            }
        };
    }

    private void openBookActivity(Book book) {
        Intent intent = new Intent(getActivity().getApplicationContext(), BookActivity.class);
        intent.putExtra("book", book);
        startActivity(intent);
    }

}