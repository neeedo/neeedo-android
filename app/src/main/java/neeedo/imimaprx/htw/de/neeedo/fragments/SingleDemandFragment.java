package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.util.List;

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.demand.Demand;
import neeedo.imimaprx.htw.de.neeedo.entities.demand.Demands;
import neeedo.imimaprx.htw.de.neeedo.entities.offer.Offer;
import neeedo.imimaprx.htw.de.neeedo.entities.offer.Offers;
import neeedo.imimaprx.htw.de.neeedo.events.ServerResponseEvent;
import neeedo.imimaprx.htw.de.neeedo.models.DemandsModel;
import neeedo.imimaprx.htw.de.neeedo.models.OffersModel;
import neeedo.imimaprx.htw.de.neeedo.rest.demand.GetDemandsAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.offer.GetOffersAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.util.BaseAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.util.DeleteAsyncTask;


public class SingleDemandFragment extends SuperFragment implements View.OnClickListener {

    TextView textView;
    Button btnDeleteDemand;
    Button btnEditDemand;
    ListView matchingView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.single_demand_view, container, false);

        textView = (TextView) view.findViewById(R.id.singleview);
        matchingView = (ListView) view.findViewById(R.id.matchingview);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Activity activity = getActivity();
        btnDeleteDemand = (Button) activity.findViewById(R.id.btnDelete);
        btnEditDemand = (Button) activity.findViewById(R.id.btnEdit);

        btnDeleteDemand.setOnClickListener(this);
        btnEditDemand.setOnClickListener(this);

        BaseAsyncTask asyncTask;

        asyncTask = new GetDemandsAsyncTask(BaseAsyncTask.GetEntitiesMode.GET_RANDOM);
        asyncTask.execute();

        // TODO launch matching task here
        new GetOffersAsyncTask(BaseAsyncTask.GetEntitiesMode.GET_RANDOM).execute();
    }

    private Demand findSingleDemand(String demandId) {
        Demands demands = DemandsModel.getInstance().getDemands();

        List<Demand> demandList = demands.getDemands();
        Demand currentDemand = null;

        for (Demand demand : demandList) {
            if (demand.getId().equals(demandId)) {
                currentDemand = demand;
                break;
            }
        }

        return currentDemand;
    }

    @Subscribe
    public void fillText(ServerResponseEvent e) {
        String demandId = getArguments().getString("id");
        Demand currentDemand = findSingleDemand(demandId);

        textView.setText(currentDemand.toString());

        // TODO handle exception if demand not found

        // TODO use matched offers
        Offers offers = OffersModel.getInstance().getOffers();
        List<Offer> offerList = offers.getOffers();

        ArrayAdapter<Offer> adapter = new ArrayAdapter<Offer>(getActivity(),
                android.R.layout.simple_list_item_1, offerList);
        matchingView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        String demandId = getArguments().getString("id");
        Demand currentDemand = findSingleDemand(demandId);

        switch(view.getId()) {
            case R.id.btnDelete:
                DemandsModel.getInstance().setPostDemand(currentDemand);
                BaseAsyncTask asyncTask = new DeleteAsyncTask(currentDemand);
                ConfirmDialogFragment confirmDialog = ConfirmDialogFragment.newInstance(
                        asyncTask,
                        ListDemandsFragment.class,
                        getResources().getString(R.string.dialog_delete_demand)
                );
                confirmDialog.show(getFragmentManager(), "confirm");
                break;

            case R.id.btnEdit:
                DemandsModel.getInstance().setPostDemand(currentDemand);
                redirectToFragment(EditDemandFragment.class);
                break;
        }

    }
}
