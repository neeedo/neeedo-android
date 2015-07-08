package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import org.osmdroid.util.GeoPoint;

import neeedo.imimaprx.htw.de.neeedo.MainActivity;
import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.offer.Offer;
import neeedo.imimaprx.htw.de.neeedo.entities.util.Location;
import neeedo.imimaprx.htw.de.neeedo.events.GetSuggestionEvent;
import neeedo.imimaprx.htw.de.neeedo.events.NewEanTagsReceivedEvent;
import neeedo.imimaprx.htw.de.neeedo.events.NewImageReceivedFromServer;
import neeedo.imimaprx.htw.de.neeedo.events.ServerResponseEvent;
import neeedo.imimaprx.htw.de.neeedo.fragments.handler.SendOfferHandler;
import neeedo.imimaprx.htw.de.neeedo.models.OffersModel;
import neeedo.imimaprx.htw.de.neeedo.rest.util.BaseAsyncTask;

public class EditOfferFragment extends FormOfferFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        TextView header = (TextView) view.findViewById(R.id.tvHeader);
        header.setText(R.string.edit_card_offer);

        Offer currentOffer = OffersModel.getInstance().getDraft();
        if(currentOffer != null) {
            etTags.setText(currentOffer.getTagsString());
            etPrice.setText(String.valueOf(currentOffer.getPrice()));

            // TODO add images

            Location location = currentOffer.getLocation();
            setLocation(new GeoPoint(location.getLat(), location.getLon()));
        }

        btnSubmit.setOnClickListener(new SendOfferHandler(BaseAsyncTask.SendMode.UPDATE, this));

        return view;
    }

    @Subscribe
    public void handleNewEanTagsReceived(NewEanTagsReceivedEvent event) {
        super.handleNewEanTagsReceived(event);
    }

    @Subscribe
    public void handleNewImageReceivedFromServer(NewImageReceivedFromServer event) {
        super.handleNewImageReceivedFromServer(event);
    }

    @Subscribe
    public void handleServerResponse(ServerResponseEvent e) {
        // TODO redirect to detail view
        redirectToFragment(ListOffersFragment.class, MainActivity.MENU_LIST_OFFERS);
    }

    @Subscribe
    public void fillSuggestions(GetSuggestionEvent e) {
        super.fillSuggestions(e);
    }
}
