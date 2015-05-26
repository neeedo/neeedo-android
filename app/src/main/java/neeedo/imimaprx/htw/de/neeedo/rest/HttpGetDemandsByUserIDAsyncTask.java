package neeedo.imimaprx.htw.de.neeedo.rest;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import neeedo.imimaprx.htw.de.neeedo.entities.Demands;
import neeedo.imimaprx.htw.de.neeedo.entities.SingleDemand;
import neeedo.imimaprx.htw.de.neeedo.factory.HttpRequestFactoryProviderImpl;
import neeedo.imimaprx.htw.de.neeedo.models.ActiveUser;
import neeedo.imimaprx.htw.de.neeedo.models.DemandsModel;
import neeedo.imimaprx.htw.de.neeedo.models.UserModel;

public class HttpGetDemandsByUserIDAsyncTask extends SuperHttpAsyncTask {
    @Override
    protected Object doInBackground(Object[] params) {
        try {

            final String url = ServerConstants.getActiveServer() + "demands/user/" + UserModel.getInstance().getUser().getId();;

            final ActiveUser activeUser = ActiveUser.getInstance();

            HttpBasicAuthentication authentication = new HttpBasicAuthentication(activeUser.getUsername(), activeUser.getUserPassword());

            HttpHeaders requestHeaders = new HttpHeaders();

            requestHeaders.setAuthorization(authentication);
            List<MediaType> acceptableMediaTypes = new ArrayList<>();
            acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
            requestHeaders.setAccept(acceptableMediaTypes);

            HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);

            RestTemplate restTemplate = new RestTemplate(HttpRequestFactoryProviderImpl.getClientHttpRequestFactorySSLSupport(5000));

            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            ResponseEntity<Demands> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
                    Demands.class);

            final Demands demands = responseEntity.getBody();

            DemandsModel.getInstance().setDemands(demands);


            return "Success"; //TODO use proper entities
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
            return "Failed";//TODO use proper entities
        }
    }

}
