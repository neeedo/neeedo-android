package neeedo.imimaprx.htw.de.neeedo;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.diegocarloslima.byakugallery.lib.TouchImageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import neeedo.imimaprx.htw.de.neeedo.utils.ServerConstantsUtils;

public class ImageActivity extends Activity {
    public final static String IMAGES_LIST_EXTRA = "images-list-intent-data";
    public final static String IMAGES_LIST_POSITION_EXTRA = "images-list-position-intent-data";
    private View view;
    private Intent intent;
    private TouchImageView imageView;
    private ProgressBar progressBar;
    private ImageButton btnNext;
    private ImageButton btnPrev;
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
        imageView = (TouchImageView) view.findViewById(R.id.imageView);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        btnNext = (ImageButton) view.findViewById(R.id.btnNext);
        btnPrev = (ImageButton) view.findViewById(R.id.btnPrev);

        progressBar.setVisibility(View.VISIBLE);

        imageView.setMaxScale(10);

        if(imageList != null) {
            String imageUrl = ServerConstantsUtils.getActiveServer() + "images/" + imageList.get(position);

            Log.d("Image", imageUrl);

            Picasso.with(this).
                    load(imageUrl).
                    into(imageView, new Callback() {
                        @Override
                        public void onSuccess() {
                            progressBar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError() {
                            progressBar.setVisibility(View.GONE);
                            imageView.setImageDrawable(Resources.getSystem().getDrawable(R.drawable.no_image));
                        }
                    });

            if(imageList.size() <= 1) {
                btnNext.setVisibility(View.INVISIBLE);
                btnPrev.setVisibility(View.INVISIBLE);
            } else {
                btnNext.setVisibility(View.VISIBLE);
                btnPrev.setVisibility(View.VISIBLE);
            }

            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (position < imageList.size()-1) {
                        position++;
                    } else {
                        position = 0;
                    }
                    loadImageView();
                }
            });
            btnPrev.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (position <= 0) {
                        position = imageList.size()-1;
                    } else {
                        position--;
                    }
                    loadImageView();
                }
            });
        }

        setContentView(view);
    }
}
