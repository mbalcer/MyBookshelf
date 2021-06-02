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
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import pl.edu.utp.mybookshelf.R;
import pl.edu.utp.mybookshelf.activity.BookActivity;
import pl.edu.utp.mybookshelf.adapter.BookshelfListAdapter;
import pl.edu.utp.mybookshelf.model.Book;
import pl.edu.utp.mybookshelf.model.Category;

public class BookshelfFragment extends Fragment {

    private HashMap<String, List<Book>> myBooks = new HashMap<>();
    private List<Book> books = new ArrayList();
    private ExpandableListView bookshelfListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Book hp = new Book(1l, "J.K. Rowling", "Harry Potter i Kamień Filozoficzny",
                "Książka „Harry Potter i Kamień Filozoficzny” rozpoczyna cykl o młodym czarodzieju i jego licznych przygodach. " +
                        "Tytułowy Harry Potter wychowywany jest przez nieprzychylnych mu ciotkę i wuja. Jego rodzice zginęli w tajemniczych " +
                        "okolicznościach, a jedyne, co mu po nich pozostało to blizna na czole w kształcie błyskawicy. W dniu swoich " +
                        "11. urodzin bohater dowiaduje się, że istnieje świat, o którym nie miał pojęcia", R.drawable.book_1,
                "9877323132", 326, LocalDate.of(1997, 10, 1), new Category(1l, "Fantasy"), null);
        books.add(hp);
        books.add(new Book("J.K. Rowling", "Harry Potter i Komnata Tajemnic", R.drawable.book_1));
        books.add(new Book("J.K. Rowling", "Harry Potter i Więzień Azkabanu", R.drawable.book_1));
        books.add(new Book("J.K. Rowling", "Harry Potter i Czara Ognia", R.drawable.book_1));
        books.add(new Book("Adam Mickiewicz", "Pan Tadeusz", R.drawable.book_2));

        myBooks.put("Do przeczytania", books.subList(0, 3));
        myBooks.put("Przeczytane", books.subList(3,5));
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
}