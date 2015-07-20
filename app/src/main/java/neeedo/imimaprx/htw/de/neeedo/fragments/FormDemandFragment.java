package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.ArrayList;
import java.util.Arrays;

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
    protected EditText etDistance;
    protected EditText etPriceMin;
    protected EditText etPriceMax;
    protected Button btnSubmit;
    protected Button btnSetLocation;
    protected RelativeLayout mapContainer;

    protected GeoPoint selectedGeoPoint;

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
        etDistance = (EditText) view.findViewById(R.id.etDistance);
        etPriceMin = (EditText) view.findViewById(R.id.etPriceMin);
        etPriceMax = (EditText) view.findViewById(R.id.etPriceMax);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        btnSetLocation = (Button) view.findViewById(R.id.btnChooseLocation);
        mapContainer = (RelativeLayout) view.findViewById(R.id.mapContainer);

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

        btnSetLocation.setOnClickListener(new StartLocationChooserHandler(this));

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
        return new Location(0.0, 0.0);
    }

    public int getDistance() {
        return Integer.parseInt(etDistance.getText().toString());
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
            Toast.makeText(getActivity(), R.string.fail, Toast.LENGTH_SHORT).show();
            return;
        }

        if (requestCode == RequestCodes.FIND_LOCATION_REQUEST_CODE) {
            String latitude = intent.getStringExtra("latitude");
            String longitude = intent.getStringExtra("longitude");
            selectedGeoPoint = new GeoPoint(Double.parseDouble(latitude), Double.parseDouble(longitude));
            setLocation(selectedGeoPoint);
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
}
