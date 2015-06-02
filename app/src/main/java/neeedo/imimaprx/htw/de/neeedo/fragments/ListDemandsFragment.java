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
import java.util.List;

import neeedo.imimaprx.htw.de.neeedo.LoginActivity;
import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.Demand;
import neeedo.imimaprx.htw.de.neeedo.entities.Demands;
import neeedo.imimaprx.htw.de.neeedo.events.ServerResponseEvent;
import neeedo.imimaprx.htw.de.neeedo.helpers.LocationHelper;
import neeedo.imimaprx.htw.de.neeedo.models.ActiveUser;
import neeedo.imimaprx.htw.de.neeedo.models.DemandsModel;
import neeedo.imimaprx.htw.de.neeedo.rest.HttpGetDemandsAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.HttpGetDemandsByUserIDAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.SuperHttpAsyncTask;

public class ListDemandsFragment extends SuperFragment {
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

        View view = inflater.inflate(R.layout.list_demands_view, container, false);

        listView = (ListView) view.findViewById(R.id.listview);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SuperHttpAsyncTask asyncTask = new HttpGetDemandsByUserIDAsyncTask();
        asyncTask.execute();
    }

    @Subscribe
    public void fillList(ServerResponseEvent e) {

        Demands demands = DemandsModel.getInstance().getDemands();
        List<Demand> demandList = demands.getDemands();

        ArrayAdapter<Demand> adapter = new ArrayAdapter<Demand>(getActivity(),
                android.R.layout.simple_list_item_1, demandList);
        listView.setAdapter(adapter);

        listView.setClickable(true);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                Demand demand = (Demand) listView.getItemAtPosition(position);

                FragmentManager fragmentManager = getFragmentManager();
                Fragment fragment = new SingleDemandFragment();

                Bundle args = new Bundle();
                args.putString("id", demand.getId()); // pass current item id
                fragment.setArguments(args);

                fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.container, fragment)
                        .commit();
            }
        });
    }
}
