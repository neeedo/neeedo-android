package neeedo.imimaprx.htw.de.neeedo.rest;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import neeedo.imimaprx.htw.de.neeedo.models.DemandsModel;
import neeedo.imimaprx.htw.de.neeedo.entities.SingleDemand;
import neeedo.imimaprx.htw.de.neeedo.factory.ClientHttpRequestFactoryProvider;

public class HttpGetByIDAsyncTask extends SuperHttpAsyncTask {
    @Override
    protected Object doInBackground(Object[] params) {
        try {
            Handler mHandler = new Handler(Looper.getMainLooper());
            final String url = ServerConstants.ACTIVE_SERVER  + "demands/1";

            HttpHeaders requestHeaders = new HttpHeaders();
            List<MediaType> acceptableMediaTypes = new ArrayList<>();
            acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
            requestHeaders.setAccept(acceptableMediaTypes);

            HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);

            RestTemplate restTemplate = new RestTemplate(ClientHttpRequestFactoryProvider.getClientHttpRequestFactory(5000));

            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            ResponseEntity<SingleDemand> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
                    SingleDemand.class);

            final SingleDemand singleDemand = responseEntity.getBody();

            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    DemandsModel.getInstance().setSingleDemand(singleDemand);
                }
            });

            return "Success"; //TODO use proper entities
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
            return "Failed";//TODO use proper entities
        }
    }

}
