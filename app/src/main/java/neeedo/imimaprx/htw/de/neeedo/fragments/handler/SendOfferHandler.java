package neeedo.imimaprx.htw.de.neeedo.fragments.handler;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.offer.Offer;
import neeedo.imimaprx.htw.de.neeedo.entities.user.User;
import neeedo.imimaprx.htw.de.neeedo.fragments.FormOfferFragment;
import neeedo.imimaprx.htw.de.neeedo.models.ActiveUser;
import neeedo.imimaprx.htw.de.neeedo.models.OffersModel;
import neeedo.imimaprx.htw.de.neeedo.models.UserModel;
import neeedo.imimaprx.htw.de.neeedo.rest.offer.PostCreateUpdateOfferAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.util.BaseAsyncTask;

public class SendOfferHandler implements View.OnClickListener {
    private final FormOfferFragment formOfferFragment;
    private final BaseAsyncTask.SendMode sendMode;

    public SendOfferHandler(BaseAsyncTask.SendMode sendMode, FormOfferFragment formOfferFragment) {
        this.sendMode = sendMode;
        this.formOfferFragment = formOfferFragment;
    }

    @Override
    public void onClick(View v) {
        if (!formOfferFragment.validateData()) {
            return;
        }

        formOfferFragment.showProgressDialog();

        User currentUser = UserModel.getInstance().getUser();
        Offer currentOffer = OffersModel.getInstance().getDraft();

        Offer offer = new Offer();
        offer.setTags(formOfferFragment.getOfferTags());
        offer.setLocation(formOfferFragment.getLocation());
        offer.setPrice(formOfferFragment.getPrice());
        offer.setImages(formOfferFragment.getImages());

        if (currentUser == null) {
            Context context = ActiveUser.getInstance().getContext();
            Toast.makeText(context, context.getString(R.string.exception_message_login), Toast.LENGTH_LONG).show();
            return;
        }
        offer.setUserId(currentUser.getId());
        offer.setName(currentUser.getName());

        if (sendMode == BaseAsyncTask.SendMode.UPDATE && currentOffer != null) {
            offer.setId(currentOffer.getId());
            offer.setVersion(currentOffer.getVersion());
        }

        Log.d("OFFER", offer.toString());

        try {
            OffersModel.getInstance().setDraft(offer);
            BaseAsyncTask asyncTask = new PostCreateUpdateOfferAsyncTask(sendMode);
            asyncTask.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
