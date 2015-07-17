package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import java.text.DecimalFormat;
import java.util.List;

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.favorites.Favorite;
import neeedo.imimaprx.htw.de.neeedo.entities.message.Message;
import neeedo.imimaprx.htw.de.neeedo.entities.offer.Offer;
import neeedo.imimaprx.htw.de.neeedo.entities.offer.Offers;
import neeedo.imimaprx.htw.de.neeedo.entities.offer.SingleOffer;
import neeedo.imimaprx.htw.de.neeedo.events.DeleteFinishedEvent;
import neeedo.imimaprx.htw.de.neeedo.events.FavoritesActionEvent;
import neeedo.imimaprx.htw.de.neeedo.events.GetOfferFinishedEvent;
import neeedo.imimaprx.htw.de.neeedo.events.UserMessageSendEvent;
import neeedo.imimaprx.htw.de.neeedo.models.ActiveUser;
import neeedo.imimaprx.htw.de.neeedo.models.OffersModel;
import neeedo.imimaprx.htw.de.neeedo.models.UserModel;
import neeedo.imimaprx.htw.de.neeedo.rest.favorites.CreateFavoriteAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.message.PostMessageAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.offer.GetOfferByIDAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.offer.GetOffersAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.util.BaseAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.util.DeleteAsyncTask;


public class SingleOfferFragment extends SuperFragment implements View.OnClickListener {
    private Button btnDeleteOffer;
    private Button btnEditOffer;
    private Button btnMessage;
    private Button btnSend;
    private Button btnAddToFavorites;
    private Button btnRemoveFromFavorites;
    private TextView tvTags;
    private TextView tvPrice;
    private TextView tvUser;
    private EditText editTextSendMessage;
    private Offer currentOffer;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.single_offer_view, container, false);

        tvTags = (TextView) view.findViewById(R.id.tvTags);
        tvPrice = (TextView) view.findViewById(R.id.tvPrice);
        tvUser = (TextView) view.findViewById(R.id.tvUser);
        editTextSendMessage = (EditText) view.findViewById(R.id.getMessage);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Activity activity = getActivity();
        btnDeleteOffer = (Button) activity.findViewById(R.id.btnDelete);
        btnEditOffer = (Button) activity.findViewById(R.id.btnEdit);
        btnMessage = (Button) activity.findViewById(R.id.btnMessage);
        btnSend = (Button) activity.findViewById(R.id.btnSend);
        btnAddToFavorites = (Button) activity.findViewById(R.id.single_offer_view_add_to_favorites_button);
        btnRemoveFromFavorites = (Button) activity.findViewById(R.id.single_offer_view_remove_from_favorites_button);

        btnDeleteOffer.setOnClickListener(this);
        btnEditOffer.setOnClickListener(this);
        btnMessage.setOnClickListener(this);
        btnSend.setOnClickListener(this);
        btnAddToFavorites.setOnClickListener(this);
        btnRemoveFromFavorites.setOnClickListener(this);

        btnRemoveFromFavorites.setVisibility(View.GONE);
        btnAddToFavorites.setVisibility(View.GONE);
        btnMessage.setVisibility(View.GONE);
        btnDeleteOffer.setVisibility(View.GONE);
        btnEditOffer.setVisibility(View.GONE);

        fillText(null);

    }

    private Offer findSingleOffer(String offerId) {

        List<Offer> offerList = OffersModel.getInstance().getOffers();
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
    public void fillText(GetOfferFinishedEvent e) {
        String offerId = getArguments().getString("id");
        currentOffer = findSingleOffer(offerId);
        if (currentOffer == null) {
            Offer singleOffer = OffersModel.getInstance().getSingleOffer();
            if (singleOffer == null) {
                new GetOfferByIDAsyncTask(offerId).execute();
                return;
            } else {
                currentOffer = singleOffer;
                OffersModel.getInstance().setSingleOffer(null);
            }
        }
        setVisibility();
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

    }

    private void setVisibility() {

        boolean owerShip = UserModel.getInstance().checkIfEntityOwnerIsActiveUser(currentOffer);

        if (owerShip) {
            btnDeleteOffer.setVisibility(View.VISIBLE);
            btnEditOffer.setVisibility(View.VISIBLE);
        } else {
            btnRemoveFromFavorites.setVisibility(View.VISIBLE);
            btnAddToFavorites.setVisibility(View.VISIBLE);
            btnMessage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.btnDelete:
                OffersModel.getInstance().setDraft(currentOffer);
                if (currentOffer == null) {
                    return;
                }
                BaseAsyncTask asyncTask = new DeleteAsyncTask(currentOffer);
                ConfirmDialogFragment confirmDialog = ConfirmDialogFragment.newInstance(
                        asyncTask,
                        ListOffersFragment.class,
                        getResources().getString(R.string.dialog_delete_offer)
                );
                confirmDialog.show(getFragmentManager(), "confirm");
                break;

            case R.id.btnEdit:
                OffersModel.getInstance().setDraft(currentOffer);
                redirectToFragment(EditOfferFragment.class);
                break;
            case R.id.btnMessage: {
                btnMessage.setVisibility(View.GONE);
                btnSend.setVisibility(View.VISIBLE);
                editTextSendMessage.setVisibility(View.VISIBLE);
            }
            break;

            case R.id.btnSend: {
                String message = editTextSendMessage.getText().toString();
                if (message.isEmpty() || message.matches("[ ]+")) {
                    return;
                }
                Message m = new Message();
                m.setSenderId(UserModel.getInstance().getUser().getId());
                m.setRecipientId(currentOffer.getUser().getId());
                m.setBody(message);

                new PostMessageAsyncTask(m).execute();
                btnSend.setVisibility(View.GONE);
                btnMessage.setVisibility(View.VISIBLE);
                editTextSendMessage.setVisibility(View.GONE);

            }
            break;

            case R.id.single_offer_view_add_to_favorites_button: {
                new CreateFavoriteAsyncTask(getCurrentFavorite()).execute();
            }
            break;

            case R.id.single_offer_view_remove_from_favorites_button: {
                new DeleteAsyncTask(getCurrentFavorite()).execute();
            }
            break;
        }
    }

    private Favorite getCurrentFavorite() {
        Favorite favorite = new Favorite();
        favorite.setUserId(ActiveUser.getInstance().getUserId());
        favorite.setOfferId(getArguments().getString("id"));
        return favorite;
    }

    @Subscribe
    public void messageSend(UserMessageSendEvent userMessageSendEvent) {
        Toast.makeText(getActivity(), getActivity().getString(R.string.single_offer_fragment_toast_message), Toast.LENGTH_SHORT).show();
    }

    @Subscribe
    public void favoriteActionDone(FavoritesActionEvent e) {
        Toast.makeText(getActivity(), getActivity().getString(R.string.done), Toast.LENGTH_SHORT).show();
    }

    @Subscribe
    public void removeActionDone(DeleteFinishedEvent e) {
        if (e.isFinished())
            Toast.makeText(getActivity(), getActivity().getString(R.string.delete_success), Toast.LENGTH_SHORT).show();
        OffersModel offersModel = OffersModel.getInstance();
        for (Offer offer : offersModel.getOffers()) {
            if (offer.getId().equals(currentOffer.getId())) {
                offersModel.getOffers().remove(offer);
            }
        }
    }
}
