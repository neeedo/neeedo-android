package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;

import org.apmem.tools.layouts.FlowLayout;

import java.util.ArrayList;
import java.util.Arrays;

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.util.Location;
import neeedo.imimaprx.htw.de.neeedo.entities.util.Price;
import neeedo.imimaprx.htw.de.neeedo.events.GetSuggestionEvent;
import neeedo.imimaprx.htw.de.neeedo.helpers.LocationHelper;
import neeedo.imimaprx.htw.de.neeedo.models.ActiveUser;
import neeedo.imimaprx.htw.de.neeedo.rest.util.BaseAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.utils.AutocompletionOnClickListener;
import neeedo.imimaprx.htw.de.neeedo.utils.AutocompletionOnFocusChangeListener;
import neeedo.imimaprx.htw.de.neeedo.utils.AutocompletionTextWatcher;

public class FormDemandFragment extends FormFragment {
    protected MultiAutoCompleteTextView etMustTags;
    protected FlowLayout flMustTagSuggestions;
    protected MultiAutoCompleteTextView etShouldTags;
    protected FlowLayout flShouldTagSuggestions;
    protected EditText etLocationLat;
    protected EditText etLocationLon;
    protected EditText etDistance;
    protected EditText etPriceMin;
    protected EditText etPriceMax;
    protected Button btnSubmit;

    // TODO replace with mechanism of Offer Form
    protected double locationLatitude;
    protected double locationLongitude;
    protected boolean locationAvailable;
    protected LocationHelper locationHelper;
    protected Location currentLocation;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // TODO remove if added map location stuff
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

        etMustTags = (MultiAutoCompleteTextView) view.findViewById(R.id.etMustTags);
        flMustTagSuggestions = (FlowLayout) view.findViewById(R.id.flMustTagSuggestions);
        etShouldTags = (MultiAutoCompleteTextView) view.findViewById(R.id.etShouldTags);
        flShouldTagSuggestions = (FlowLayout) view.findViewById(R.id.flShouldTagSuggestions);
        etLocationLat = (EditText) view.findViewById(R.id.etLocationLat);
        etLocationLon = (EditText) view.findViewById(R.id.etLocationLon);
        etDistance = (EditText) view.findViewById(R.id.etDistance);
        etPriceMin = (EditText) view.findViewById(R.id.etPriceMin);
        etPriceMax = (EditText) view.findViewById(R.id.etPriceMax);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);

        etMustTags.addTextChangedListener(new AutocompletionTextWatcher(this, etMustTags, BaseAsyncTask.CompletionType.TAG));
        etShouldTags.addTextChangedListener(new AutocompletionTextWatcher(this, etShouldTags, BaseAsyncTask.CompletionType.TAG));

        etMustTags.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        etShouldTags.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        etMustTags.setOnClickListener(new AutocompletionOnClickListener(etMustTags));
        etShouldTags.setOnClickListener(new AutocompletionOnClickListener(etShouldTags));

        etMustTags.addTextChangedListener(new AutocompletionTextWatcher(this, etMustTags, BaseAsyncTask.CompletionType.PHRASE));
        etShouldTags.addTextChangedListener(new AutocompletionTextWatcher(this, etShouldTags, BaseAsyncTask.CompletionType.PHRASE));

        etMustTags.setOnFocusChangeListener(new AutocompletionOnFocusChangeListener(flMustTagSuggestions));
        etShouldTags.setOnFocusChangeListener(new AutocompletionOnFocusChangeListener(flShouldTagSuggestions));

        return view;
    }

    @Override
    public void fillSuggestions(GetSuggestionEvent e) {
        super.fillSuggestions(e);

        etMustTags.setAdapter(completionsAdapter);
        etShouldTags.setAdapter(completionsAdapter);

        super.showSuggestionTags(flMustTagSuggestions, etMustTags, e);
        super.showSuggestionTags(flShouldTagSuggestions, etShouldTags, e);

        completionsAdapter.notifyDataSetChanged();
    }

    public ArrayList<String> getMustTags() {
        return new ArrayList<String>(Arrays.asList(etMustTags.getText().toString().split(",")));
    }

    public ArrayList<String> getShouldTags() {
        return new ArrayList<String>(Arrays.asList(etShouldTags.getText().toString().split(",")));
    }

    public Location getLocation() {
        // TODO OpenStreetMaps stuff
        return new Location(
                Double.parseDouble(etLocationLat.getText().toString()),
                Double.parseDouble(etLocationLon.getText().toString()));
    }

    public int getDistance() {
        return Integer.parseInt(etDistance.getText().toString());
    }

    public Price getPrice() {
        return new Price(
                Double.parseDouble(etPriceMin.getText().toString()),
                Double.parseDouble(etPriceMax.getText().toString()));
    }
}
