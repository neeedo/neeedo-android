package neeedo.imimaprx.htw.de.neeedo;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import neeedo.imimaprx.htw.de.neeedo.entities.util.Location;


public class LocationChooserActivity extends ActionBarActivity {
    public static final String NOMINATIM_SERVICE_URL = "http://nominatim.openstreetmap.org/";
    private MapView mapView;
    private AutoCompleteTextView autoCompleteTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_chooser);

        final ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        mapView = new MapView(this, null);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        mapView.getController().setZoom(12);
        mapView.setClickable(true);
        mapView.setBuiltInZoomControls(true);
        mapView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        RelativeLayout mapContainer = (RelativeLayout) findViewById(R.id.locationChooserMapContainer);
        mapContainer.addView(mapView);

        //this is a hack to get around one of the osmdroid bugs
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mapView.getController().animateTo(new GeoPoint(52468277, 13425979));
            }
        }, 200);


        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteAddress);

        autoCompleteTextView.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                new NewSearchForLocationHandler(s.toString()).execute();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_location_chooser, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class NewSearchForLocationHandler extends AsyncTask {
        private final String query;

        public NewSearchForLocationHandler(String query) {
            this.query = query;
        }

        @Override
        protected Object doInBackground(Object[] params) {

            InputStream is = null;
            String json = "";
            ArrayList<FoundLocation> locationsArrayList;

            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost("http://nominatim.openstreetmap.org/search?q=berlin+hauptstr&format=json");

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                    System.out.println(line);
                }
                is.close();
                json = sb.toString();

                JSONArray locationJSONArray = new JSONArray(json);
                Log.d("1", locationJSONArray.toString());

                locationsArrayList = new ArrayList<FoundLocation>();

                for (int i = 0; i < locationJSONArray.length(); i++) {
                    JSONObject currentLocationObject = (JSONObject) locationJSONArray.get(i);

                    FoundLocation foundLocation = new FoundLocation( currentLocationObject);
                    locationsArrayList.add(foundLocation);
                }
                Log.d("2", locationsArrayList.toString());
//                locationArray.getJSONArray()
            } catch (Exception e) {
                Log.e("Buffer Error", "Error converting result " + e.toString());
            }

            return "foo!";
        }
    }

}
