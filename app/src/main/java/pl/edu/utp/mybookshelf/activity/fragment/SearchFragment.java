package pl.edu.utp.mybookshelf.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import pl.edu.utp.mybookshelf.R;
import pl.edu.utp.mybookshelf.activity.BookActivity;
import pl.edu.utp.mybookshelf.adapter.SearchResultListAdapter;
import pl.edu.utp.mybookshelf.model.Book;
import pl.edu.utp.mybookshelf.model.Category;

public class SearchFragment extends Fragment {

    private List<Book> booksFound = new ArrayList<>();
    private ListView searchBookResultList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Book hp = new Book(1l, "J.K. Rowling", "Harry Potter i Kamień Filozoficzny",
                "Książka „Harry Potter i Kamień Filozoficzny” rozpoczyna cykl o młodym czarodzieju i jego licznych przygodach. " +
                        "Tytułowy Harry Potter wychowywany jest przez nieprzychylnych mu ciotkę i wuja. Jego rodzice zginęli w tajemniczych " +
                        "okolicznościach, a jedyne, co mu po nich pozostało to blizna na czole w kształcie błyskawicy. W dniu swoich " +
                        "11. urodzin bohater dowiaduje się, że istnieje świat, o którym nie miał pojęcia", R.drawable.book_1,
                "9877323132", 326, LocalDate.of(1997, 10, 1), new Category(1l, "Fantasy"), null);

        booksFound.add(hp);
        booksFound.add(new Book("J.K. Rowling", "Harry Potter i Komnata Tajemnic", R.drawable.book_1));
        booksFound.add(new Book("J.K. Rowling", "Harry Potter i Więzień Azkabanu", R.drawable.book_1));
        booksFound.add(new Book("J.K. Rowling", "Harry Potter i Czara Ognia", R.drawable.book_1));
        booksFound.add(new Book("Adam Mickiewicz", "Pan Tadeusz", R.drawable.book_2));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_search, container, false);

        searchBookResultList = inflate.findViewById(R.id.search_book_result_list);
        SearchResultListAdapter adapter = new SearchResultListAdapter(getActivity(), booksFound);
        searchBookResultList.setAdapter(adapter);
        searchBookResultList.setOnItemClickListener(bookItemClick());

        return inflate;
    }

    private AdapterView.OnItemClickListener bookItemClick() {
        return new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openBookActivity(booksFound.get(position));
            }
        };
    }

    private void openBookActivity(Book book) {
        Intent intent = new Intent(getActivity().getApplicationContext(), BookActivity.class);
        intent.putExtra("book", book);
        startActivity(intent);
    }
}