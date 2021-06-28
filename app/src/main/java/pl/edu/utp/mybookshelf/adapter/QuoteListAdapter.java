package pl.edu.utp.mybookshelf.adapter;

import android.app.Activity;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.List;

import pl.edu.utp.mybookshelf.R;
import pl.edu.utp.mybookshelf.model.Quote;

public class QuoteListAdapter implements ListAdapter {

    private final Activity context;
    private final List<Quote> quotes;

    public QuoteListAdapter(Activity context, List<Quote> quotes) {
        this.context = context;
        this.quotes = quotes;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
    }

    @Override
    public int getCount() {
        return quotes.size();
    }

    @Override
    public Object getItem(int position) {
        return quotes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.book_quote_item, null, true);

        TextView quoteText = rowView.findViewById(R.id.quote_text);
        TextView pageText = rowView.findViewById(R.id.quote_page);
        TextView userText = rowView.findViewById(R.id.quote_user);

        quoteText.setText(quotes.get(position).getText());
        if (quotes.get(position).getPage() != null && !quotes.get(position).getPage().isEmpty()) {
            pageText.setText("Strona " + quotes.get(position).getPage());
        }
        if (quotes.get(position).getUser() != null) {
            if (quotes.get(position).getUser().getFullName() != null && !quotes.get(position).getUser().getFullName().isEmpty()) {
                userText.setText(quotes.get(position).getUser().getFullName());
            } else {
                userText.setText(quotes.get(position).getUser().getEmail());
            }
        }
        return rowView;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return quotes.size();
    }

    @Override
    public boolean isEmpty() {
        return quotes.isEmpty();
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }
}

