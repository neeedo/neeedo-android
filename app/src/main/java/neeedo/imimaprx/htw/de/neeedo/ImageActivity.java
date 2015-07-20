package neeedo.imimaprx.htw.de.neeedo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import neeedo.imimaprx.htw.de.neeedo.utils.ServerConstantsUtils;

public class ImageActivity extends ActionBarActivity {
    public final static String IMAGES_LIST_EXTRA = "images-list-intent-data";
    public final static String IMAGES_LIST_POSITION_EXTRA = "images-list-position-intent-data";
    private View view;
    private ImageView imageView;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = getLayoutInflater().inflate(R.layout.activity_image, null);
        intent = getIntent();

        imageView = (ImageView) view.findViewById(R.id.imageView);

        if(intent != null) {
            ArrayList<String> imageList = intent.getStringArrayListExtra(IMAGES_LIST_EXTRA);
            int position = intent.getIntExtra(IMAGES_LIST_POSITION_EXTRA, 0);

            String imageUrl = ServerConstantsUtils.getActiveServer() + "images/" + imageList.get(position);

            Log.d("Image", imageUrl);

            Picasso.with(this).load(imageUrl).into(imageView);
        }

        setContentView(view);
    }
}
