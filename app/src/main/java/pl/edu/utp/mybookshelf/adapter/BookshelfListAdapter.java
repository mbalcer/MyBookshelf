package pl.edu.utp.mybookshelf.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import pl.edu.utp.mybookshelf.R;
import pl.edu.utp.mybookshelf.model.Book;

public class BookshelfListAdapter extends BaseExpandableListAdapter {

    private final Activity context;
    private final Map<String, List<Book>> books;
    private final List<String> listTitles;

    public BookshelfListAdapter(Activity context, Map<String, List<Book>> books) {
        this.context = context;
        this.books = books;
        this.listTitles = books.keySet().stream().collect(Collectors.toList());
    }

    @Override
    public int getGroupCount() {
        return listTitles.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return books.get(listTitles.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listTitles.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return books.get(listTitles.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String listTitle = (String) getGroup(groupPosition);
        LayoutInflater inflater = context.getLayoutInflater();
        convertView = inflater.inflate(R.layout.book_list_group, null);

        TextView listTitleTextView = convertView.findViewById(R.id.book_list_title);
        listTitleTextView.setText(listTitle);
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.book_list_item, null,true);

        TextView titleText = rowView.findViewById(R.id.book_title);
        TextView authorText = rowView.findViewById(R.id.book_author);
        ImageView imageView = rowView.findViewById(R.id.book_image);

        titleText.setText(books.get(listTitles.get(groupPosition)).get(childPosition).getTitle());
        authorText.setText(books.get(listTitles.get(groupPosition)).get(childPosition).getAuthor());
        imageView.setImageResource(books.get(listTitles.get(groupPosition)).get(childPosition).getImage());

        return rowView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
