package pl.edu.utp.mybookshelf.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;

import androidx.fragment.app.Fragment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import pl.edu.utp.mybookshelf.R;
import pl.edu.utp.mybookshelf.activity.BookActivity;
import pl.edu.utp.mybookshelf.adapter.BookshelfListAdapter;
import pl.edu.utp.mybookshelf.database.DBHelper;
import pl.edu.utp.mybookshelf.model.Book;
import pl.edu.utp.mybookshelf.model.BookState;
import pl.edu.utp.mybookshelf.model.Category;
import pl.edu.utp.mybookshelf.model.Review;

public class BookshelfFragment extends Fragment {

    private final HashMap<BookState, List<Book>> myBooks = new HashMap<>();
    private ExpandableListView bookshelfListView;

    private List<Book> booksFromRemote = new ArrayList<>();

    private DBHelper dbHelper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbHelper = new DBHelper(getContext());
        booksFromRemote = getBooksFromRemoteDatabase();

        myBooks.put(BookState.TO_READ, new ArrayList<>());
        myBooks.put(BookState.READ, new ArrayList<>());

        dbHelper.getAllByBookState(BookState.TO_READ).forEach(local -> {
            Optional<Book> optionalBook = booksFromRemote.stream()
                    .filter(remote -> remote.getId().equals(local.getBookId())).findFirst();
            optionalBook.ifPresent(book -> myBooks.get(BookState.TO_READ).add(book));
        });

        dbHelper.getAllByBookState(BookState.READ).forEach(local -> {
            Optional<Book> optionalBook = booksFromRemote.stream()
                    .filter(remote -> remote.getId().equals(local.getBookId())).findFirst();
            optionalBook.ifPresent(book -> myBooks.get(BookState.READ).add(book));
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_bookshelf, container, false);

        BookshelfListAdapter adapter = new BookshelfListAdapter(getActivity(), myBooks);
        bookshelfListView = inflate.findViewById(R.id.bookshelf_list);
        bookshelfListView.setAdapter(adapter);
        bookshelfListView.setOnChildClickListener(bookshelfItemClick());

        return inflate;
    }

    private ExpandableListView.OnChildClickListener bookshelfItemClick() {
        return new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Book book = myBooks.get(myBooks.keySet().stream().collect(Collectors.toList()).get(groupPosition)).get(childPosition);
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

    // TODO: pobieranie książek ze zdalnej bazy danych
    private List<Book> getBooksFromRemoteDatabase() {
        Review r1 = new Review(1l, "Dobra książka", 5, null);
        Review r2 = new Review(2l, "Mogłaby być lepsza", 3, null);
        Review r3 = new Review(3l, "Nie zachwyca", 2, null);

        Book hp = new Book(1l, "J.K. Rowling", "Harry Potter i Kamień Filozoficzny",
                "Książka „Harry Potter i Kamień Filozoficzny” rozpoczyna cykl o młodym czarodzieju i jego licznych przygodach. " +
                        "Tytułowy Harry Potter wychowywany jest przez nieprzychylnych mu ciotkę i wuja. Jego rodzice zginęli w tajemniczych " +
                        "okolicznościach, a jedyne, co mu po nich pozostało to blizna na czole w kształcie błyskawicy. W dniu swoich " +
                        "11. urodzin bohater dowiaduje się, że istnieje świat, o którym nie miał pojęcia", R.drawable.book_1,
                "9877323132", 326, LocalDate.of(1997, 10, 1), new Category(1l, "Fantasy"), Arrays.asList(r1, r2, r3));

        Book book2 = new Book("J.K. Rowling", "Harry Potter i Komnata Tajemnic");
        book2.setId(2L);
        Book book3 = new Book("J.K. Rowling", "Harry Potter i Więzień Azkabanu");
        book3.setId(3L);
        Book book4 = new Book("J.K. Rowling", "Harry Potter i Czara Ognia", R.drawable.book_1);
        book4.setId(4L);
        Book book5 = new Book("Adam Mickiewicz", "Pan Tadeusz", R.drawable.book_2);
        book5.setId(5L);
        return new ArrayList<>(Arrays.asList(hp, book2, book3, book4, book5));
    }
}