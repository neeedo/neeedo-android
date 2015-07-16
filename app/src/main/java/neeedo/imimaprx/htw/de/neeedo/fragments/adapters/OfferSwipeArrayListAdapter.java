package neeedo.imimaprx.htw.de.neeedo.fragments.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.utils.ServerConstantsUtils;

public class OfferSwipeArrayListAdapter extends ArrayAdapter {

    private final ArrayList<SwipeCardViewItem> swipeCardViewItems;
    private final LayoutInflater inflater;
    private final int layoutId;
    private final Activity activity;

    private int currentImageInPreview = 0;

    public OfferSwipeArrayListAdapter(FragmentActivity activity, int layoutId, ArrayList<SwipeCardViewItem> swipeCardViewItems) {
        super(activity, layoutId);

        this.activity = activity;
        this.swipeCardViewItems = swipeCardViewItems;
        this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.layoutId = layoutId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = inflater.inflate(layoutId, parent, false);

        final TextView titleTextView = (TextView) view.findViewById(R.id.card_title);
        final TextView descriptionTextView = (TextView) view.findViewById(R.id.card_description);
        final ImageView imageView = (ImageView) view.findViewById(R.id.card_image);

        final SwipeCardViewItem offerItem = swipeCardViewItems.get(position);

        ImageButton leftButton = (ImageButton) view.findViewById(R.id.button_diolor_gallery_left);
        leftButton
                .setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            switchImageInGallery(-1, offerItem, imageView);
                                        }
                                    }
                );

        ImageButton rightButton = (ImageButton) view.findViewById(R.id.button_diolor_gallery_right);
        rightButton
                .setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            switchImageInGallery(+1, offerItem, imageView);
                                        }
                                    }
                );


        if (offerItem.getImages().isEmpty()) {
            Picasso.with(getContext()).load(R.drawable.no_image).fit().centerInside().into(imageView);
            rightButton.setVisibility(View.GONE);
            leftButton.setVisibility(View.GONE);
        } else if (offerItem.getImages().size() == 1) {
            Picasso.with(getContext()).load(ServerConstantsUtils.getActiveServer() + "images/" + offerItem.getImages().get(0)).fit().centerInside().into(imageView);
            rightButton.setVisibility(View.GONE);
            leftButton.setVisibility(View.GONE);
        } else {
            Picasso.with(getContext()).load(ServerConstantsUtils.getActiveServer() + "images/" + offerItem.getImages().get(0)).fit().centerInside().into(imageView);
        }

        titleTextView.setText(offerItem.getTitle());
        descriptionTextView.setText(offerItem.getDescription());

        return view;
    }

    private void switchImageInGallery(int direction, SwipeCardViewItem offerItem, ImageView imageView) {
        currentImageInPreview += direction;

        int numberOfImages = offerItem.getImages().size();
        if (currentImageInPreview > numberOfImages - 1)
            currentImageInPreview = 0;
        if (currentImageInPreview < 0)
            currentImageInPreview = numberOfImages - 1;

        Picasso.with(getContext()).load(ServerConstantsUtils.getActiveServer() + "images/" + offerItem.getImages().get(currentImageInPreview)).fit().centerInside().into(imageView);
    }

    // the following methods are a bit of a hack due to the fact that swipe lib doesn't expose
    // a proper interface or super class

    @Override
    public int getCount() {
        return swipeCardViewItems.size();
    }

    @Override
    public Context getContext() {
        return activity;
    }

    @Override
    public SwipeCardViewItem getItem(int position) {
        return swipeCardViewItems.get(position);
    }
}
