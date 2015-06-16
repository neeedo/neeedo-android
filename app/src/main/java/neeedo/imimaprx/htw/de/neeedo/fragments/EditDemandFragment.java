package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.Demand;
import neeedo.imimaprx.htw.de.neeedo.entities.Location;
import neeedo.imimaprx.htw.de.neeedo.entities.Price;
import neeedo.imimaprx.htw.de.neeedo.events.ServerResponseEvent;
import neeedo.imimaprx.htw.de.neeedo.helpers.LocationHelper;
import neeedo.imimaprx.htw.de.neeedo.models.DemandsModel;
import neeedo.imimaprx.htw.de.neeedo.models.UserModel;
import neeedo.imimaprx.htw.de.neeedo.rest.demand.PostCreateUpdateDemandAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.util.BaseAsyncTask;


public class EditDemandFragment extends FormDemandFragment {
    private final Demand currentDemand = DemandsModel.getInstance().getPostDemand();;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        TextView header = (TextView) view.findViewById(R.id.tvHeader);
        header.setText(R.string.edit_card_demand);

        if(currentDemand != null) {
            etMustTags.setText(currentDemand.getMustTagsString());
            etShouldTags.setText(currentDemand.getShouldTagsString());
            etLocationLat.setText(String.valueOf(currentDemand.getLocation().getLat()));
            etLocationLon.setText(String.valueOf(currentDemand.getLocation().getLon()));
            etDistance.setText(String.valueOf(currentDemand.getDistance()));
            etPriceMin.setText(String.valueOf(currentDemand.getPrice().getMin()));
            etPriceMax.setText(String.valueOf(currentDemand.getPrice().getMax()));
        }

        btnSubmit.setOnClickListener(this);

        return view;
    }

    @Subscribe
    public void handleServerResponse(ServerResponseEvent e) {
        redirectToFragment(ListDemandsFragment.class);
    }

    @Override
    public void onClick(View view) {
        super.onClick(view);

        switch(view.getId()) {
            case R.id.btnSubmit:

                try {

                    Demand demand = new Demand();
                    if(currentDemand != null) {
                        demand.setId(currentDemand.getId());
                        demand.setVersion(currentDemand.getVersion());
                    }
                    demand.setMustTags(mustTags);
                    demand.setShouldTags(shouldTags);
                    demand.setLocation(location);
                    demand.setDistance(distance);
                    demand.setPrice(price);
                    demand.setUserId(UserModel.getInstance().getUser().getId());

                    Log.d("DEMAND", demand.toString());

                    DemandsModel.getInstance().setPostDemand(demand);
                    BaseAsyncTask asyncTask = new PostCreateUpdateDemandAsyncTask(BaseAsyncTask.SendMode.UPDATE);
                    asyncTask.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), getString(R.string.error_empty_or_wrong_format), Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
}
