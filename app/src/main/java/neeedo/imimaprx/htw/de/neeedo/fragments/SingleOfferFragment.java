package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import org.apmem.tools.layouts.FlowLayout;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import neeedo.imimaprx.htw.de.neeedo.ImageActivity;
import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.favorites.Favorite;
import neeedo.imimaprx.htw.de.neeedo.entities.message.Message;
import neeedo.imimaprx.htw.de.neeedo.entities.offer.Offer;
import neeedo.imimaprx.htw.de.neeedo.events.DeleteFinishedEvent;
import neeedo.imimaprx.htw.de.neeedo.events.FavoritesActionEvent;
import neeedo.imimaprx.htw.de.neeedo.events.GetOfferFinishedEvent;
import neeedo.imimaprx.htw.de.neeedo.events.UserMessageSendEvent;
import neeedo.imimaprx.htw.de.neeedo.models.ActiveUser;
import neeedo.imimaprx.htw.de.neeedo.models.FavoritesModel;
import neeedo.imimaprx.htw.de.neeedo.models.OffersModel;
import neeedo.imimaprx.htw.de.neeedo.models.UserModel;
import neeedo.imimaprx.htw.de.neeedo.rest.favorites.CreateFavoriteAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.message.PostMessageAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.offer.GetOfferByIDAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.util.BaseAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.util.DeleteAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.utils.ServerConstantsUtils;


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
    private FlowLayout layoutImages;
    private Offer currentOffer;
    private String offerId = "";
    private ArrayList<String> images;
    private Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.single_offer_view, container, false);
        context = getActivity().getApplicationContext();

        tvTags = (TextView) view.findViewById(R.id.tvTags);
        tvPrice = (TextView) view.findViewById(R.id.tvPrice);
        tvUser = (TextView) view.findViewById(R.id.tvUser);
        editTextSendMessage = (EditText) view.findViewById(R.id.getMessage);
        layoutImages = (FlowLayout) view.findViewById(R.id.layoutImages);

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

        offerId = getArguments().getString("id");
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

        //try to find it in the list, if not available reload the offer and add it to the list
        currentOffer = findSingleOffer(offerId);
        if (currentOffer == null) {
            new GetOfferByIDAsyncTask(offerId).execute();
            return;
        }

        images = currentOffer.getImages();
        if (images != null && images.size() > 0) {

            for (int i = 0; i < images.size(); i++) {
                FlowLayout.LayoutParams imageLayoutParams = new FlowLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                imageLayoutParams.setMargins(0, 0, 15, 15);
                ImageView imageView = new ImageView(context);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(0, 0, 0, 20);
                imageView.setAdjustViewBounds(true);
                imageView.setLayoutParams(imageLayoutParams);
                imageView.setMinimumHeight(300);
                imageView.setMinimumWidth(300);
                imageView.setMaxHeight(300);
                imageView.setMaxWidth(300);

                String imageUrl = ServerConstantsUtils.getActiveServer() + "images/" + images.get(i);
                Picasso.with(context).load(imageUrl).fit().centerInside().into(imageView);
                layoutImages.addView(imageView);

                final int position = i;

                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent imageActivityIntent = new Intent(context, ImageActivity.class);
                        imageActivityIntent.putExtra(ImageActivity.IMAGES_LIST_EXTRA, images);
                        imageActivityIntent.putExtra(ImageActivity.IMAGES_LIST_POSITION_EXTRA, position);
                        startActivity(imageActivityIntent);
                    }
                });
            }
        }

        setVisibility();
        Context context = getActivity();

        DecimalFormat priceFormat = new DecimalFormat(context.getString(R.string.format_price));

        String tagsText = currentOffer.getTagsString();
        String priceText = priceFormat.format(currentOffer.getPrice());
        String userText = currentOffer.getUser().getName();

        tvTags.setText(tagsText);
        tvPrice.setText(priceText);
        tvUser.setText(userText);

    }

    private void setVisibility() {

        if (ActiveUser.getInstance().userCredentialsAvailable()) {
            boolean isOwn = UserModel.getInstance().checkIfEntityOwnerIsActiveUser(currentOffer);

            boolean isFavorite = false;
            ArrayList<Favorite> favorites = FavoritesModel.getInstance().getFavorites();
            for(Favorite favorite : favorites) {
                if(currentOffer.getId().equals(favorite.getId())) {
                    isFavorite = true;
                    break;
                }
            }

            if (isOwn) {
                btnDeleteOffer.setVisibility(View.VISIBLE);
                btnEditOffer.setVisibility(View.VISIBLE);
            } else {
                if(isFavorite) {
                    btnAddToFavorites.setVisibility(View.GONE);
                    btnRemoveFromFavorites.setVisibility(View.VISIBLE);
                } else {
                    btnAddToFavorites.setVisibility(View.VISIBLE);
                    btnRemoveFromFavorites.setVisibility(View.GONE);
                }
                btnMessage.setVisibility(View.VISIBLE);
            }
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
                        null,
                        getResources().getString(R.string.dialog_delete_offer)
                );
                confirmDialog.show(getFragmentManager(), getString(R.string.confirm));
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
    public void offerDeleted(DeleteFinishedEvent e) {
        redirectToFragment(ListOffersFragment.class);
    }


}
