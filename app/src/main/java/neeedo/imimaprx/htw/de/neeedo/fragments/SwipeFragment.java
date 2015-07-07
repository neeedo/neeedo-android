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

        final SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) getActivity().findViewById(R.id.frame);

        Offers offers = OffersModel.getInstance().getOffers();
        ArrayList<Offer> offerArrayList = offers.getOffers();

        swipeCardViewItems = new ArrayList<SwipeCardViewItem>();

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
                makeToast(getActivity(), "Clicked!");
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

    static void makeToast(Context ctx, String s) {
        Toast.makeText(ctx, s, Toast.LENGTH_SHORT).show();
    }

}
