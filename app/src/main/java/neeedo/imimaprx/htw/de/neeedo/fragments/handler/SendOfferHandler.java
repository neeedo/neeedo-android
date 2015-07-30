package neeedo.imimaprx.htw.de.neeedo.fragments.handler;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.offer.Offer;
import neeedo.imimaprx.htw.de.neeedo.entities.user.User;
import neeedo.imimaprx.htw.de.neeedo.fragments.FormOfferFragment;
import neeedo.imimaprx.htw.de.neeedo.models.ApplicationContextModel;
import neeedo.imimaprx.htw.de.neeedo.models.OffersModel;
import neeedo.imimaprx.htw.de.neeedo.models.UserModel;
import neeedo.imimaprx.htw.de.neeedo.rest.offer.PostCreateUpdateOfferAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.util.BaseAsyncTask;

public class SendOfferHandler implements View.OnClickListener {
    private final FormOfferFragment formOfferFragment;
    private final BaseAsyncTask.SendMode sendMode;
    private OffersModel offersModel;

    public SendOfferHandler(BaseAsyncTask.SendMode sendMode, FormOfferFragment formOfferFragment) {
        this.sendMode = sendMode;
        this.formOfferFragment = formOfferFragment;
    }

    @Override
    public void onClick(View view) {
        view.requestFocusFromTouch(); // workaround for checking last field by focus change listener

        if (!formOfferFragment.checkValidation()) {
            return;
        }

        formOfferFragment.getBtnSubmit().setEnabled(false);

        offersModel = OffersModel.getInstance();
        User currentUser = UserModel.getInstance().getUser();
        Offer currentOffer = offersModel.getDraft();

        Offer offer = new Offer();
        offer.setTags(formOfferFragment.getOfferTags());
        offer.setLocation(formOfferFragment.getLocation());
        offer.setPrice(formOfferFragment.getPrice());
        offer.setImages(formOfferFragment.getImageNames());

        if (currentUser == null) {
            Context context = ApplicationContextModel.getInstance().getApplicationContext();
            Toast.makeText(context, context.getString(R.string.exception_message_login), Toast.LENGTH_LONG).show();
            return;
        }

        offer.setUserId(currentUser.getId());
        offer.setName(currentUser.getName());

        if (sendMode == BaseAsyncTask.SendMode.UPDATE && currentOffer != null) {
            offer.setId(currentOffer.getId());
            offer.setVersion(currentOffer.getVersion());
            offersModel.removeOfferByID(currentOffer.getId());
        }

        Log.d("OFFER", offer.toString());

        try {

            offersModel.setDraft(offer);
            BaseAsyncTask asyncTask = new PostCreateUpdateOfferAsyncTask(sendMode);
            offersModel.setNewCreatedState(true);
            asyncTask.execute();
        } catch (Exception e) {
            formOfferFragment.getBtnSubmit().setEnabled(true);
            e.printStackTrace();
        }
    }
}
