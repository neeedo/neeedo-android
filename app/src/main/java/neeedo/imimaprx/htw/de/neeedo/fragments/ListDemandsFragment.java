package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.demand.Demand;
import neeedo.imimaprx.htw.de.neeedo.events.GetDemandListFinishedEvent;
import neeedo.imimaprx.htw.de.neeedo.fragments.adapters.ListProductsArrayAdapter;
import neeedo.imimaprx.htw.de.neeedo.models.DemandsModel;
import neeedo.imimaprx.htw.de.neeedo.rest.demand.GetDemandsAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.favorites.GetFavoritesByIDAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.util.BaseAsyncTask;

public class ListDemandsFragment extends ListFragment {
    private DemandsModel demandsModel;

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        demandsModel = DemandsModel.getInstance();
        if (activeUser.hasActiveUser()) {
            BaseAsyncTask.GetEntitiesMode listMode = BaseAsyncTask.GetEntitiesMode.GET_BY_USER;
            if (demandsModel.isUseLocalList()) {
                demandsModel.setUseLocalList(false);
                fillList(null);
                return;
            }
            new GetDemandsAsyncTask(listMode, 100, 0).execute();
            new GetFavoritesByIDAsyncTask(activeUser.getUserId()).execute();
        } else {
            fillList(null);
        }
    }

    @Subscribe
    public void fillList(GetDemandListFinishedEvent e) {

        List<Demand> demandList = new ArrayList<>();


        if (e == null) {
            demandsModel.setLastDeletedEntityId("");
        }

        TextView tvEmpty = (TextView) view.findViewById(R.id.tvEmpty);


        ListProductsArrayAdapter<Demand> adapter = new ListProductsArrayAdapter(getActivity(),
                R.layout.list_products_item, demandList, Demand.class);

        ArrayList<Demand> tempList = demandsModel.getDemands();

        if (tempList.isEmpty()) {
            tvEmpty.setText(getActivity().getString(R.string.empty_demands_message));
            tvEmpty.setVisibility(View.VISIBLE);
            tvHeader.setVisibility(View.GONE);
            progressBar.setVisibility(View.GONE);
            return;
        }

        for (Demand demand : tempList) {
            adapter.add(demand);
        }
        adapter.notifyDataSetChanged();

        listView.setAdapter(adapter);

        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Demand demand = (Demand) listView.getItemAtPosition(position);

                FragmentManager fragmentManager = getFragmentManager();
                Fragment fragment = new SingleDemandFragmentSwiper();

                Bundle args = new Bundle();
                args.putString("id", demand.getId());
                fragment.setArguments(args);

                fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.container, fragment)
                        .commit();
            }
        });

        tvHeader.setText(getResources().getString(R.string.demands));

        tvEmpty.setVisibility(View.GONE);

        progressBar.setVisibility(View.GONE);
    }
}
