package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.text.DecimalFormat;
import java.util.List;

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.demand.Demand;
import neeedo.imimaprx.htw.de.neeedo.entities.demand.Demands;
import neeedo.imimaprx.htw.de.neeedo.entities.demand.SingleDemand;
import neeedo.imimaprx.htw.de.neeedo.entities.offer.Offer;
import neeedo.imimaprx.htw.de.neeedo.entities.offer.Offers;
import neeedo.imimaprx.htw.de.neeedo.entities.util.Price;
import neeedo.imimaprx.htw.de.neeedo.events.FoundMatchesEvent;
import neeedo.imimaprx.htw.de.neeedo.events.ServerResponseEvent;
import neeedo.imimaprx.htw.de.neeedo.fragments.adapters.ListProductsArrayAdapter;
import neeedo.imimaprx.htw.de.neeedo.models.DemandsModel;
import neeedo.imimaprx.htw.de.neeedo.models.OffersModel;
import neeedo.imimaprx.htw.de.neeedo.rest.demand.GetDemandByIDAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.demand.GetDemandsAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.matching.GetOffersToDemandAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.offer.GetOffersAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.util.BaseAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.util.DeleteAsyncTask;


public class SingleDemandFragment extends SuperFragment implements View.OnClickListener {
    private Button btnDeleteDemand;
    private Button btnEditDemand;
    private View view;
    private ListView matchingView;
    private TextView tvMustTags;
    private TextView tvShouldTags;
    private TextView tvDistance;
    private TextView tvPrice;
    private TextView tvUser;
    private Demand currentDemand;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.single_demand_view, container, false);

        tvMustTags = (TextView) view.findViewById(R.id.tvMustTags);
        tvShouldTags = (TextView) view.findViewById(R.id.tvShouldTags);
        tvDistance = (TextView) view.findViewById(R.id.tvDistance);
        tvPrice = (TextView) view.findViewById(R.id.tvPrice);
        tvUser = (TextView) view.findViewById(R.id.tvUser);

        matchingView = (ListView) view.findViewById(R.id.matchingview);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Activity activity = getActivity();
        btnDeleteDemand = (Button) activity.findViewById(R.id.btnDelete);
        btnEditDemand = (Button) activity.findViewById(R.id.btnEdit);

        btnDeleteDemand.setOnClickListener(this);
        btnEditDemand.setOnClickListener(this);

        BaseAsyncTask asyncTask;

        asyncTask = new GetDemandsAsyncTask(BaseAsyncTask.GetEntitiesMode.GET_RANDOM);
        asyncTask.execute();

        // TODO launch matching task here
        new GetOffersAsyncTask(BaseAsyncTask.GetEntitiesMode.GET_RANDOM).execute();
    }

    private Demand findSingleDemand(String demandId) {
        Demands demands = DemandsModel.getInstance().getDemands();

        List<Demand> demandList = demands.getDemands();
        Demand currentDemand = null;

        for (Demand demand : demandList) {
            if (demand.getId().equals(demandId)) {
                currentDemand = demand;
                break;
            }
        }

        return currentDemand;
    }

    @Subscribe
    public void fillText(ServerResponseEvent e) {
        String demandId = getArguments().getString("id");
        currentDemand = findSingleDemand(demandId);
        if (currentDemand == null) {
            SingleDemand singleDemand = DemandsModel.getInstance().getSingleDemand();
            if (singleDemand == null) {
                new GetDemandByIDAsyncTask(demandId).execute();
                return;
            } else {
                currentDemand = singleDemand.getDemand();
                DemandsModel.getInstance().setSingleDemand(null);
            }
        }


        Context context = getActivity();

        DecimalFormat priceFormat = new DecimalFormat(context.getString(R.string.format_price));
        DecimalFormat distanceFormat = new DecimalFormat(context.getString(R.string.format_distance));
        Price price = currentDemand.getPrice();

        String mustTagsText = currentDemand.getMustTagsString();
        String shouldTagsText = currentDemand.getShouldTagsString();
        String distanceText = context.getString(
                R.string.item_distance) +
                ": " +
                distanceFormat.format(currentDemand.getDistance()
                );
        String priceText = context.getString(
                R.string.item_price) +
                ": " +
                priceFormat.format(price.getMin()) +
                " - " +
                priceFormat.format(price.getMax()
                );
        String userText = currentDemand.getUser().getName();

        tvMustTags.setText(mustTagsText);
        tvShouldTags.setText(shouldTagsText);
        tvDistance.setText(distanceText);
        tvPrice.setText(priceText);
        tvUser.setText(userText);

        // TODO handle exception if demand not found

        //DemandsModel.getInstance().setPostDemand(currentDemand);
        OffersModel.getInstance().setOffers(null);
        new GetOffersToDemandAsyncTask(currentDemand).execute();

    }

    @Subscribe
    public void fillMatches(FoundMatchesEvent e) {
        Log.d("Matching", "Event called");

        Offers offers = OffersModel.getInstance().getOffers();
        List<Offer> offerList = offers.getOffers();

        TextView tvEmpty = (TextView) view.findViewById(R.id.tvEmpty);
        final ListView matchingView = (ListView) view.findViewById(R.id.matchingview);

        if (!offerList.isEmpty()) {
            ListProductsArrayAdapter<Offer> adapter = new ListProductsArrayAdapter(getActivity(),
                    R.layout.list_products_item, offerList);
            matchingView.setAdapter(adapter);

            matchingView.setClickable(true);
            matchingView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                    Offer offer = (Offer) matchingView.getItemAtPosition(position);

                    FragmentManager fragmentManager = getFragmentManager();
                    Fragment fragment = new SingleOfferFragment();

                    Bundle args = new Bundle();
                    args.putString("id", offer.getId());
                    fragment.setArguments(args);

                    fragmentManager.beginTransaction()
                            .addToBackStack(null)
                            .replace(R.id.container, fragment)
                            .commit();
                }
            });

            tvEmpty.setVisibility(View.GONE);
            matchingView.setVisibility(View.VISIBLE);
        } else {
            matchingView.setVisibility(View.GONE);
            tvEmpty.setVisibility(View.VISIBLE);
            tvEmpty.setText(getActivity().getString(R.string.empty_matching_message));
        }
    }

    @Override
    public void onClick(View view) {
        
        switch (view.getId()) {
            case R.id.btnDelete:
                DemandsModel.getInstance().setPostDemand(currentDemand);
                BaseAsyncTask asyncTask = new DeleteAsyncTask(currentDemand);
                ConfirmDialogFragment confirmDialog = ConfirmDialogFragment.newInstance(
                        asyncTask,
                        ListDemandsFragment.class,
                        getResources().getString(R.string.dialog_delete_demand)
                );
                confirmDialog.show(getFragmentManager(), "confirm");
                break;

            case R.id.btnEdit:
                DemandsModel.getInstance().setPostDemand(currentDemand);
                redirectToFragment(EditDemandFragment.class);
                break;
        }

    }
}
