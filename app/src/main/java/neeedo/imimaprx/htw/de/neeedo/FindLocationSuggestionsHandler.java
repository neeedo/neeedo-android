package neeedo.imimaprx.htw.de.neeedo;

import android.app.Activity;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;

import neeedo.imimaprx.htw.de.neeedo.rest.util.returntype.FindLocationResult;
import neeedo.imimaprx.htw.de.neeedo.rest.util.returntype.RestResult;

public class FindLocationSuggestionsHandler extends AsyncTask<Void, Void, FindLocationResult> {
    private static int requestCounter = 0;
    private final LocationAutoCompleteTextView autoCompleteTextView;
    private final Activity activity;
    private ArrayAdapter<FoundLocation> adapter;
    private String query;

    public FindLocationSuggestionsHandler(String query, ArrayAdapter<FoundLocation> adapter, LocationAutoCompleteTextView autoCompleteTextView, Activity activity) {
        this.adapter = adapter;
        this.autoCompleteTextView = autoCompleteTextView;
        this.activity = activity;
        try {
            this.query = URLEncoder.encode(query, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected FindLocationResult doInBackground(Void... voids) {
        InputStream is = null;
        String jsonString = "";
        ArrayList<FoundLocation> locationsArrayList = null;

        requestCounter++;
        int requestNumberThisRequest = requestCounter;

        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            String url = "http://nominatim.openstreetmap.org/search?q=" + query + "&format=json";
            HttpPost httpPost = new HttpPost(url);

            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            is = httpEntity.getContent();

            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"), 8);
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            is.close();
            jsonString = stringBuilder.toString();


            JSONArray locationJSONArray = new JSONArray(jsonString);

            locationsArrayList = new ArrayList<FoundLocation>();

            for (int i = 0; i < locationJSONArray.length(); i++) {
                JSONObject currentLocationObject = (JSONObject) locationJSONArray.get(i);
                FoundLocation foundLocation = new FoundLocation(currentLocationObject);
                locationsArrayList.add(foundLocation);
            }

            Log.d("foo", url + "\n" +
                    "returned" + jsonString + "\n" +
                    "size" + locationsArrayList.size());
        } catch (Exception e) {
            return new FindLocationResult(RestResult.ReturnType.FAILED, requestNumberThisRequest, null);
        }

        return new FindLocationResult(RestResult.ReturnType.SUCCESS, requestNumberThisRequest, locationsArrayList);
    }

    @Override
    protected void onPostExecute(FindLocationResult findLocationResult) {
        super.onPostExecute(findLocationResult);
        if (findLocationResult.getResult() == RestResult.ReturnType.FAILED) {
            return;
        } else if (findLocationResult.getResult() == RestResult.ReturnType.SUCCESS) {
            if (findLocationResult.getRequestNumberThisRequest() < requestCounter) { //if response is for the last request we have triggered
                return;
            }
            if (findLocationResult.getFoundLocations().size() == 0) {
                if (autoCompleteTextView.isPopupShowing()) {
                    autoCompleteTextView.dismissDropDown();
                }
            } else {
                adapter = new ArrayAdapter<FoundLocation>(activity, android.R.layout.simple_dropdown_item_1line, findLocationResult.getFoundLocations());
                autoCompleteTextView.setAdapter(adapter);
                autoCompleteTextView.showDropDown();
            }
        }
    }
}