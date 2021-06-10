package pl.edu.utp.mybookshelf.activity.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import androidx.fragment.app.Fragment;

import com.google.android.material.switchmaterial.SwitchMaterial;

import pl.edu.utp.mybookshelf.R;
import pl.edu.utp.mybookshelf.database.DBHelper;
import pl.edu.utp.mybookshelf.model.Book;
import pl.edu.utp.mybookshelf.model.BookState;
import pl.edu.utp.mybookshelf.model.UserBook;

public class BookMyDataFragment extends Fragment {
    private DBHelper dbHelper;
    private Book book;

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
}