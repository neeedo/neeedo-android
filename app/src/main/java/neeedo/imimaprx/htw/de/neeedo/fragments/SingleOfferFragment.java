package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.Offer;
import neeedo.imimaprx.htw.de.neeedo.entities.Offers;
import neeedo.imimaprx.htw.de.neeedo.events.ServerResponseEvent;
import neeedo.imimaprx.htw.de.neeedo.models.OffersModel;
import neeedo.imimaprx.htw.de.neeedo.rest.offer.GetOffersAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.util.BaseAsyncTask;


public class SingleOfferFragment extends SuperFragment {

    TextView textView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.single_offer_view, container, false);

        textView = (TextView) view.findViewById(R.id.singleview);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        BaseAsyncTask asyncTask;

        asyncTask = new GetOffersAsyncTask(BaseAsyncTask.GetEntitiesMode.GET_RANDOM);
        asyncTask.execute();
    }

    @Subscribe
    public void fillText(ServerResponseEvent e) {

        String offerId = getArguments().getString("id");

        Offers offers = OffersModel.getInstance().getOffers();

        ArrayList<Offer> offerList = offers.getOffers();
        Offer currentOffer = null;

        for (Offer offer : offerList) {
            if (offer.getId().equals(offerId)) {
                currentOffer = offer;
                break;
            }
        }

        textView.setText(currentOffer.toString());

        // TODO handle exception if offer not found
    }
}
