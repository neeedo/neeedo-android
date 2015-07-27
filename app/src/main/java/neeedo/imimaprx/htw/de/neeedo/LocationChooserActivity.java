package neeedo.imimaprx.htw.de.neeedo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
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
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.bonuspack.overlays.MapEventsOverlay;
import org.osmdroid.bonuspack.overlays.MapEventsReceiver;
import org.osmdroid.bonuspack.overlays.Polygon;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;
import java.util.List;


public class LocationChooserActivity extends ActionBarActivity implements MapEventsReceiver {
    private MapView mapView;
    private LocationAutoCompleteTextView autoCompleteTextView;
    private DefaultResourceProxyImpl resourceProxy;
    private Activity that = this;
    private GeoPoint selectedGeoPoint = null;
    private int selectedDistanceInKm;
    private TextView distanceTextView;
    private boolean withDistance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent(); // gets the previously created intent
        withDistance = intent.getBooleanExtra("withDistance", false);

        setContentView(R.layout.activity_location_chooser);

        final ImageButton findOwnLocationButton = (ImageButton) findViewById(R.id.find_own_location);
        final LocationManager mLocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        findOwnLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog mProgressDialog = new ProgressDialog(findOwnLocationButton.getContext());
                mProgressDialog.setMessage(getString(R.string.searching_location));
                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                mProgressDialog.setCancelable(true);
                mProgressDialog.show();

                final LocationListener mLocListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        float accuracy = location.getAccuracy();
                        if (accuracy < 15) {//if pretty sure to have found location in a radius of 15 meters
                            mLocManager.removeUpdates(this);
                            setLocationSelected(new GeoPoint(location.getLatitude(), location.getLongitude()));
                            mProgressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                    }
                };
                mLocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mLocListener);
            }
        });

        final SeekBar distanceSeekBar = (SeekBar) findViewById(R.id.distance_seek_bar);
        distanceSeekBar.setVisibility(withDistance ? View.VISIBLE : View.GONE);

        distanceTextView = (TextView) findViewById(R.id.distance_text_view);
        distanceTextView.setVisibility(withDistance ? View.VISIBLE : View.GONE);

        distanceSeekBar.setProgress(9);

        distanceSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                setNewDistance(progress + 1);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


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

        //TODO make last known location
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Location lastKnownLocation = mLocManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
                if (lastKnownLocation != null)
                    mapView.getController().animateTo(new GeoPoint(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude()));
                else
                    mapView.getController().animateTo(new GeoPoint(52468277, 13425979));
            }
        }, 200);

        autoCompleteTextView = (LocationAutoCompleteTextView) findViewById(R.id.autoCompleteAddress);
        autoCompleteTextView.setThreshold(3);//will start working after third character
        autoCompleteTextView.setAdapter(new ArrayAdapter<FoundLocation>(this, android.R.layout.simple_dropdown_item_1line));
        autoCompleteTextView.setHint(getString(R.string.location_chooser_search_hint));
        autoCompleteTextView.clearFocus();
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

        setNewDistance(10);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    private void setNewDistance(int distanceInKm) {
        selectedDistanceInKm = distanceInKm;
        distanceTextView.setText(getText(R.string.kilometer_radius) + String.valueOf(distanceInKm));
        setZoomAccordingToDistance();

        if (selectedGeoPoint != null) {
            refreshViewToNewSettings();
        }
    }

    private void setZoomAccordingToDistance() {
        int newZoomLevel = mapView.getZoomLevel();

        if (selectedDistanceInKm <= 3)
            newZoomLevel = 14;
        else if (selectedDistanceInKm <= 6)
            newZoomLevel = 13;
        else if (selectedDistanceInKm <= 12)
            newZoomLevel = 12;
        else if (selectedDistanceInKm <= 25)
            newZoomLevel = 11;
        else if (selectedDistanceInKm <= 50)
            newZoomLevel = 10;
        else if (selectedDistanceInKm <= 95)
            newZoomLevel = 9;
        else if (selectedDistanceInKm <= 100)
            newZoomLevel = 8;

        mapView.getController().setZoom(newZoomLevel);
    }

    private void setLocationSelected(GeoPoint geoPoint) {
        selectedGeoPoint = geoPoint;
        refreshViewToNewSettings();
    }

    private void refreshViewToNewSettings() {
        mapView.getController().animateTo(selectedGeoPoint);

        deleteAllUiOverlays();

        if (withDistance) {
            Polygon circle = new Polygon(this);
            circle.setPoints(Polygon.pointsAsCircle(selectedGeoPoint, selectedDistanceInKm * 1000));
            circle.setFillColor(0xaa88BEB1);
            circle.setStrokeColor(0x88BEB1);
            circle.setStrokeWidth(2);
            mapView.getOverlays().add(circle);
        }

        ArrayList<OverlayItem> ownOverlay = new ArrayList<OverlayItem>();
        ownOverlay.add(new OverlayItem("", "", (GeoPoint) selectedGeoPoint));
        ItemizedIconOverlay userLocationOverlay = new ItemizedIconOverlay<OverlayItem>(ownOverlay, getResources().getDrawable(R.drawable.map_marker), null, resourceProxy);
        mapView.getOverlays().add(userLocationOverlay);

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
        if (selectedGeoPoint != null) {
            Intent output = new Intent();
            output.putExtra("latitude", Double.toString(selectedGeoPoint.getLatitude()));
            output.putExtra("longitude", Double.toString(selectedGeoPoint.getLongitude()));
            if (withDistance)
                output.putExtra("distance", Integer.toString(selectedDistanceInKm));
            setResult(RESULT_OK, output);
        } else {
            setResult(RESULT_CANCELED);
        }

        ViewGroup view = (ViewGroup) getWindow().getDecorView();
        view.removeAllViews();

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
        } else if (id == android.R.id.home) {
            super.onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint geoPoint) {
        return false;
    }

    @Override
    public boolean longPressHelper(GeoPoint geoPoint) {
        setLocationSelected(geoPoint);
        return true;
    }
}
