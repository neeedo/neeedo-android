package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.text.DecimalFormatSymbols;

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

        Demand currentDemand = DemandsModel.getInstance().getDraft();
        if (currentDemand != null) {
            etMustTags.setText(currentDemand.getMustTagsString());
            etShouldTags.setText(currentDemand.getShouldTagsString());
            etPriceMin.setText(String.format("%.2f", currentDemand.getPrice().getMin()));
            etPriceMax.setText(String.format("%.2f", currentDemand.getPrice().getMax()));
        }

        btnSubmit.setOnClickListener(new SendDemandHandler(BaseAsyncTask.SendMode.UPDATE, this));

        return view;
    }

    @Subscribe
    public void handleServerResponse(ServerResponseEvent e) {
        // TODO redirect to detail view
        redirectToFragment(ListDemandsFragment.class);
    }

    @Subscribe
    public void fillSuggestions(GetSuggestionEvent e) {
        super.fillSuggestions(e);
    }

}
