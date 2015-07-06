package neeedo.imimaprx.htw.de.neeedo.fragments.handler;

import android.view.View;
import android.widget.EditText;

import org.osmdroid.util.GeoPoint;

import java.util.ArrayList;
import java.util.Arrays;

import neeedo.imimaprx.htw.de.neeedo.entities.offer.Offer;
import neeedo.imimaprx.htw.de.neeedo.entities.user.User;
import neeedo.imimaprx.htw.de.neeedo.entities.util.Location;
import neeedo.imimaprx.htw.de.neeedo.fragments.NewOfferFragment;
import neeedo.imimaprx.htw.de.neeedo.models.OffersModel;
import neeedo.imimaprx.htw.de.neeedo.models.UserModel;
import neeedo.imimaprx.htw.de.neeedo.rest.offer.PostCreateUpdateOfferAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.util.BaseAsyncTask;

public class SendNewOfferHandler implements View.OnClickListener {
//
//    private final EditText etTags;
//    private final String latitude;
//    private final String longitude;
//    private final EditText etPrice;


    public SendNewOfferHandler(EditText etTags, GeoPoint selectedGeoPoint, EditText etPrice) {
//        this.etTags = etTags;
//        this.latitude = String.valueOf(selectedGeoPoint.getLatitude());
//        this.longitude = String.valueOf(selectedGeoPoint.getLongitude());
//        this.etPrice = etPrice;
    }

    public SendNewOfferHandler(NewOfferFragment newOfferFragment) {
    }

    @Override
    public void onClick(View v) {
//        String etTagsText = etTags.getText().toString();
//        String etLocationLatText = latitude;
//        String etLocationLonText = longitude;
//        String etPriceText = etPrice.getText().toString();
//
//        ArrayList<String> tags = new ArrayList<String>(Arrays.asList(etTagsText.split(",")));
//        Location location = new Location(Double.parseDouble(etLocationLatText), Double.parseDouble(etLocationLonText));
//        Double price = Double.parseDouble(etPriceText);
//
//        User currentUser = UserModel.getInstance().getUser();
//
//        Offer offer = new Offer();
//        offer.setTags(tags);
//        offer.setLocation(location);
//        offer.setPrice(price);
//        offer.setUserId(currentUser.getId());
//        offer.setName(currentUser.getName());
//
//        OffersModel.getInstance().setDraft(offer);
//        BaseAsyncTask asyncTask = new PostCreateUpdateOfferAsyncTask(BaseAsyncTask.SendMode.CREATE);
//        asyncTask.execute();
    }
}
