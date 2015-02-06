package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.squareup.otto.Subscribe;
import java.util.ArrayList;
import java.util.List;
import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.Demand;
import neeedo.imimaprx.htw.de.neeedo.events.ServerResponseEvent;
import neeedo.imimaprx.htw.de.neeedo.models.DemandsModel;
import neeedo.imimaprx.htw.de.neeedo.rest.HttpGetAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.SuperHttpAsyncTask;

import neeedo.imimaprx.htw.de.neeedo.entities.Demands;

public class ListCardsFragment extends SuperFragment {

    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.list_cards_view, container, false);

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

        // list items
        List<String> list = new ArrayList<String>();
        for(int i = 0; i < demandList.size(); i++) {
            // TODO nice output format
            list.add(demandList.get(i).toString()); // fill with demands
        }

        // array adapter shows items in list view
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);

        // TODO set item listeners for single view
    }
}
