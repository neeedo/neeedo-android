package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.Demand;
import neeedo.imimaprx.htw.de.neeedo.entities.Location;
import neeedo.imimaprx.htw.de.neeedo.entities.Price;
import neeedo.imimaprx.htw.de.neeedo.events.ServerResponseEvent;
import neeedo.imimaprx.htw.de.neeedo.helpers.LocationHelper;
import neeedo.imimaprx.htw.de.neeedo.models.ActiveUser;
import neeedo.imimaprx.htw.de.neeedo.models.DemandsModel;
import neeedo.imimaprx.htw.de.neeedo.models.UserModel;
import neeedo.imimaprx.htw.de.neeedo.rest.demand.PostCreateUpdateDemandAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.util.BaseAsyncTask;


public class NewDemandFragment extends FormDemandFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        if (locationAvailable) {
            etLocationLat.setText(String.valueOf(locationLatitude));
            etLocationLon.setText(String.valueOf(locationLongitude));
        }

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String etMustTagsText = etMustTags.getText().toString();
                String etShouldTagsText = etShouldTags.getText().toString();
                String etLocationLatText = etLocationLat.getText().toString();
                String etLocationLonText = etLocationLon.getText().toString();
                String etDistanceText = etDistance.getText().toString();
                String etPriceMinText = etPriceMin.getText().toString();
                String etPriceMaxText = etPriceMax.getText().toString();

                try {
                    ArrayList<String> mustTags = new ArrayList<String>(Arrays.asList(etMustTagsText.split(",")));
                    ArrayList<String> shouldTags = new ArrayList<String>(Arrays.asList(etShouldTagsText.split(",")));
                    Location location = new Location(Double.parseDouble(etLocationLatText), Double.parseDouble(etLocationLonText));
                    int distance = Integer.parseInt(etDistanceText);
                    Price price = new Price(Double.parseDouble(etPriceMinText), Double.parseDouble(etPriceMaxText));

                    Demand demand = new Demand();
                    demand.setMustTags(mustTags);
                    demand.setShouldTags(shouldTags);
                    demand.setLocation(location);
                    demand.setDistance(distance);
                    demand.setPrice(price);
                    demand.setUserId(UserModel.getInstance().getUser().getId());

                    System.out.println(demand);

                    DemandsModel.getInstance().setPostDemand(demand);
                    BaseAsyncTask asyncTask = new PostCreateUpdateDemandAsyncTask(BaseAsyncTask.SendMode.CREATE);
                    asyncTask.execute();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), getString(R.string.error_empty_or_wrong_format), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    @Subscribe
    public void handleServerResponse(ServerResponseEvent e) {
        redirectToFragment(ListDemandsFragment.class);
    }
}
