package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.offer.Offer;
import neeedo.imimaprx.htw.de.neeedo.entities.offer.Offers;
import neeedo.imimaprx.htw.de.neeedo.fragments.adapters.OfferSwipeArrayListAdapter;
import neeedo.imimaprx.htw.de.neeedo.fragments.adapters.SwipeCardViewItem;
import neeedo.imimaprx.htw.de.neeedo.models.OffersModel;

public class SwipeFragment extends SuperFragment {

    private ArrayAdapter<String> titleArrayAdapter;

    private ArrayList<SwipeCardViewItem> swipeCardViewItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.diolor_main, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) getActivity().findViewById(R.id.frame);

        Offers offers = OffersModel.getInstance().getOffers();
        ArrayList<Offer> offerArrayList = offers.getOffers();

        swipeCardViewItems = new ArrayList<SwipeCardViewItem>();

        for (Offer currentOffer : offerArrayList) {
            swipeCardViewItems.add(new SwipeCardViewItem(currentOffer.getName(), currentOffer.getTagsString(), currentOffer.getImages()));
        }

        titleArrayAdapter = new OfferSwipeArrayListAdapter(getActivity(), R.layout.diolor_item, swipeCardViewItems);

        flingContainer.setAdapter(titleArrayAdapter);

        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                Log.d("LIST", "removed object!");
                swipeCardViewItems.remove(0);
                titleArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                Toast.makeText(getActivity(), "Dismissed!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Toast.makeText(getActivity(), "Liked!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                swipeCardViewItems.add(new SwipeCardViewItem("", "", new ArrayList<String>()));
                titleArrayAdapter.notifyDataSetChanged();
                Log.d("LIST", "notified");
            }

            @Override
            public void onScroll(float v) {

            }
        });

        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
                makeToast(getActivity(), "Clicked!");
            }
        });
    }

    static void makeToast(Context ctx, String s) {
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }

}
