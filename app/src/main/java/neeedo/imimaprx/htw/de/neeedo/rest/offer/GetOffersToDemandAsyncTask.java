package neeedo.imimaprx.htw.de.neeedo.rest.offer;


import android.util.Log;

import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import neeedo.imimaprx.htw.de.neeedo.entities.Demand;
import neeedo.imimaprx.htw.de.neeedo.entities.Offers;
import neeedo.imimaprx.htw.de.neeedo.factory.HttpRequestFactoryProviderImpl;
import neeedo.imimaprx.htw.de.neeedo.models.ActiveUser;
import neeedo.imimaprx.htw.de.neeedo.models.DemandsModel;
import neeedo.imimaprx.htw.de.neeedo.models.OffersModel;
import neeedo.imimaprx.htw.de.neeedo.utils.ServerConstantsUtils;
import neeedo.imimaprx.htw.de.neeedo.rest.BaseAsyncTask;

public class GetOffersToDemandAsyncTask extends BaseAsyncTask {


    @Override
    protected Object doInBackground(Object[] params) {
        try {


            final ActiveUser activeUser = ActiveUser.getInstance();
            String email = activeUser.getUsername();
            String url = ServerConstantsUtils.getActiveServer() + "users/mail/";

            if (email == null || email == "") {
                return "Failed, no E-Mail is given";
            }

            url += email;

            HttpBasicAuthentication authentication = new HttpBasicAuthentication(email, activeUser.getUserPassword());

            HttpHeaders requestHeaders = new HttpHeaders();

            requestHeaders.setAuthorization(authentication);

            List<MediaType> acceptableMediaTypes = new ArrayList<>();
            acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
            requestHeaders.setAccept(acceptableMediaTypes);

            HttpEntity<Demand> requestEntity = new HttpEntity<Demand>(DemandsModel.getInstance().getPostDemand(), requestHeaders);


            RestTemplate restTemplate = new RestTemplate(HttpRequestFactoryProviderImpl.getClientHttpRequestFactorySSLSupport(9000));


            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            ResponseEntity<Offers> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Offers.class);

            Offers offers = response.getBody();

            OffersModel.getInstance().setOffers(offers);

            return "Success";//TODO use proper entities


        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
            return "Failed";//TODO use proper entities
        }
    }
}
