package pl.edu.utp.mybookshelf.adapter;

import android.app.Activity;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import java.time.format.DateTimeFormatter;
import java.util.List;

import pl.edu.utp.mybookshelf.R;
import pl.edu.utp.mybookshelf.model.Review;

public class ReviewListAdapter implements ListAdapter {
    private final Activity context;
    private final List<Review> reviews;

    public ReviewListAdapter(Activity context, List<Review> reviews) {
        this.context = context;
        this.reviews = reviews;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) { }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) { }

    @Override
    public int getCount() {
        return reviews.size();
    }

    @Override
    public Object getItem(int position) {
        return reviews.get(position);
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
        View rowView = inflater.inflate(R.layout.book_review_item, null,true);

        TextView userText = rowView.findViewById(R.id.review_user);
        RatingBar ratingBar = rowView.findViewById(R.id.review_rating);
        TextView timeText = rowView.findViewById(R.id.review_time);
        TextView reviewText = rowView.findViewById(R.id.review_text);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

        userText.setText(reviews.get(position).getUser().getFullName());
        ratingBar.setRating(reviews.get(position).getRating());
        timeText.setText(reviews.get(position).getReviewTime().format(formatter));
        if(reviews.get(position).getText() != null && !reviews.get(position).getText().isEmpty())
            reviewText.setText(reviews.get(position).getText());
        else {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0,0 ,0);
            reviewText.setLayoutParams(params);
        }

        return rowView;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return reviews.size();
    }

    @Override
    public boolean isEmpty() {
        return reviews.isEmpty();
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

