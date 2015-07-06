package neeedo.imimaprx.htw.de.neeedo.service;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;

public class ImageDownloader extends AsyncTask<Void, Void, Bitmap> {
    private final String url;
    ImageView bmImage;

    public ImageDownloader(ImageView bmImage, String url) {
        this.bmImage = bmImage;
        this.url = url;
    }

    protected Bitmap doInBackground(Void... urls) {
        Bitmap mIcon = null;
        try {
            InputStream in = new java.net.URL(url).openStream();
            mIcon = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
//            Log.e("Error", e.getMessage());
            System.out.println();
        }
        return mIcon;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
    }
}