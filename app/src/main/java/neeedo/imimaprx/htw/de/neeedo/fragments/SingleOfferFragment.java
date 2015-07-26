package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
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
import neeedo.imimaprx.htw.de.neeedo.entities.offer.Offer;
import neeedo.imimaprx.htw.de.neeedo.events.DeleteFinishedEvent;
import neeedo.imimaprx.htw.de.neeedo.events.FavoritesActionEvent;
import neeedo.imimaprx.htw.de.neeedo.events.GetOfferFinishedEvent;
import neeedo.imimaprx.htw.de.neeedo.models.ActiveUser;
import neeedo.imimaprx.htw.de.neeedo.models.FavoritesModel;
import neeedo.imimaprx.htw.de.neeedo.models.OffersModel;
import neeedo.imimaprx.htw.de.neeedo.models.UserModel;
import neeedo.imimaprx.htw.de.neeedo.rest.favorites.PostCreateFavoriteAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.offer.GetOfferByIDAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.util.BaseAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.util.DeleteAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.utils.ServerConstantsUtils;


public class SingleOfferFragment extends SuperFragment implements View.OnClickListener {
    private ImageButton btnDeleteOffer;
    private ImageButton btnEditOffer;
    private Button btnMessage;
    private ImageButton btnAddToFavorites;
    private ImageButton btnRemoveFromFavorites;
    private TextView tvTags;
    private TextView tvPrice;
    private TextView tvUser;
    private FlowLayout layoutImages;
    private HorizontalScrollView viewImages;
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
        layoutImages = (FlowLayout) view.findViewById(R.id.layoutImages);
        viewImages = (HorizontalScrollView) view.findViewById(R.id.viewImages);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Activity activity = getActivity();
        btnDeleteOffer = (ImageButton) activity.findViewById(R.id.btnDelete);
        btnEditOffer = (ImageButton) activity.findViewById(R.id.btnEdit);
        btnMessage = (Button) activity.findViewById(R.id.btnMessage);
        btnAddToFavorites = (ImageButton) activity.findViewById(R.id.single_offer_view_add_to_favorites_button);
        btnRemoveFromFavorites = (ImageButton) activity.findViewById(R.id.single_offer_view_remove_from_favorites_button);

        btnDeleteOffer.setOnClickListener(this);
        btnEditOffer.setOnClickListener(this);
        btnMessage.setOnClickListener(this);
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

            if(currentOffer.getImages().isEmpty()) {
                viewImages.setVisibility(View.GONE);
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
            case R.id.btnMessage:
                FragmentManager fragmentManager = getFragmentManager();
                Fragment fragment = new MessagesFragment();

                Bundle args = new Bundle();
                args.putString(MessagesFragment.FRAGMENT_ARGS_USER2ID, currentOffer.getUser().getId());
                args.putString(MessagesFragment.FRAGMENT_ARGS_USER1ID, ActiveUser.getInstance().getUserId());
                args.putString(MessagesFragment.FRAGMENT_ARGS_USERNAME, currentOffer.getUser().getName());
                fragment.setArguments(args);

                fragmentManager.beginTransaction()
                        .addToBackStack(null)
                        .replace(R.id.container, fragment)
                        .commit();
                break;

            case R.id.single_offer_view_add_to_favorites_button:
                new PostCreateFavoriteAsyncTask(getCurrentFavorite()).execute();
                break;

            case R.id.single_offer_view_remove_from_favorites_button:
                BaseAsyncTask removeFavoriteTask = new DeleteAsyncTask(getCurrentFavorite());
                ConfirmDialogFragment confirmRemoveFavorite = ConfirmDialogFragment.newInstance(
                        removeFavoriteTask,
                        null,
                        getResources().getString(R.string.dialog_remove_favorite)
                );
                confirmRemoveFavorite.show(getFragmentManager(), getString(R.string.confirm));

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
    public void favoriteActionDone(FavoritesActionEvent e) {
        Toast.makeText(getActivity(), getActivity().getString(R.string.done), Toast.LENGTH_SHORT).show();

        // TODO add a field to the event which describes if it removed or added the favorite

        if(btnAddToFavorites.getVisibility() == View.VISIBLE) {
            btnAddToFavorites.setVisibility(View.GONE);
        } else {
            btnAddToFavorites.setVisibility(View.VISIBLE);
        }

        if(btnRemoveFromFavorites.getVisibility() == View.VISIBLE) {
            btnRemoveFromFavorites.setVisibility(View.GONE);
        } else {
            btnRemoveFromFavorites.setVisibility(View.VISIBLE);
        }
    }

    @Subscribe
    public void offerDeleted(DeleteFinishedEvent e) {
        redirectToFragment(ListOffersFragment.class);
    }


}
