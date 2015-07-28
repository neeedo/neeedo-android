package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.demand.Demand;
import neeedo.imimaprx.htw.de.neeedo.entities.offer.Offer;
import neeedo.imimaprx.htw.de.neeedo.events.GetOfferFinishedEvent;
import neeedo.imimaprx.htw.de.neeedo.fragments.adapters.ListProductsArrayAdapter;
import neeedo.imimaprx.htw.de.neeedo.models.OffersModel;
import neeedo.imimaprx.htw.de.neeedo.rest.offer.GetOffersAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.util.BaseAsyncTask;

public class ListOffersFragment extends ListFragment {
    protected OffersModel offersModel;

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        offersModel = OffersModel.getInstance();

        if (activeUser.hasActiveUser()) {
            BaseAsyncTask.GetEntitiesMode listMode = BaseAsyncTask.GetEntitiesMode.GET_BY_USER;

            if (offersModel.isUseLocalList()) {
                offersModel.setUseLocalList(false);
                fillList(null);
                return;
            }
            new GetOffersAsyncTask(listMode, 100, 0).execute();
        } else {
            fillList(null);
        }

    }

    @Subscribe
    public void fillList(GetOfferFinishedEvent e) {
        ArrayList<Offer> offerList = offersModel.getOffers();

        if (e == null) {
            offersModel.setLastDeletedEntityId("");
        }

        TextView tvEmpty = (TextView) view.findViewById(R.id.tvEmpty);

        if (!offerList.isEmpty()) {
            ListProductsArrayAdapter<Demand> adapter = new ListProductsArrayAdapter(getActivity(),
                    R.layout.list_products_item, offerList);

            listView.setAdapter(adapter);

            listView.setClickable(true);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                    Offer offer = (Offer) listView.getItemAtPosition(position);

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

            tvEmpty.setVisibility(View.GONE);
        } else {
            tvEmpty.setText(getActivity().getString(R.string.empty_offers_message));
            tvEmpty.setVisibility(View.VISIBLE);
        }
        progressBar.setVisibility(View.GONE);
    }
}
