package pl.edu.utp.mybookshelf.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import pl.edu.utp.mybookshelf.R;

public class BookshelfFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_bookshelf, container, false);
        inflate.findViewById(R.id.textButton).setOnClickListener(l -> Log.d(BookshelfFragment.class.getName(), "Hello"));
        return inflate;
    }
}