package pl.edu.utp.mybookshelf.activity.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import pl.edu.utp.mybookshelf.R;
import pl.edu.utp.mybookshelf.adapter.ReviewListAdapter;
import pl.edu.utp.mybookshelf.model.Book;

public class BookReviewsFragment extends Fragment {

    private Book book;
    private ListView layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_book_reviews, container, false);
        if (getActivity().getIntent().getExtras() != null && getActivity().getIntent().getExtras().getSerializable("book") != null) {
            book = (Book) getActivity().getIntent().getExtras().getSerializable("book");
        }

        layout = inflate.findViewById(R.id.reviews_list_view);
        ReviewListAdapter adapter = new ReviewListAdapter(getActivity(), book.getReviews());
        layout.setAdapter(adapter);
        setListViewHeightBasedOnChildren();

        return inflate;
    }

    private void setListViewHeightBasedOnChildren() {
        ListAdapter adapter = layout.getAdapter();
        if (layout != null) {
            int totalHeight = 0;
            for (int i = 0; i < adapter.getCount(); i++) {
                View item = adapter.getView(i, null, layout);
                item.measure(0, 0);
                totalHeight += item.getMeasuredHeight();
            }

            ViewGroup.LayoutParams params = layout.getLayoutParams();
            params.height = totalHeight + (layout.getDividerHeight() * (adapter.getCount() - 1));
            layout.setLayoutParams(params);
        }
    }
}