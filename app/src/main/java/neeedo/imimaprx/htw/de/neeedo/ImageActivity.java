package neeedo.imimaprx.htw.de.neeedo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import neeedo.imimaprx.htw.de.neeedo.utils.ServerConstantsUtils;

public class ImageActivity extends ActionBarActivity {
    public final static String IMAGES_LIST_EXTRA = "images-list-intent-data";
    public final static String IMAGES_LIST_POSITION_EXTRA = "images-list-position-intent-data";
    private View view;
    private Intent intent;
    private ImageView imageView;
    private Button btnNext;
    private Button btnPrev;
    private ArrayList<String> imageList;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        view = getLayoutInflater().inflate(R.layout.activity_image, null);
        intent = getIntent();

        if(intent != null) {
            imageList = intent.getStringArrayListExtra(IMAGES_LIST_EXTRA);
            position = intent.getIntExtra(IMAGES_LIST_POSITION_EXTRA, 0);

            loadImageView();
        }

    }

    private void loadImageView() {
        imageView = (ImageView) view.findViewById(R.id.imageView);
        btnNext = (Button) view.findViewById(R.id.btnNext);
        btnPrev = (Button) view.findViewById(R.id.btnPrev);

        if(imageList != null) {
            String imageUrl = ServerConstantsUtils.getActiveServer() + "images/" + imageList.get(position);

            Log.d("Image", imageUrl);

            Picasso.with(this).load(imageUrl).into(imageView);

            if (position == 0) {
                btnPrev.setVisibility(View.INVISIBLE);
            } else {
                btnPrev.setVisibility(View.VISIBLE);
            }
            if (position == imageList.size()-1) {
                btnNext.setVisibility(View.INVISIBLE);
            } else {
                btnNext.setVisibility(View.VISIBLE);
            }

            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    position++;
                    loadImageView();
                }
            });
            btnPrev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    position--;
                    loadImageView();
                }
            });
        }

        setContentView(view);
    }
}
