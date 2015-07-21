package neeedo.imimaprx.htw.de.neeedo.fragments.handler;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.demand.Demand;
import neeedo.imimaprx.htw.de.neeedo.entities.user.User;
import neeedo.imimaprx.htw.de.neeedo.fragments.FormDemandFragment;
import neeedo.imimaprx.htw.de.neeedo.models.ActiveUser;
import neeedo.imimaprx.htw.de.neeedo.models.DemandsModel;
import neeedo.imimaprx.htw.de.neeedo.models.UserModel;
import neeedo.imimaprx.htw.de.neeedo.rest.demand.PostCreateUpdateDemandAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.util.BaseAsyncTask;

public class SendDemandHandler implements View.OnClickListener {
    private final FormDemandFragment formDemandFragment;
    private final BaseAsyncTask.SendMode sendMode;

    public SendDemandHandler(BaseAsyncTask.SendMode sendMode, FormDemandFragment formDemandFragment) {
        this.sendMode = sendMode;
        this.formDemandFragment = formDemandFragment;
    }

    @Override
    public void onClick(View view) {
        view.requestFocusFromTouch(); // workaround for checking last field by focus change listener

        if(!formDemandFragment.checkValidation()) {
            // TODO show Toast error
            // TODO set errors for fields
            return;
        }

        User currentUser = UserModel.getInstance().getUser();
        Demand currentDemand = DemandsModel.getInstance().getDraft();

        Demand demand = new Demand();
        demand.setMustTags(formDemandFragment.getMustTags());
        demand.setShouldTags(formDemandFragment.getShouldTags());
        demand.setLocation(formDemandFragment.getLocation());
        demand.setDistance(formDemandFragment.getDistance());
        demand.setPrice(formDemandFragment.getPrice());

        if (currentUser == null) {
            Context context = ActiveUser.getInstance().getContext();
            Toast.makeText(context, context.getString(R.string.exception_message_login), Toast.LENGTH_LONG).show();
            return;
        }
        demand.setUserId(currentUser.getId());

        if (sendMode == BaseAsyncTask.SendMode.UPDATE && currentDemand != null) {
            demand.setId(currentDemand.getId());
            demand.setVersion(currentDemand.getVersion());
        }

        Log.d("DEMAND", demand.toString());

        try {
            DemandsModel.getInstance().setDraft(demand);
            BaseAsyncTask asyncTask = new PostCreateUpdateDemandAsyncTask(sendMode);
            asyncTask.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
