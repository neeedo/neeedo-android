package neeedo.imimaprx.htw.de.neeedo.RestService;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import neeedo.imimaprx.htw.de.neeedo.Entities.LocalDemands;
import neeedo.imimaprx.htw.de.neeedo.Entities.SingleDemand;
import neeedo.imimaprx.htw.de.neeedo.R;

public class HttpGetByIDActivity extends Activity {

    private volatile boolean destroyed = false;
    protected static final String TAG = HttpGetByIDActivity.class.getSimpleName();

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            Intent returnIntent = new Intent();

            String result = new DownloadDemandsTask().execute().get();
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
        this.destroyed = true;
    }

    private class DownloadDemandsTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            try {
                Handler mHandler = new Handler(Looper.getMainLooper());
                final String url = getString(R.string.base_uri) + "demands/1";

                HttpHeaders requestHeaders = new HttpHeaders();
                List<MediaType> acceptableMediaTypes = new ArrayList<>();
                acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
                requestHeaders.setAccept(acceptableMediaTypes);

                HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);

                RestTemplate restTemplate = new RestTemplate(clientHttpRequestFactory());

                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                ResponseEntity<SingleDemand> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
                        SingleDemand.class);

                final SingleDemand singleDemand = responseEntity.getBody();

                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        LocalDemands.getInstance().setSingleDemand(singleDemand);
                    }
                });

                return "Success";
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
