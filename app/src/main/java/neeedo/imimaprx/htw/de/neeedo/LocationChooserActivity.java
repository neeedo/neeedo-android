package neeedo.imimaprx.htw.de.neeedo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.bonuspack.overlays.GroundOverlay;
import org.osmdroid.bonuspack.overlays.MapEventsOverlay;
import org.osmdroid.bonuspack.overlays.MapEventsReceiver;
import org.osmdroid.bonuspack.overlays.Polygon;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.PathOverlay;

import java.util.ArrayList;
import java.util.List;


public class LocationChooserActivity extends ActionBarActivity implements MapEventsReceiver {
    public static final String NOMINATIM_SERVICE_URL = "http://nominatim.openstreetmap.org/";
    private MapView mapView;
    private LocationAutoCompleteTextView autoCompleteTextView;
    private DefaultResourceProxyImpl resourceProxy;
    private Activity that = this;
    private GeoPoint currentlySelectedGeoPoint = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_chooser);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        resourceProxy = new DefaultResourceProxyImpl(this);

        mapView = new MapView(this, null);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        mapView.getController().setZoom(12);
        mapView.setBuiltInZoomControls(true);
        mapView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        mapView.setClickable(true);

        RelativeLayout mapContainer = (RelativeLayout) findViewById(R.id.locationChooserMapContainer);
        mapContainer.addView(mapView);

        //this is a hack to get around one of the osmdroid bugs
        //TODO make last known location
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mapView.getController().animateTo(new GeoPoint(52468277, 13425979));
            }
        }, 200);

        autoCompleteTextView = (LocationAutoCompleteTextView) findViewById(R.id.autoCompleteAddress);
        autoCompleteTextView.setThreshold(3);//will start working after third character
        autoCompleteTextView.setAdapter(new ArrayAdapter<FoundLocation>(this, android.R.layout.simple_dropdown_item_1line));
        autoCompleteTextView.setHint(getString(R.string.location_chooser_search_hint));
        autoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence inputString, int start, int before, int count) {
                if (autoCompleteTextView.isPerformingCompletion()) {
                    return; // An item has been selected from the list. ignore.
                }
                new FindLocationSuggestionsHandler(inputString.toString(), (ArrayAdapter) autoCompleteTextView.getAdapter(), autoCompleteTextView, that).execute();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {
                ListAdapter adapter = autoCompleteTextView.getAdapter();
                FoundLocation selectedItem = (FoundLocation) adapter.getItem(position);
                GeoPoint geoPoint = new GeoPoint(selectedItem.getLatitude(), selectedItem.getLongitude());
                setLocationSelected(geoPoint);
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(autoCompleteTextView.getWindowToken(), 0);
                autoCompleteTextView.dismissDropDown();


            }
        });

        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this, this);
        mapView.getOverlays().add(mapEventsOverlay);
        mapView.invalidate();
    }

    private void setLocationSelected(GeoPoint geoPoint) {
        mapView.getController().animateTo(geoPoint);
        currentlySelectedGeoPoint = geoPoint;
        deleteAllUiOverlays();

        ArrayList<OverlayItem> ownOverlay = new ArrayList<OverlayItem>();
        ownOverlay.add(new OverlayItem("", "", (GeoPoint) geoPoint));
        ItemizedIconOverlay userLocationOverlay = new ItemizedIconOverlay<OverlayItem>(ownOverlay, getResources().getDrawable(R.drawable.map_marker), null, resourceProxy);
        mapView.getOverlays().add(userLocationOverlay);

        Polygon circle = new Polygon(this);

        circle.setPoints(Polygon.pointsAsCircle(geoPoint, 2000.0));
        circle.setFillColor(0x12121212);
        circle.setStrokeColor(Color.RED);
        circle.setStrokeWidth(2);

        mapView.getOverlays().add(circle);

        mapView.invalidate();
    }

    private void deleteAllUiOverlays() {
        List<Overlay> overlays = mapView.getOverlays();
        int startIndex = overlays.size() - 1;

        for (int i = startIndex; i >= 0; i--) {
            Overlay element = overlays.get(i);
            if (element instanceof MapEventsOverlay) {
                continue;
            }
            mapView.getOverlays().remove(element);
        }
    }

    private void closeActivityAndReturnData() {
        if (currentlySelectedGeoPoint != null) {
            Intent output = new Intent();
            output.putExtra("latitude", Double.toString(currentlySelectedGeoPoint.getLatitude()));
            output.putExtra("longitude", Double.toString(currentlySelectedGeoPoint.getLongitude()));
            setResult(RESULT_OK, output);
        } else {
            setResult(RESULT_CANCELED);
        }

        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_location_chooser, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.location_chooser_done) {
            closeActivityAndReturnData();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint geoPoint) {
        return false;
    }

    @Override
    public boolean longPressHelper(GeoPoint geoPoint) {
        Toast toast = Toast.makeText(getApplicationContext(), "Longitude: " + geoPoint.getLongitude() + " Latitude: " + geoPoint.getLatitude(), Toast.LENGTH_LONG);
        toast.show();
        setLocationSelected(geoPoint);
        return true;
    }
}
