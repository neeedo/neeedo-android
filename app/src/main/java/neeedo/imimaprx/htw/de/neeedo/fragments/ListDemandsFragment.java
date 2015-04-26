package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.Demand;
import neeedo.imimaprx.htw.de.neeedo.events.ServerResponseEvent;
import neeedo.imimaprx.htw.de.neeedo.models.DemandsModel;

import neeedo.imimaprx.htw.de.neeedo.entities.Demands;
import neeedo.imimaprx.htw.de.neeedo.rest.HttpGetAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.SuperHttpAsyncTask;

public class ListDemandsFragment extends SuperFragment {

    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.list_demands_view, container, false);

        listView = (ListView) view.findViewById(R.id.listview);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SuperHttpAsyncTask asyncTask;

        // execute async task @Subscribe
        asyncTask = new HttpGetAsyncTask();
        asyncTask.execute();
    }

    @Subscribe
    public void fillList(ServerResponseEvent e) {

        // get demands out of demands model singleton
        Demands demands = DemandsModel.getInstance().getDemands();

        // get array list of demands
        ArrayList<Demand> demandList = demands.getDemands();

        // array adapter shows items in list view
        ArrayAdapter<Demand> adapter = new ArrayAdapter<Demand>(getActivity(),
                android.R.layout.simple_list_item_1, demandList);
        listView.setAdapter(adapter);

        // item click listener
        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                // get clicked item
                Demand demand = (Demand) listView.getItemAtPosition(position);

                // load SingleDemandFragment
                FragmentManager fragmentManager = getFragmentManager();
                Fragment fragment = new SingleDemandFragment();

                // pass arguments to fragment
                Bundle args = new Bundle();
                args.putString("id", demand.getId()); // pass current item id
                fragment.setArguments(args);

                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();
            }
        });
    }
}
