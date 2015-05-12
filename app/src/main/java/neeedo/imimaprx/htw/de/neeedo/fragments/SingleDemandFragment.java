package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.Demand;
import neeedo.imimaprx.htw.de.neeedo.entities.Demands;
import neeedo.imimaprx.htw.de.neeedo.events.ServerResponseEvent;
import neeedo.imimaprx.htw.de.neeedo.models.DemandsModel;
import neeedo.imimaprx.htw.de.neeedo.rest.HttpGetDemandsAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.SuperHttpAsyncTask;


public class SingleDemandFragment extends SuperFragment {

    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.single_demand_view, container, false);

        textView = (TextView) view.findViewById(R.id.singleview);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        SuperHttpAsyncTask asyncTask;

        // execute async task @Subscribe
        asyncTask = new HttpGetDemandsAsyncTask();
        asyncTask.execute();
    }

    @Subscribe
    public void fillText(ServerResponseEvent e) {

        // TODO for Offers

        // get card id out of arguments
        String demandId = getArguments().getString("id");

        // get demands out of demands model singleton
        Demands demands = DemandsModel.getInstance().getDemands();

        // get array list of demands
        ArrayList<Demand> demandList = demands.getDemands();
        Demand currentDemand = null;

        // find card in model
        for(Demand demand : demandList) {
            if(demand.getId().equals(demandId)) {
                currentDemand = demand;
                break;
            }
        }

        // set output
        textView.setText(currentDemand.toString());

        // TODO handle exception if demand not found

    }
}
