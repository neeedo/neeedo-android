package neeedo.imimaprx.htw.de.neeedo.fragments.handler;

import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Arrays;

import neeedo.imimaprx.htw.de.neeedo.entities.demand.Demand;
import neeedo.imimaprx.htw.de.neeedo.entities.user.User;
import neeedo.imimaprx.htw.de.neeedo.entities.util.Location;
import neeedo.imimaprx.htw.de.neeedo.entities.util.Price;
import neeedo.imimaprx.htw.de.neeedo.models.DemandsModel;
import neeedo.imimaprx.htw.de.neeedo.models.UserModel;
import neeedo.imimaprx.htw.de.neeedo.rest.demand.PostCreateUpdateDemandAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.util.BaseAsyncTask;

public class SendDemandHandler implements View.OnClickListener {
    private final EditText etMustTags;
    private final EditText etShouldTags;
    private final EditText etLocationLat;
    private final EditText etLocationLon;
    private final EditText etDistance;
    private final EditText etPriceMin;
    private final EditText etPriceMax;
    private final BaseAsyncTask.SendMode sendMode;

    public SendDemandHandler(BaseAsyncTask.SendMode sendMode, EditText etMustTags, EditText etShouldTags, EditText etLocationLat, EditText etLocationLon, EditText etDistance, EditText etPriceMin, EditText etPriceMax) {
        this.sendMode = sendMode;
        this.etMustTags = etMustTags;
        this.etShouldTags = etShouldTags;
        this.etLocationLat = etLocationLat;
        this.etLocationLon = etLocationLon;
        this.etDistance = etDistance;
        this.etPriceMin = etPriceMin;
        this.etPriceMax = etPriceMax;
    }

    @Override
    public void onClick(View view) {
        String etMustTagsText = etMustTags.getText().toString();
        String etShouldTagsText = etShouldTags.getText().toString();
        String etLocationLatText = etLocationLat.getText().toString();
        String etLocationLonText = etLocationLon.getText().toString();
        String etDistanceText = etDistance.getText().toString();
        String etPriceMinText = etPriceMin.getText().toString();
        String etPriceMaxText = etPriceMax.getText().toString();
        ArrayList<String> mustTags = new ArrayList<String>(Arrays.asList(etMustTagsText.split(",")));
        ArrayList<String> shouldTags = new ArrayList<String>(Arrays.asList(etShouldTagsText.split(",")));
        Location location = new Location(Double.parseDouble(etLocationLatText), Double.parseDouble(etLocationLonText));
        int distance = Integer.parseInt(etDistanceText);
        Price price = new Price(Long.parseLong(etPriceMinText), Long.parseLong(etPriceMaxText));

        User currentUser = UserModel.getInstance().getUser();
        Demand currentDemand = DemandsModel.getInstance().getPostDemand();

        Demand demand = new Demand();
        demand.setMustTags(mustTags);
        demand.setShouldTags(shouldTags);
        demand.setLocation(location);
        demand.setDistance(distance);
        demand.setPrice(price);
        demand.setUserId(currentUser.getId());

        if (sendMode == BaseAsyncTask.SendMode.UPDATE && currentDemand != null) {
            demand.setId(currentDemand.getId());
            demand.setVersion(currentDemand.getVersion());
        }

        Log.d("DEMAND", demand.toString());

        try {
            DemandsModel.getInstance().setPostDemand(demand);
            BaseAsyncTask asyncTask = new PostCreateUpdateDemandAsyncTask(sendMode);
            asyncTask.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
