package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.apmem.tools.layouts.FlowLayout;
import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.util.Location;
import neeedo.imimaprx.htw.de.neeedo.entities.util.Price;
import neeedo.imimaprx.htw.de.neeedo.events.GetSuggestionEvent;
import neeedo.imimaprx.htw.de.neeedo.fragments.handler.StartLocationChooserHandler;
import neeedo.imimaprx.htw.de.neeedo.rest.util.BaseAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.utils.AutocompletionOnClickListener;
import neeedo.imimaprx.htw.de.neeedo.utils.AutocompletionOnFocusChangeListener;
import neeedo.imimaprx.htw.de.neeedo.utils.AutocompletionTextWatcher;
import neeedo.imimaprx.htw.de.neeedo.vo.RequestCodes;

public class FormDemandFragment extends FormFragment {
    protected MultiAutoCompleteTextView etMustTags;
    protected FlowLayout flMustTagSuggestions;
    protected MultiAutoCompleteTextView etShouldTags;
    protected FlowLayout flShouldTagSuggestions;
    protected EditText etPriceMin;
    protected EditText etPriceMax;
    protected Button btnSubmit;
    protected Button btnSetLocation;
    protected RelativeLayout mapContainer;

    protected GeoPoint selectedGeoPoint;
    protected int selectedDistance;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.form_demand_view, container, false);

        etMustTags = (MultiAutoCompleteTextView) view.findViewById(R.id.etMustTags);
        flMustTagSuggestions = (FlowLayout) view.findViewById(R.id.flMustTagSuggestions);
        etShouldTags = (MultiAutoCompleteTextView) view.findViewById(R.id.etShouldTags);
        flShouldTagSuggestions = (FlowLayout) view.findViewById(R.id.flShouldTagSuggestions);
        etPriceMin = (EditText) view.findViewById(R.id.etPriceMin);
        etPriceMax = (EditText) view.findViewById(R.id.etPriceMax);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        btnSetLocation = (Button) view.findViewById(R.id.btnChooseLocation);
        mapContainer = (RelativeLayout) view.findViewById(R.id.mapContainer);

        etMustTags.addTextChangedListener(new AutocompletionTextWatcher(this, etMustTags, null, BaseAsyncTask.CompletionType.TAG));
        etShouldTags.addTextChangedListener(new AutocompletionTextWatcher(this, etShouldTags, null, BaseAsyncTask.CompletionType.TAG));

        etMustTags.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        etShouldTags.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        etMustTags.setOnClickListener(new AutocompletionOnClickListener(etMustTags));
        etShouldTags.setOnClickListener(new AutocompletionOnClickListener(etShouldTags));

        etMustTags.addTextChangedListener(new AutocompletionTextWatcher(this, etMustTags, etShouldTags, BaseAsyncTask.CompletionType.PHRASE));
        etShouldTags.addTextChangedListener(new AutocompletionTextWatcher(this, etShouldTags, etMustTags, BaseAsyncTask.CompletionType.PHRASE));

        etMustTags.setOnFocusChangeListener(new AutocompletionOnFocusChangeListener(flMustTagSuggestions));
        etShouldTags.setOnFocusChangeListener(new AutocompletionOnFocusChangeListener(flShouldTagSuggestions));

        btnSetLocation.setOnClickListener(new StartLocationChooserHandler(this, true));

        validation();

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
     return new Location(selectedGeoPoint);
    }

    public Price getPrice() {
        return new Price(
                Double.parseDouble(etPriceMin.getText().toString()),
                Double.parseDouble(etPriceMax.getText().toString()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == RequestCodes.FIND_LOCATION_REQUEST_CODE) {
            selectedGeoPoint = new GeoPoint(Double.parseDouble(intent.getStringExtra("latitude")), Double.parseDouble(intent.getStringExtra("longitude")));
            selectedDistance = Integer.parseInt(intent.getStringExtra("distance"));
            setLocation(selectedGeoPoint);
            btnSetLocation.setError(null);

        }
    }

    protected void setLocation(final GeoPoint geoPoint) {
        final MapView mapView = new MapView(getActivity(), null);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        mapView.getController().setZoom(18);
        mapView.setClickable(true);
        mapView.setBuiltInZoomControls(true);
        mapView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        mapContainer.setVisibility(View.VISIBLE);
        mapContainer.addView(mapView);

        ResourceProxy resourceProxy = new DefaultResourceProxyImpl(getActivity());
        ArrayList<OverlayItem> ownOverlay = new ArrayList<OverlayItem>();
        ownOverlay.add(new OverlayItem("", "", (GeoPoint) geoPoint));
        ItemizedIconOverlay userLocationOverlay = new ItemizedIconOverlay<OverlayItem>(ownOverlay, getResources().getDrawable(R.drawable.map_marker), null, resourceProxy);
        mapView.getOverlays().add(userLocationOverlay);

        // this is a hack to get around one of the osmdroid bugs
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mapView.getController().animateTo(geoPoint);
            }
        }, 200);
    }

    protected void validation() {
        validViews.put(etMustTags, false);
        validViews.put(etShouldTags, false);
        validViews.put(etPriceMin, false);
        validViews.put(etPriceMax, false);
        validViews.put(btnSetLocation, false);

        etMustTags.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                String mustTags = etMustTags.getText().toString();
                if (!focus) {
                    if (mustTags.length() <= 0) {
                        etMustTags.setError(getResources().getString(R.string.validation_no_tag));
                    }
                    if (etMustTags.getError() == null) {
                        validViews.put(etMustTags, true);
                    }
                }
            }
        });
        etShouldTags.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                // TODO remove this validation?
                String shouldTags = etShouldTags.getText().toString();
                if (!focus) {
                    if (etShouldTags.getError() == null) {
                        validViews.put(etShouldTags, true);
                    }
                }
            }
        });
        etPriceMin.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                String priceMin = String.valueOf(etPriceMin.getText().toString());
                String priceMax = String.valueOf(etPriceMax.getText().toString());
                if (!focus) {
                    if (priceMin.length() > 0 && Double.valueOf(priceMin) < 0) {
                        etPriceMin.setError(getResources().getString(R.string.validation_value_negative));
                    }
                    if (priceMin.length() > 0 && priceMax.length() > 0 &&
                            Double.valueOf(priceMin) > Double.valueOf(priceMax)) {
                        etPriceMin.setError(getResources().getString(R.string.validation_price_min_high));
                    } else {
                        etPriceMin.setError(null);
                    }
                    if (priceMin.length() > 0 && !priceMin.matches("(\\d+(.(\\d{2}|\\d{0}))|\\d)")) {
                        etPriceMin.setError(getResources().getString(R.string.validation_price_number_format));
                    }
                    if (priceMin.length() == 0) {
                        // TODO if not set, it should be 0
                        etPriceMin.setError(getResources().getString(R.string.validation_empty_field));
                    }
                    if (etPriceMin.getError() == null) {
                        validViews.put(etPriceMin, true);
                    }
                }
            }
        });
        etPriceMax.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                String priceMin = String.valueOf(etPriceMin.getText().toString());
                String priceMax = String.valueOf(etPriceMax.getText().toString());
                if (!focus) {
                    if (priceMax.length() > 0 && Double.valueOf(priceMax) < 0) {
                        etPriceMax.setError(getResources().getString(R.string.validation_value_negative));
                    }
                    if (priceMax.length() > 0 && priceMin.length() > 0 &&
                            Double.valueOf(priceMin) > Double.valueOf(priceMax)) {
                        etPriceMax.setError(getResources().getString(R.string.validation_price_max_low));
                    } else {
                        etPriceMax.setError(null);
                    }
                    if (priceMax.length() > 0 && !priceMax.matches("(\\d+(.(\\d{2}|\\d{0}))|\\d)")) {
                        etPriceMax.setError(getResources().getString(R.string.validation_price_number_format));
                    }
                    if (priceMax.length() == 0) {
                        etPriceMax.setError(getResources().getString(R.string.validation_empty_field));
                    }
                    if (etPriceMax.getError() == null) {
                        validViews.put(etPriceMax, true);
                    }
                }
            }
        });
        btnSetLocation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if (selectedGeoPoint == null) {
                    btnSetLocation.setError(getResources().getString(R.string.validation_no_location));
                }
                if (btnSetLocation.getError() == null) {
                    validViews.put(btnSetLocation, true);
                }
            }
        });
    }

    public boolean checkValidation() {
        // force re-focus all textviews for showing errors from above
        etMustTags.requestFocusFromTouch();
        etShouldTags.requestFocusFromTouch();
        etPriceMin.requestFocusFromTouch();
        etPriceMax.requestFocusFromTouch();
        btnSetLocation.requestFocusFromTouch();
        btnSubmit.requestFocusFromTouch();

        if(validViews.containsValue(false)) {
            Log.d("Validation", "Invalid");
            return false;
        } else {
            Log.d("Validation", "Valid");
            return true;
        }
    }

    public Button getBtnSubmit() {
        return btnSubmit;
    }

    public int getDistance() {
        return selectedDistance;
    }
}
