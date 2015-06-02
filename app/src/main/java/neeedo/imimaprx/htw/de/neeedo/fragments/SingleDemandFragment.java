package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.util.List;

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.Demand;
import neeedo.imimaprx.htw.de.neeedo.entities.Demands;
import neeedo.imimaprx.htw.de.neeedo.events.ServerResponseEvent;
import neeedo.imimaprx.htw.de.neeedo.models.DemandsModel;
import neeedo.imimaprx.htw.de.neeedo.rest.BaseAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.demand.GetDemandsAsyncTask;


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

        BaseAsyncTask asyncTask;

        asyncTask = new GetDemandsAsyncTask(BaseAsyncTask.GetEntitiesMode.GET_RANDOM);
        asyncTask.execute();
    }

    @Subscribe
    public void fillText(ServerResponseEvent e) {

        String demandId = getArguments().getString("id");

        Demands demands = DemandsModel.getInstance().getDemands();

        List<Demand> demandList = demands.getDemands();
        Demand currentDemand = null;

        for (Demand demand : demandList) {
            if (demand.getId().equals(demandId)) {
                currentDemand = demand;
                break;
            }
        }

        textView.setText(currentDemand.toString());

        // TODO handle exception if demand not found
    }
}
