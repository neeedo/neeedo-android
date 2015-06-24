package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.squareup.otto.Subscribe;

import java.text.DecimalFormat;
import java.util.List;

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.offer.Offer;
import neeedo.imimaprx.htw.de.neeedo.entities.offer.Offers;
import neeedo.imimaprx.htw.de.neeedo.events.ServerResponseEvent;
import neeedo.imimaprx.htw.de.neeedo.models.OffersModel;
import neeedo.imimaprx.htw.de.neeedo.rest.offer.GetOffersAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.util.BaseAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.util.DeleteAsyncTask;


public class SingleOfferFragment extends SuperFragment implements View.OnClickListener {
    private Button btnDeleteOffer;
    private Button btnEditOffer;
    private TextView tvTags;
    private TextView tvPrice;
    private TextView tvUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.single_offer_view, container, false);

        tvTags = (TextView) view.findViewById(R.id.tvTags);
        tvPrice = (TextView) view.findViewById(R.id.tvPrice);
        tvUser = (TextView) view.findViewById(R.id.tvUser);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Activity activity = getActivity();
        btnDeleteOffer = (Button) activity.findViewById(R.id.btnDelete);
        btnEditOffer = (Button) activity.findViewById(R.id.btnEdit);

        btnDeleteOffer.setOnClickListener(this);
        btnEditOffer.setOnClickListener(this);

        BaseAsyncTask asyncTask;

        asyncTask = new GetOffersAsyncTask(BaseAsyncTask.GetEntitiesMode.GET_RANDOM);
        asyncTask.execute();
    }

    private Offer findSingleOffer(String offerId) {
        Offers offers = OffersModel.getInstance().getOffers();

        List<Offer> offerList = offers.getOffers();
        Offer currentOffer = null;

        for (Offer offer : offerList) {
            if (offer.getId().equals(offerId)) {
                currentOffer = offer;
                break;
            }
        }

        return currentOffer;
    }

    @Subscribe
    public void fillText(ServerResponseEvent e) {
        String offerId = getArguments().getString("id");
        Offer currentOffer = findSingleOffer(offerId);
        Context context = getActivity();

        DecimalFormat priceFormat = new DecimalFormat(context.getString(R.string.format_price));

        String tagsText = currentOffer.getTagsString();
        String priceText = context.getString(R.string.item_price) +
                ": " +
                priceFormat.format(currentOffer.getPrice()
                );
        String userText = currentOffer.getUser().getName();

        tvTags.setText(tagsText);
        tvPrice.setText(priceText);
        tvUser.setText(userText);

        // TODO handle exception if offer not found
    }

    @Override
    public void onClick(View view) {
        String offerId = getArguments().getString("id");
        Offer currentOffer = findSingleOffer(offerId);

        switch(view.getId()) {
            case R.id.btnDelete:
                OffersModel.getInstance().setDraft(currentOffer);
                BaseAsyncTask asyncTask = new DeleteAsyncTask(currentOffer);
                ConfirmDialogFragment confirmDialog = ConfirmDialogFragment.newInstance(
                        asyncTask,
                        ListOffersFragment.class,
                        getResources().getString(R.string.dialog_delete_offer)
                );
                confirmDialog.show(getFragmentManager(), "confirm");
                break;

            case R.id.btnEdit:
                // TODO
                Log.d("DEBUG", "Edit offer");
//                OffersModel.getInstance().setDraft(currentOffer);
//                redirectToFragment(EditOfferFragment.class);
                break;
        }
    }
}
