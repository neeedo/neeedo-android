package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.demand.Demand;
import neeedo.imimaprx.htw.de.neeedo.entities.offer.Offer;
import neeedo.imimaprx.htw.de.neeedo.entities.offer.Offers;
import neeedo.imimaprx.htw.de.neeedo.events.GetOfferFinishedEvent;
import neeedo.imimaprx.htw.de.neeedo.fragments.adapters.ListProductsArrayAdapter;
import neeedo.imimaprx.htw.de.neeedo.models.ActiveUser;
import neeedo.imimaprx.htw.de.neeedo.models.OffersModel;
import neeedo.imimaprx.htw.de.neeedo.rest.offer.GetOffersAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.util.BaseAsyncTask;

public class ListOffersFragment extends SuperFragment {

    private final ActiveUser activeUser = ActiveUser.getInstance();
    private ListView listView;
    private View view;
    private ProgressBar progressBar;
    private OffersModel offersModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        view = inflater.inflate(R.layout.list_products_view, container, false);

        listView = (ListView) view.findViewById(R.id.listview);
        progressBar = (ProgressBar) view.findViewById(R.id.list_offers_progessbar);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        offersModel = OffersModel.getInstance();
        BaseAsyncTask.GetEntitiesMode listMode = BaseAsyncTask.GetEntitiesMode.GET_RANDOM;
        if (activeUser.hasActiveUser()) {
            listMode = BaseAsyncTask.GetEntitiesMode.GET_BY_USER;

            if (offersModel.isUseLocalList()) {
                offersModel.setUseLocalList(false);
                fillList(null);
                return;
            }
        }
        new GetOffersAsyncTask(listMode, 100, 0).execute();
    }

    @Subscribe
    public void fillList(GetOfferFinishedEvent e) {
        ArrayList<Offer> offerList = offersModel.getOffers();

        TextView tvEmpty = (TextView) view.findViewById(R.id.tvEmpty);

        if (!offerList.isEmpty()) {
            ListProductsArrayAdapter<Demand> adapter = new ListProductsArrayAdapter(getActivity(),
                    R.layout.list_products_item, offerList);
            adapter.notifyDataSetChanged();
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
