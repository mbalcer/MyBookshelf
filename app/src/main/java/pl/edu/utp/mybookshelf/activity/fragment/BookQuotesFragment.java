package pl.edu.utp.mybookshelf.activity.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import pl.edu.utp.mybookshelf.R;
import pl.edu.utp.mybookshelf.adapter.QuoteListAdapter;
import pl.edu.utp.mybookshelf.model.Book;

public class BookQuotesFragment extends Fragment {

    private Book book;
    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_book_quotes, container, false);
        if (getActivity().getIntent().getExtras() != null && getActivity().getIntent().getExtras().getSerializable("book") != null) {
            book = (Book) getActivity().getIntent().getExtras().getSerializable("book");
        }
        if (book.getQuotes() != null && !book.getQuotes().isEmpty()) {
            listView = inflate.findViewById(R.id.quotes_list_view);
            QuoteListAdapter adapter = new QuoteListAdapter(getActivity(), book.getQuotes());
            listView.setAdapter(adapter);
            setListViewHeightBasedOnChildren();
        } else {
            createInfoZeroQuotes(inflate);
        }
        return inflate;
    }

    private void createInfoZeroQuotes(View inflate) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 50, 0, 0);

        TextView infoZeroQuotes = new TextView(getContext());
        infoZeroQuotes.setTextSize(18);
        infoZeroQuotes.setTypeface(null, Typeface.BOLD);
        infoZeroQuotes.setGravity(Gravity.CENTER);
        infoZeroQuotes.setText(R.string.info_zero_quotes);
        infoZeroQuotes.setLayoutParams(params);

        LinearLayout linearLayout = inflate.findViewById(R.id.quotes_layout);
        linearLayout.addView(infoZeroQuotes);
    }

    private void setListViewHeightBasedOnChildren() {
        ListAdapter adapter = listView.getAdapter();
        if (listView != null) {
            int totalHeight = 0;
            for (int i = 0; i < adapter.getCount(); i++) {
                View item = adapter.getView(i, null, listView);
                item.measure(0, 0);
                totalHeight += item.getMeasuredHeight() + 20;
                // TODO: to check why this +20 is required
            }
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
            listView.setLayoutParams(params);
        }
    }

}