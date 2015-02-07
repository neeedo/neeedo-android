package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Arrays;

import neeedo.imimaprx.htw.de.neeedo.entities.Demand;

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.Location;
import neeedo.imimaprx.htw.de.neeedo.entities.Price;
import neeedo.imimaprx.htw.de.neeedo.models.DemandsModel;
import neeedo.imimaprx.htw.de.neeedo.rest.HttpPostAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.SuperHttpAsyncTask;

public class NewDemandCardFragment extends SuperFragment {

    // fields
    private EditText etMustTags;
    private EditText etShouldTags;
    private EditText etLocationLat;
    private EditText etLocationLon;
    private EditText etDistance;
    private EditText etPriceMin;
    private EditText etPriceMax;
    private Button btnSubmit;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.new_demand_card_form, container, false);

        // initalize fields
        etMustTags = (EditText) view.findViewById(R.id.etMustTags);
        etShouldTags = (EditText) view.findViewById(R.id.etShouldTags);
        etLocationLat = (EditText) view.findViewById(R.id.etLocationLat);
        etLocationLon = (EditText) view.findViewById(R.id.etLocationLon);
        etDistance = (EditText) view.findViewById(R.id.etDistance);
        etPriceMin = (EditText) view.findViewById(R.id.etPriceMin);
        etPriceMax = (EditText) view.findViewById(R.id.etPriceMax);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get form values
                String etMustTagsText = etMustTags.getText().toString();
                String etShouldTagsText = etShouldTags.getText().toString();
                String etLocationLatText = etLocationLat.getText().toString();
                String etLocationLonText = etLocationLon.getText().toString();
                String etDistanceText = etDistance.getText().toString();
                String etPriceMinText = etPriceMin.getText().toString();
                String etPriceMaxText = etPriceMax.getText().toString();

                // TODO handle wrong formats and exceptions

                // convert fields
                ArrayList<String> mustTags = new ArrayList<String>(Arrays.asList(etMustTagsText.split(",")));
                ArrayList<String> shouldTags = new ArrayList<String>(Arrays.asList(etShouldTagsText.split(",")));
                Location location = new Location(Double.parseDouble(etLocationLatText), Double.parseDouble(etLocationLonText));
                int distance = Integer.parseInt(etDistanceText);
                Price price = new Price(Double.parseDouble(etPriceMinText), Double.parseDouble(etPriceMaxText));

                // create new demand
                Demand demand = new Demand();
                demand.setMustTags(mustTags);
                demand.setShouldTags(shouldTags);
                demand.setLocation(location);
                demand.setDistance(distance);
                demand.setPrice(price);
                demand.setUserId("1"); // TODO use user id if implemented

                System.out.println(demand);

                // TODO send data

                DemandsModel.getInstance().setPostDemand(demand);

                SuperHttpAsyncTask asyncTask = new HttpPostAsyncTask();
                asyncTask.execute();
            }
        });

        return view;
    }
}
