package pl.edu.utp.mybookshelf.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import pl.edu.utp.mybookshelf.R;
import pl.edu.utp.mybookshelf.adapter.BookshelfListAdapter;
import pl.edu.utp.mybookshelf.model.Book;

public class BookshelfFragment extends Fragment {

    private List<Book> myBooks = new ArrayList();
    private ListView bookshelfListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myBooks.add(new Book("J.K. Rowling", "Harry Potter i Kamień Filozoficzny", R.drawable.book_1));
        myBooks.add(new Book("J.K. Rowling", "Harry Potter i Komnata Tajemnic", R.drawable.book_1));
        myBooks.add(new Book("J.K. Rowling", "Harry Potter i Więzień Azkabanu", R.drawable.book_1));
        myBooks.add(new Book("J.K. Rowling", "Harry Potter i Czara Ognia", R.drawable.book_1));
        myBooks.add(new Book("Adam Mickiewicz", "Pan Tadeusz", R.drawable.book_2));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_bookshelf, container, false);

        BookshelfListAdapter adapter = new BookshelfListAdapter(getActivity(), myBooks);
        bookshelfListView = inflate.findViewById(R.id.bookshelf_list);
        bookshelfListView.setAdapter(adapter);
        bookshelfListView.setOnItemClickListener(bookshelfItemClick());

        return inflate;
    }

    private AdapterView.OnItemClickListener bookshelfItemClick() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(BookshelfFragment.class.getName(), "Click: " + position + ", book: " + myBooks.get(position));
            }
        };
    }
}