package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import neeedo.imimaprx.htw.de.neeedo.MainActivity;
import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.demand.Demand;
import neeedo.imimaprx.htw.de.neeedo.events.GetSuggestionEvent;
import neeedo.imimaprx.htw.de.neeedo.events.ServerResponseEvent;
import neeedo.imimaprx.htw.de.neeedo.fragments.handler.SendDemandHandler;
import neeedo.imimaprx.htw.de.neeedo.models.DemandsModel;
import neeedo.imimaprx.htw.de.neeedo.rest.util.BaseAsyncTask;


public class EditDemandFragment extends FormDemandFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        TextView header = (TextView) view.findViewById(R.id.tvHeader);
        header.setText(R.string.edit_card_demand);

        Demand currentDemand = DemandsModel.getInstance().getPostDemand();
        if(currentDemand != null) {
            etMustTags.setText(currentDemand.getMustTagsString());
            etShouldTags.setText(currentDemand.getShouldTagsString());
            etLocationLat.setText(String.valueOf(currentDemand.getLocation().getLat()));
            etLocationLon.setText(String.valueOf(currentDemand.getLocation().getLon()));
            etDistance.setText(String.valueOf(currentDemand.getDistance()));
            etPriceMin.setText(String.valueOf(currentDemand.getPrice().getMin()));
            etPriceMax.setText(String.valueOf(currentDemand.getPrice().getMax()));
        }

        btnSubmit.setOnClickListener(new SendDemandHandler(BaseAsyncTask.SendMode.UPDATE, this));

        return view;
    }

    @Subscribe
    public void handleServerResponse(ServerResponseEvent e) {
        // TODO redirect to detail view
        redirectToFragment(ListDemandsFragment.class, MainActivity.MENU_LIST_DEMANDS);
    }

    @Subscribe
    public void fillSuggestions(GetSuggestionEvent e) {
        super.fillSuggestions(e);
    }

}
