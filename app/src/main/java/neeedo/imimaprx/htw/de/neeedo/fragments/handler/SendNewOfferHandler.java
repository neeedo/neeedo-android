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

    private final NewOfferFragment newOfferFragment;

    public SendNewOfferHandler(NewOfferFragment newOfferFragment) {
        this.newOfferFragment = newOfferFragment;
    }

    @Override
    public void onClick(View v) {
        if (!newOfferFragment.validateData()) {
            return;
        }

        User currentUser = UserModel.getInstance().getUser();

        Offer offer = new Offer();
        offer.setTags(newOfferFragment.getOfferTags());
        offer.setLocation(newOfferFragment.getLocation());
        offer.setPrice(newOfferFragment.getPrice());
        offer.setImages(newOfferFragment.getImages());

        offer.setUserId(currentUser.getId());
        offer.setName(currentUser.getName());

        OffersModel.getInstance().setDraft(offer);
        BaseAsyncTask asyncTask = new PostCreateUpdateOfferAsyncTask(BaseAsyncTask.SendMode.CREATE);
        asyncTask.execute();
    }
}
