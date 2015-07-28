package neeedo.imimaprx.htw.de.neeedo.fragments.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.demand.Demand;
import neeedo.imimaprx.htw.de.neeedo.entities.favorites.Favorite;
import neeedo.imimaprx.htw.de.neeedo.entities.offer.Offer;
import neeedo.imimaprx.htw.de.neeedo.entities.util.Price;
import neeedo.imimaprx.htw.de.neeedo.utils.ServerConstantsUtils;

public class ListProductsArrayAdapter<Object> extends ArrayAdapter<Object> {
    private Context context;
    private int layoutResourceId;
    private List<Object> products;
    private Class productType;

    private RelativeLayout layoutImage;
    private LinearLayout layoutText;

    private ImageView imageView;
    private TextView tvPrimaryTags;
    private TextView tvSecondaryTags;
    private TextView tvDistance;
    private TextView tvPrice;
    private TextView tvUser;

    public ListProductsArrayAdapter(Context context, int layoutResourceId, List<Object> products) {
        super(context, layoutResourceId, products);

        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.products = products;

        try {
            productType = products.get(0).getClass(); // TODO is there a better way to determine the class?
        } catch (NullPointerException e) {
            productType = null;
        }

    }

    @Override
    public View getView(int position, View row, ViewGroup parent) {
        row = LayoutInflater.from(context).inflate(layoutResourceId, parent, false);

        if(position % 2 == 0) {
            row.setBackgroundColor(0xFF4F9181);
        }

        layoutImage = (RelativeLayout) row.findViewById(R.id.layoutImage);
        layoutText = (LinearLayout) row.findViewById(R.id.layoutText);

        imageView = (ImageView) row.findViewById(R.id.imageView);
        tvPrimaryTags = (TextView) row.findViewById(R.id.tvPrimaryTags);
        tvSecondaryTags = (TextView) row.findViewById(R.id.tvSecondaryTags);
        tvDistance = (TextView) row.findViewById(R.id.tvDistance);
        tvPrice = (TextView) row.findViewById(R.id.tvPrice);
        tvUser = (TextView) row.findViewById(R.id.tvUser);

        DecimalFormat priceFormat = new DecimalFormat(context.getString(R.string.format_price));
        DecimalFormat distanceFormat = new DecimalFormat(context.getString(R.string.format_distance));

        String primaryTagsText = null;
        String secondaryTagsText = null;
        String distanceText = null;
        String priceText = null;
        String userText = null;

        if (productType == null) {

            tvPrimaryTags.setVisibility(View.GONE);
            tvSecondaryTags.setVisibility(View.GONE);
            tvDistance.setVisibility(View.GONE);
            tvPrice.setVisibility(View.GONE);
            tvUser.setVisibility(View.GONE);

        } else if (productType.equals(Demand.class)) {

            Demand demand = (Demand) products.get(position);
            Price price = demand.getPrice();

            primaryTagsText = demand.getMustTagsString();
            secondaryTagsText = demand.getShouldTagsString();
            distanceText = context.getString(
                    R.string.item_distance) + ": " + distanceFormat.format(demand.getDistance());
            if(price.getMin() == price.getMax()) {
                priceText = priceFormat.format(price.getMin());
            } else {
                priceText = priceFormat.format(price.getMin()) + " - " + priceFormat.format(price.getMax());
            }
            userText = demand.getUser().getName();

            // hide image
            RelativeLayout.LayoutParams parentLayoutParams = (RelativeLayout.LayoutParams) layoutText.getLayoutParams();
            parentLayoutParams.setMargins(0, 0, 0, 0);
            layoutImage.setVisibility(View.GONE);
            layoutText.setLayoutParams(parentLayoutParams);

        } else if (productType.equals(Offer.class)) {

            Offer offer = (Offer) products.get(position);

            primaryTagsText = offer.getTagsString();
            priceText = priceFormat.format(offer.getPrice()
                    );
            userText = offer.getUser().getName();

            tvSecondaryTags.setVisibility(View.GONE);
            tvDistance.setVisibility(View.GONE);

            ArrayList<String> images = offer.getImages();
            if (images.isEmpty()) {
                Picasso.with(context).load(R.drawable.no_image).fit().centerInside().into(imageView);
            } else {
                String imageUrl = ServerConstantsUtils.getActiveServer() + "images/" + images.get(0);
                Picasso.with(context).load(imageUrl).fit().centerInside().into(imageView);
            }

        } else if (productType.equals(Favorite.class)) {

            Favorite favorite = (Favorite) products.get(position);

            primaryTagsText = favorite.getTagsString();
            priceText = priceFormat.format(favorite.getPrice()
                    );
            userText = favorite.getUser().getName();

            tvSecondaryTags.setVisibility(View.GONE);
            tvDistance.setVisibility(View.GONE);

            ArrayList<String> images = favorite.getImages();
            if (images.isEmpty()) {
                Picasso.with(context).load(R.drawable.no_image).fit().centerInside().into(imageView);
            } else {
                String imageUrl = ServerConstantsUtils.getActiveServer() + "images/" + images.get(0);
                Picasso.with(context).load(imageUrl).fit().centerInside().into(imageView);
            }

        } else {

            throw new IllegalArgumentException();

        }

        tvPrimaryTags.setText(primaryTagsText);
        tvSecondaryTags.setText(secondaryTagsText);
        tvDistance.setText(distanceText);
        tvPrice.setText(priceText);
        tvUser.setText(userText);

        return row;
    }
}
