package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.squareup.otto.Subscribe;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.demand.Demand;
import neeedo.imimaprx.htw.de.neeedo.entities.demand.SingleDemand;
import neeedo.imimaprx.htw.de.neeedo.entities.offer.Offer;
import neeedo.imimaprx.htw.de.neeedo.entities.offer.Offers;
import neeedo.imimaprx.htw.de.neeedo.entities.util.Price;
import neeedo.imimaprx.htw.de.neeedo.events.FoundMatchesEvent;
import neeedo.imimaprx.htw.de.neeedo.events.ServerResponseEvent;
import neeedo.imimaprx.htw.de.neeedo.fragments.adapters.OfferSwipeArrayListAdapter;
import neeedo.imimaprx.htw.de.neeedo.fragments.adapters.SwipeCardViewItem;
import neeedo.imimaprx.htw.de.neeedo.models.DemandsModel;
import neeedo.imimaprx.htw.de.neeedo.models.OffersModel;
import neeedo.imimaprx.htw.de.neeedo.rest.demand.GetDemandByIDAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.demand.GetDemandsAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.matching.GetOffersToDemandAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.offer.GetOffersAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.util.BaseAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.util.DeleteAsyncTask;


public class SingleDemandFragmentSwiper extends SuperFragment implements View.OnClickListener {
    private Button btnDeleteDemand;
    private Button btnEditDemand;

    private TextView tvMustTags;
    private TextView tvShouldTags;
    private TextView tvDistance;
    private TextView tvPrice;
    private TextView tvUser;
    private View view;
    private Demand currentDemand;

    private DemandsModel demandsModel = DemandsModel.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.single_demand_view_swiper, container, false);

        tvMustTags = (TextView) view.findViewById(R.id.tvMustTags);
        tvShouldTags = (TextView) view.findViewById(R.id.tvShouldTags);
        tvDistance = (TextView) view.findViewById(R.id.tvDistance);
        tvPrice = (TextView) view.findViewById(R.id.tvPrice);
        tvUser = (TextView) view.findViewById(R.id.tvUser);
        btnDeleteDemand = (Button) view.findViewById(R.id.btnDelete);
        btnEditDemand = (Button) view.findViewById(R.id.btnEdit);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnDeleteDemand.setOnClickListener(this);
        btnEditDemand.setOnClickListener(this);

        String demandId = getArguments().getString("id");

        currentDemand = demandsModel.getDemandById(demandId);

        if (currentDemand == null) {
            SingleDemand singleDemand = demandsModel.getSingleDemand();
            if (singleDemand == null) {
                new GetDemandByIDAsyncTask(demandId).execute();
                return;
            } else {
                currentDemand = singleDemand.getDemand();
                DemandsModel.getInstance().setSingleDemand(null);
            }
        }

        DecimalFormat priceFormat = new DecimalFormat(getActivity().getString(R.string.format_price));
        DecimalFormat distanceFormat = new DecimalFormat(getActivity().getString(R.string.format_distance));

        tvMustTags.setText(currentDemand.getMustTagsString());
        tvShouldTags.setText(currentDemand.getShouldTagsString());
        tvDistance.setText(getActivity().getString(R.string.item_distance) + ": " + distanceFormat.format(currentDemand.getDistance()));
        tvPrice.setText(getActivity().getString(R.string.item_price) + ": " + priceFormat.format(currentDemand.getPrice().getMin()) + " - " + priceFormat.format(currentDemand.getPrice()));
        tvUser.setText(currentDemand.getUser().getName());

        OffersModel.getInstance().setOffers(null);
        new GetOffersToDemandAsyncTask(currentDemand).execute();
    }

    @Subscribe
    public void fillMatches(FoundMatchesEvent e) {
        Offers offers = OffersModel.getInstance().getOffers();
        List<Offer> offerArrayList = offers.getOffers();


        final SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) getActivity().findViewById(R.id.swipe_frame);
        final ArrayList<SwipeCardViewItem> swipeCardViewItems = new ArrayList<SwipeCardViewItem>();

        for (Offer currentOffer : offerArrayList) {
            swipeCardViewItems.add(new SwipeCardViewItem(currentOffer));
        }

        ArrayList<String> sampleImageArray = new ArrayList<String>();
        sampleImageArray.add("http://i.imgur.com/RVhlr3C.jpg");
        sampleImageArray.add("http://i.imgur.com/ZGIRhB1.png");
        sampleImageArray.add("http://i.imgur.com/DvpvklR.png");

        Offer testOffer = new Offer();
        testOffer.setImages(sampleImageArray);
        testOffer.setName("foo1");

        swipeCardViewItems.add(new SwipeCardViewItem(testOffer));
        swipeCardViewItems.add(new SwipeCardViewItem(testOffer));
        swipeCardViewItems.add(new SwipeCardViewItem(testOffer));
        swipeCardViewItems.add(new SwipeCardViewItem(testOffer));

        final ArrayAdapter<String> titleArrayAdapter = new OfferSwipeArrayListAdapter(getActivity(), R.layout.diolor_item, swipeCardViewItems);

        flingContainer.setAdapter(titleArrayAdapter);

        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                Log.d("LIST", "removed object!");
                if (swipeCardViewItems.size() > 0) {
                    swipeCardViewItems.remove(0);
                }

                titleArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                SwipeCardViewItem swipeCardViewItem = (SwipeCardViewItem) dataObject;
                Offer offer = swipeCardViewItem.getOffer();
                //TODO dismiss
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                SwipeCardViewItem swipeCardViewItem = (SwipeCardViewItem) dataObject;
                Offer offer = swipeCardViewItem.getOffer();
                //TODO fav
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
//                swipeCardViewItems.add(new SwipeCardViewItem("", "", new ArrayList<String>()));
//                titleArrayAdapter.notifyDataSetChanged();
                if (itemsInAdapter == 0)
                    getActivity().findViewById(R.id.buttons_diolor_chooser).setVisibility(View.GONE);
            }

            @Override
            public void onScroll(float scrollProgressPercent) {
                View view = flingContainer.getSelectedView();
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });

        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                Toast.makeText(getActivity(), "Clicked!", Toast.LENGTH_LONG);
            }
        });

        getActivity().findViewById(R.id.button_diolor_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flingContainer.getTopCardListener().selectRight();
            }
        });

        getActivity().findViewById(R.id.button_diolor_left).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flingContainer.getTopCardListener().selectLeft();
            }
        });


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnDelete:
                DemandsModel.getInstance().setPostDemand(currentDemand);
                if (currentDemand == null) {
                    return;
                }
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
