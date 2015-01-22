package neeedo.imimaprx.htw.de.neeedo.RestService;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.ExecutionException;

import neeedo.imimaprx.htw.de.neeedo.Entities.Demand;
import neeedo.imimaprx.htw.de.neeedo.Entities.LocalDemands;
import neeedo.imimaprx.htw.de.neeedo.R;


/**
 * Created by Admin on 14.01.2015.
 */
public class HttpPostActivity extends Activity {


    protected static final String TAG = HttpPostActivity.class.getSimpleName();

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Intent returnIntent = new Intent();

            String result = new PostDemandTask().execute().get();
            returnIntent.putExtra("result", result);
            setResult(RESULT_OK, returnIntent);
            finish();

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private class PostDemandTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            try {


                // The URL for making the GET request
                final String url = getString(R.string.base_uri) + "demands";

                HttpHeaders requestHeaders = new HttpHeaders();

                // Sending a JSON object i.e. "application/json"
                requestHeaders.setContentType(MediaType.APPLICATION_JSON);

                // Populate the JsonManagerAnswers object to serialize and headers in an
                // HttpEntity object to use for the request
                HttpEntity<Demand> requestEntity = new HttpEntity<Demand>(LocalDemands.getInstance().getPostDemand(), requestHeaders);

                // Create a new RestTemplate instance
                RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());

                restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                // Make the network request, posting the message, expecting a String in response from the server
                ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity,
                        String.class);

                // Return the response body to display to the user
                return response.getBody();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage(), e);
            }

            return "Failed";
        }
    }

    private ClientHttpRequestFactory clientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setReadTimeout(5000);
        factory.setConnectTimeout(5000);
        return factory;
    }

}
