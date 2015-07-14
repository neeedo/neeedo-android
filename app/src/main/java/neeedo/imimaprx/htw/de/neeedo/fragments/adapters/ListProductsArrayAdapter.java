package neeedo.imimaprx.htw.de.neeedo.fragments.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.demand.Demand;
import neeedo.imimaprx.htw.de.neeedo.entities.offer.Offer;
import neeedo.imimaprx.htw.de.neeedo.entities.util.Price;
import neeedo.imimaprx.htw.de.neeedo.utils.ServerConstantsUtils;

public class ListProductsArrayAdapter<Object> extends ArrayAdapter<Object> {
    private Context context;
    private int layoutResourceId;
    private List<Object> products;
    private Class productType;

    ImageView imageView;
    TextView tvPrimaryTags;
    TextView tvSecondaryTags;
    TextView tvDistance;
    TextView tvPrice;
    TextView tvUser;

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

        if (productType.equals(Demand.class)) {

            Demand demand = (Demand) products.get(position);
            Price price = demand.getPrice();

            primaryTagsText = demand.getMustTagsString();
            secondaryTagsText = demand.getShouldTagsString();
            distanceText = context.getString(
                    R.string.item_distance) + ": " + distanceFormat.format(demand.getDistance());
            priceText = context.getString(
                    R.string.item_price) + ": " + priceFormat.format(price.getMin()) + " - " + priceFormat.format(price.getMax());
            userText = demand.getUser().getName();

        } else if (productType.equals(Offer.class)) {

            Offer offer = (Offer) products.get(position);

            primaryTagsText = offer.getTagsString();
            priceText = context.getString(
                    R.string.item_price) +
                    ": " +
                    priceFormat.format(offer.getPrice()
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

        } else if (productType == null) {

            tvPrimaryTags.setVisibility(View.GONE);
            tvSecondaryTags.setVisibility(View.GONE);
            tvDistance.setVisibility(View.GONE);
            tvPrice.setVisibility(View.GONE);
            tvUser.setVisibility(View.GONE);

        } else {

            throw new IllegalArgumentException();

        }

        // TODO add image (for offers)

        tvPrimaryTags.setText(primaryTagsText);
        tvSecondaryTags.setText(secondaryTagsText);
        tvDistance.setText(distanceText);
        tvPrice.setText(priceText);
        tvUser.setText(userText);

        return row;
    }
}
