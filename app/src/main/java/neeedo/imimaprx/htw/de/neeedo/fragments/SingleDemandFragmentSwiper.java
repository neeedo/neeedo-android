package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.lorentzos.flingswipe.SwipeFlingAdapterView;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.demand.Demand;
import neeedo.imimaprx.htw.de.neeedo.entities.favorites.Favorite;
import neeedo.imimaprx.htw.de.neeedo.entities.offer.Offer;
import neeedo.imimaprx.htw.de.neeedo.events.DeleteFinishedEvent;
import neeedo.imimaprx.htw.de.neeedo.events.FoundMatchesEvent;
import neeedo.imimaprx.htw.de.neeedo.fragments.adapters.OfferSwipeArrayListAdapter;
import neeedo.imimaprx.htw.de.neeedo.fragments.adapters.SwipeCardViewItem;
import neeedo.imimaprx.htw.de.neeedo.models.ActiveUser;
import neeedo.imimaprx.htw.de.neeedo.models.DemandsModel;
import neeedo.imimaprx.htw.de.neeedo.models.FavoritesModel;
import neeedo.imimaprx.htw.de.neeedo.rest.favorites.GetFavoritesByIDAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.favorites.PostCreateFavoriteAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.matching.GetOffersToDemandAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.util.BaseAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.util.DeleteAsyncTask;


public class SingleDemandFragmentSwiper extends SuperFragment implements View.OnClickListener {

    private ImageButton btnDeleteDemand;
    private ImageButton btnEditDemand;

    private ProgressBar progressBar;

    private View view;
    private Demand currentDemand;

    private ArrayList<SwipeCardViewItem> swipeCardViewItems;
    private ArrayAdapter<String> arrayAdapter;

    private DemandsModel demandsModel = DemandsModel.getInstance();
    private FavoritesModel favouritesModel = FavoritesModel.getInstance();
    private ActiveUser activeUser = ActiveUser.getInstance();

    private String demandId;
    private LinearLayout diolorButtonsContainer;
    private TextView textViewEmpty;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.single_demand_view_swiper, container, false);

        btnDeleteDemand = (ImageButton) view.findViewById(R.id.btnDelete);
        btnEditDemand = (ImageButton) view.findViewById(R.id.btnEdit);

        progressBar = (ProgressBar) view.findViewById(R.id.loading_matches_progress_bar);
        diolorButtonsContainer = (LinearLayout) view.findViewById(R.id.buttons_diolor_chooser);
        textViewEmpty = (TextView) view.findViewById(R.id.list_empty_error);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        btnDeleteDemand.setOnClickListener(this);
        btnEditDemand.setOnClickListener(this);

        demandId = getArguments().getString("id");

        currentDemand = demandsModel.getDemandById(demandId);

        new GetOffersToDemandAsyncTask(currentDemand).execute();
        new GetFavoritesByIDAsyncTask(activeUser.getUserId()).execute();
    }

    @Subscribe
    public void handleFoundMatchesEvent(FoundMatchesEvent event) {
        final SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) getActivity().findViewById(R.id.swipe_frame);

        swipeCardViewItems = new ArrayList<SwipeCardViewItem>();
        List<Offer> offerArrayList = DemandsModel.getInstance().getOfferlistToDemandById(demandId);

        final String sharedPrefsKey = "dismissedOffers" + demandId + activeUser.getUserId();
        SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
        String dismissedOffersString = sharedPref.getString(sharedPrefsKey, "");

        for (int i = offerArrayList.size() - 1; i >= 0; i--) {
            Offer currentOfferSearch = offerArrayList.get(i);
            if (dismissedOffersString.contains(currentOfferSearch.getId())) {
                offerArrayList.remove(currentOfferSearch);
                continue;
            }

            ArrayList<Favorite> favorites = favouritesModel.getFavorites();
            for (Favorite currentFav : favorites) {
                if (currentFav.getId().equals(currentOfferSearch.getId())) {
                    offerArrayList.remove(currentOfferSearch);
                    continue;
                }
            }
        }

        for (Offer currentOffer : offerArrayList) {
            swipeCardViewItems.add(new SwipeCardViewItem(currentOffer));
        }

        arrayAdapter = new OfferSwipeArrayListAdapter(getActivity(), R.layout.diolor_item, swipeCardViewItems);

        flingContainer.setAdapter(arrayAdapter);

        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                Log.d("LIST", "removed object!");
                if (swipeCardViewItems.size() > 0) {
                    swipeCardViewItems.remove(0);
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
                SwipeCardViewItem swipeCardViewItem = (SwipeCardViewItem) dataObject;
                Offer offer = swipeCardViewItem.getOffer();

                SharedPreferences sharedPref = getActivity().getPreferences(Context.MODE_PRIVATE);
                String dismissedOffersString = sharedPref.getString(sharedPrefsKey, "");

                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(sharedPrefsKey, dismissedOffersString + "," + offer.getId());
                editor.commit();
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                SwipeCardViewItem swipeCardViewItem = (SwipeCardViewItem) dataObject;
                Offer offer = swipeCardViewItem.getOffer();
                Favorite favorite = new Favorite();
                favorite.setOfferId(offer.getId());
                favorite.setUserId(ActiveUser.getInstance().getUserId());

                new PostCreateFavoriteAsyncTask(favorite).execute();

            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                if (itemsInAdapter == 0) {
                    textViewEmpty.setVisibility(View.VISIBLE);
                    diolorButtonsContainer.setVisibility(View.GONE);
                }
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
                SwipeCardViewItem swipeCardViewItem = (SwipeCardViewItem) dataObject;
                Offer offer = swipeCardViewItem.getOffer();

                FragmentManager fragmentManager = getFragmentManager();
                Fragment fragment = new SingleOfferFragment();

                Bundle args = new Bundle();
                args.putString("id", offer.getId()); // pass current item id
                fragment.setArguments(args);

                fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.container, fragment)
                        .commit();

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

        arrayAdapter.notifyDataSetChanged();
        progressBar.setVisibility(View.GONE);
    }

    @Subscribe
    public void demandDeleted(DeleteFinishedEvent e) {
        redirectToFragment(ListDemandsFragment.class);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnDelete:
                demandsModel.setDraft(currentDemand);
                if (currentDemand == null) {
                    return;
                }
                BaseAsyncTask asyncTask = new DeleteAsyncTask(currentDemand);
                ConfirmDialogFragment confirmDialog = ConfirmDialogFragment.newInstance(
                        asyncTask,
                        null,
                        getResources().getString(R.string.dialog_delete_demand)
                );
                confirmDialog.show(getFragmentManager(), getString(R.string.confirm));
                break;

            case R.id.btnEdit:
                demandsModel.setDraft(currentDemand);
                redirectToFragment(EditDemandFragment.class);
                break;
        }

    }


}
