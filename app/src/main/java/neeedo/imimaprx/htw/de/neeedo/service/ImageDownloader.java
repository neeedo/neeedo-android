package neeedo.imimaprx.htw.de.neeedo.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.InputStream;

public class ImageDownloader extends AsyncTask<Void, Void, Bitmap> {
    private final String url;
    private final Context context;
    private ImageView imageView;

    public ImageDownloader(ImageView imageView, String url, Context context) {
        this.imageView = imageView;
        this.url = url;
        this.context = context;
    }

    protected Bitmap doInBackground(Void... urls) {
        Bitmap mIcon = null;
        try {
            InputStream in = new java.net.URL(url).openStream();
            mIcon = BitmapFactory.decodeStream(in);

            Picasso.with(context).load(url).into(imageView);
        } catch (Exception e) {
//            Log.e("Error", e.getMessage());
            System.out.println();
        }
        return mIcon;
    }

    protected void onPostExecute(Bitmap result) {
//        imageView.setImageBitmap(result);
    }
}