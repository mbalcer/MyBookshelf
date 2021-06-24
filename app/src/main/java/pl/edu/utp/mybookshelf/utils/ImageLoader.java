package pl.edu.utp.mybookshelf.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import pl.edu.utp.mybookshelf.R;

public class ImageLoader extends AsyncTask<String, Void, Bitmap> {

    private final ImageView imageView;
    private final Context context;

    public ImageLoader(ImageView imageView, Context context) {
        this.imageView = imageView;
        this.context = context;
    }

    @Override
    protected Bitmap doInBackground(String... strings) {
        String imageUrl = strings[0];
        Bitmap bitmap = null;
        try {
            InputStream inputStream = new URL(imageUrl).openStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
            ImageLoader imageLoader = new ImageLoader(imageView, context);
            imageLoader.execute(context.getResources().getString(R.string.unknown_book_url));
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
    }

}
