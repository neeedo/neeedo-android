package neeedo.imimaprx.htw.de.neeedo.fragments.handler;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.Location;
import neeedo.imimaprx.htw.de.neeedo.entities.Offer;
import neeedo.imimaprx.htw.de.neeedo.models.OffersModel;
import neeedo.imimaprx.htw.de.neeedo.models.UserModel;
import neeedo.imimaprx.htw.de.neeedo.rest.HttpPostOfferAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.SuperHttpAsyncTask;

public class SendNewOfferHandler implements View.OnClickListener {

    private final EditText etTags;
    private final EditText etLocationLat;
    private final EditText etLocationLon;
    private final EditText etPrice;


    public SendNewOfferHandler(EditText etTags, EditText etLocationLat, EditText etLocationLon, EditText etPrice) {
        this.etTags = etTags;
        this.etLocationLat = etLocationLat;
        this.etLocationLon = etLocationLon;
        this.etPrice = etPrice;
    }

    @Override
    public void onClick(View v) {
        String etTagsText = etTags.getText().toString();
        String etLocationLatText = etLocationLat.getText().toString();
        String etLocationLonText = etLocationLon.getText().toString();
        String etPriceText = etPrice.getText().toString();

        try {
            ArrayList<String> tags = new ArrayList<String>(Arrays.asList(etTagsText.split(",")));
            Location location = new Location(Double.parseDouble(etLocationLatText), Double.parseDouble(etLocationLonText));
            Double price = Double.parseDouble(etPriceText);

            Offer offer = new Offer();
            offer.setTags(tags);
            offer.setLocation(location);
            offer.setPrice(price);
            offer.setUserId(UserModel.getInstance().getUser().getId());

            System.out.println(offer);

            OffersModel.getInstance().setPostOffer(offer);
            SuperHttpAsyncTask asyncTask = new HttpPostOfferAsyncTask();
            asyncTask.execute();

        } catch (Exception e) {
        }
    }
}
