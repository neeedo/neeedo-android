package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import neeedo.imimaprx.htw.de.neeedo.LoginActivity;
import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.Offer;
import neeedo.imimaprx.htw.de.neeedo.entities.Offers;
import neeedo.imimaprx.htw.de.neeedo.events.ServerResponseEvent;
import neeedo.imimaprx.htw.de.neeedo.models.ActiveUser;
import neeedo.imimaprx.htw.de.neeedo.models.OffersModel;
import neeedo.imimaprx.htw.de.neeedo.rest.offer.GetOffersAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.BaseAsyncTask;

public class ListOffersFragment extends SuperFragment {

    private final ActiveUser activeUser = ActiveUser.getInstance();
    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!activeUser.userInformationLoaded()) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            // TODO reload view after login
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.list_offers_view, container, false);

        listView = (ListView) view.findViewById(R.id.listview);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        BaseAsyncTask asyncTask;

        asyncTask = new GetOffersAsyncTask();
        asyncTask.execute();
    }

    @Subscribe
    public void fillList(ServerResponseEvent e) {

        Offers offers = OffersModel.getInstance().getOffers();
        ArrayList<Offer> offerList = offers.getOffers();

        ArrayAdapter<Offer> adapter = new ArrayAdapter<Offer>(getActivity(),
                android.R.layout.simple_list_item_1, offerList);
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
    }
}
