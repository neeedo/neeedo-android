package neeedo.imimaprx.htw.de.neeedo.fragments.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.List;

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.demand.Demand;
import neeedo.imimaprx.htw.de.neeedo.entities.util.Price;

public class ListDemandsArrayAdapter extends ArrayAdapter<Demand> {
    private Context context;
    private int layoutResourceId;
    private List<Demand> demands;

    TextView tvMustTags;
    TextView tvShouldTags;
    TextView tvDistance;
    TextView tvPrice;
    TextView tvUser;

    public ListDemandsArrayAdapter(Context context, int layoutResourceId, List<Demand> demands) {
        super(context, layoutResourceId, demands);

        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.demands = demands;
    }

    @Override
    public View getView(int position, View row, ViewGroup parent) {
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        row = inflater.inflate(layoutResourceId, parent, false);

        tvMustTags = (TextView) row.findViewById(R.id.tvMustTags);
        tvShouldTags = (TextView) row.findViewById(R.id.tvShouldTags);
        tvDistance = (TextView) row.findViewById(R.id.tvDistance);
        tvPrice = (TextView) row.findViewById(R.id.tvPrice);
        tvUser = (TextView) row.findViewById(R.id.tvUser);

        Demand demand = demands.get(position);
        Price price = demand.getPrice();
        DecimalFormat priceFormat = new DecimalFormat(context.getString(R.string.format_price));
        DecimalFormat distanceFormat = new DecimalFormat(context.getString(R.string.format_distance));

        tvMustTags.setText(
                demand.getMustTagsString()
        );
        tvShouldTags.setText(
                demand.getShouldTagsString()
        );
        tvDistance.setText(context.getString(
                        R.string.item_distance) +
                        ": " +
                        distanceFormat.format(demand.getDistance())
        );
        tvPrice.setText(context.getString(
                        R.string.item_price) +
                        ": " +
                        priceFormat.format(price.getMin()) +
                        " - " +
                        priceFormat.format(price.getMax())
        );
        tvUser.setText(
               demand.getUser().getName()
        );

        return row;
    }
}
