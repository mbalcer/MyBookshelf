package pl.edu.utp.mybookshelf.adapter;

import android.app.Activity;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.List;

import pl.edu.utp.mybookshelf.R;
import pl.edu.utp.mybookshelf.model.Book;

public class BookshelfListAdapter implements ListAdapter {

    private final Activity context;
    private final List<Book> books;

    public BookshelfListAdapter(Activity context, List<Book> books) {
        this.context = context;
        this.books = books;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) { }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) { }

    @Override
    public int getCount() {
        return books.size();
    }

    @Override
    public Object getItem(int position) {
        return books.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.book_list_item, null,true);

        TextView titleText = rowView.findViewById(R.id.book_title);
        TextView authorText = rowView.findViewById(R.id.book_author);
        ImageView imageView = rowView.findViewById(R.id.book_image);

        titleText.setText(books.get(position).getTitle());
        authorText.setText(books.get(position).getAuthor());
        imageView.setImageResource(books.get(position).getImage());

        return rowView;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return books.size();
    }

    @Override
    public boolean isEmpty() {
        return false;
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
