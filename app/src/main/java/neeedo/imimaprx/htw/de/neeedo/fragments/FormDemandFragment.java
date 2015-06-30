package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TextView;

import org.apmem.tools.layouts.FlowLayout;

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.util.Location;
import neeedo.imimaprx.htw.de.neeedo.events.GetSuggestionEvent;
import neeedo.imimaprx.htw.de.neeedo.helpers.LocationHelper;
import neeedo.imimaprx.htw.de.neeedo.models.ActiveUser;
import neeedo.imimaprx.htw.de.neeedo.rest.util.BaseAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.utils.AutocompletionOnClickListener;
import neeedo.imimaprx.htw.de.neeedo.utils.AutocompletionTextWatcher;

public class FormDemandFragment extends FormFragment {
    protected final ActiveUser activeUser = ActiveUser.getInstance();

    protected MultiAutoCompleteTextView etMustTags;
    protected FlowLayout flMustTagSuggestions;
    protected MultiAutoCompleteTextView etShouldTags;
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

        etMustTags = (MultiAutoCompleteTextView) view.findViewById(R.id.etMustTags);
        flMustTagSuggestions = (FlowLayout) view.findViewById(R.id.flMustTagSuggestions);
        etShouldTags = (MultiAutoCompleteTextView) view.findViewById(R.id.etShouldTags);
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


        return view;
    }

    @Override
    public void fillSuggestions(GetSuggestionEvent e) {
        super.fillSuggestions(e);

        etMustTags.setAdapter(completionsAdapter);
        etShouldTags.setAdapter(completionsAdapter);

        if(e.getCompletionType().equals(BaseAsyncTask.CompletionType.PHRASE)) {
            flMustTagSuggestions.removeAllViewsInLayout();
        }

        for(final String suggestion : suggestions) {
            final TextView tvMustTag = new TextView(getActivity());
            tvMustTag.setText(suggestion);
            tvMustTag.setPadding(15, 10, 15, 10);
            tvMustTag.setBackgroundColor(Color.parseColor("#88BEB1"));
            FlowLayout.LayoutParams layoutParams = new FlowLayout.LayoutParams(
                    FlowLayout.LayoutParams.WRAP_CONTENT, FlowLayout.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(5, 5, 5, 10);
            flMustTagSuggestions.addView(tvMustTag, layoutParams);
            tvMustTag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String tag = tvMustTag.getText().toString();
                    etMustTags.setText(etMustTags.getText().toString() + tag + ", ");
                    etMustTags.setSelection(etMustTags.length());
                    flMustTagSuggestions.removeViewInLayout(tvMustTag);
                    suggestions.remove(suggestion);
                }
            });
        }
    }
}
