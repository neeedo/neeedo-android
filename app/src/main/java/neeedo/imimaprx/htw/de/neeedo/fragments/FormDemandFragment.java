package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Arrays;

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.Demand;
import neeedo.imimaprx.htw.de.neeedo.entities.Location;
import neeedo.imimaprx.htw.de.neeedo.entities.Price;
import neeedo.imimaprx.htw.de.neeedo.helpers.LocationHelper;
import neeedo.imimaprx.htw.de.neeedo.models.ActiveUser;
import neeedo.imimaprx.htw.de.neeedo.models.UserModel;

public class FormDemandFragment extends SuperFragment implements View.OnClickListener {
    protected final ActiveUser activeUser = ActiveUser.getInstance();

    protected EditText etMustTags;
    protected EditText etShouldTags;
    protected EditText etLocationLat;
    protected EditText etLocationLon;
    protected EditText etDistance;
    protected EditText etPriceMin;
    protected EditText etPriceMax;
    protected Button btnSubmit;

    protected double locationLatitude;
    protected double locationLongitude;
    protected boolean locationAvailable;
    protected LocationHelper locationHelper;
    protected Location currentLocation;

    protected String etMustTagsText;
    protected String etShouldTagsText;
    protected String etLocationLatText;
    protected String etLocationLonText;
    protected String etDistanceText;
    protected String etPriceMinText;
    protected String etPriceMaxText;
    protected ArrayList<String> mustTags;
    protected ArrayList<String> shouldTags;
    protected Location location;
    protected int distance;
    protected Price price;

    protected Demand postDemand;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locationHelper = new LocationHelper(getActivity());
        currentLocation = locationHelper.getLocation();
        locationLatitude = currentLocation.getLat();
        locationLongitude = currentLocation.getLon();
        locationAvailable = locationHelper.isLocationAvailable();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.form_demand_view, container, false);

        etMustTags = (EditText) view.findViewById(R.id.etMustTags);
        etShouldTags = (EditText) view.findViewById(R.id.etShouldTags);
        etLocationLat = (EditText) view.findViewById(R.id.etLocationLat);
        etLocationLon = (EditText) view.findViewById(R.id.etLocationLon);
        etDistance = (EditText) view.findViewById(R.id.etDistance);
        etPriceMin = (EditText) view.findViewById(R.id.etPriceMin);
        etPriceMax = (EditText) view.findViewById(R.id.etPriceMax);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);

        return view;
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.btnSubmit:
                etMustTagsText = etMustTags.getText().toString();
                etShouldTagsText = etShouldTags.getText().toString();
                etLocationLatText = etLocationLat.getText().toString();
                etLocationLonText = etLocationLon.getText().toString();
                etDistanceText = etDistance.getText().toString();
                etPriceMinText = etPriceMin.getText().toString();
                etPriceMaxText = etPriceMax.getText().toString();
                mustTags = new ArrayList<String>(Arrays.asList(etMustTagsText.split(",")));
                shouldTags = new ArrayList<String>(Arrays.asList(etShouldTagsText.split(",")));
                location = new Location(Double.parseDouble(etLocationLatText), Double.parseDouble(etLocationLonText));
                distance = Integer.parseInt(etDistanceText);
                price = new Price(Double.parseDouble(etPriceMinText), Double.parseDouble(etPriceMaxText));

                postDemand = new Demand();
                postDemand.setMustTags(mustTags);
                postDemand.setShouldTags(shouldTags);
                postDemand.setLocation(location);
                postDemand.setDistance(distance);
                postDemand.setPrice(price);
                postDemand.setUserId(UserModel.getInstance().getUser().getId());

                break;
        }
    }
}
