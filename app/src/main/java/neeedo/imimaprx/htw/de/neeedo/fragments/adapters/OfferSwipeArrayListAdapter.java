package neeedo.imimaprx.htw.de.neeedo.fragments.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.service.ImageDownloader;

public class OfferSwipeArrayListAdapter extends ArrayAdapter {

    private final ArrayList<SwipeCardViewItem> swipeCardViewItems;
    private final LayoutInflater inflater;
    private final int layoutId;
    private final Activity activity;

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

        TextView titleTextView = (TextView) view.findViewById(R.id.card_title);
        TextView descriptionTextView = (TextView) view.findViewById(R.id.card_description);
        ImageView imageView = (ImageView) view.findViewById(R.id.card_image);

        SwipeCardViewItem offerItem = swipeCardViewItems.get(position);

        if (!offerItem.getImages().isEmpty()) {
            Picasso.with(getContext()).load(offerItem.getImages().get(0)).fit().centerInside().into(imageView);
        }

        titleTextView.setText(offerItem.getTitle());
        descriptionTextView.setText(offerItem.getDescription());

        return view;
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
