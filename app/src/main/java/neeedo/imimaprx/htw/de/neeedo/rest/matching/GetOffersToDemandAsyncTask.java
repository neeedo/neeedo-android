package neeedo.imimaprx.htw.de.neeedo.rest.matching;


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

import neeedo.imimaprx.htw.de.neeedo.entities.Demand;
import neeedo.imimaprx.htw.de.neeedo.entities.Offers;
import neeedo.imimaprx.htw.de.neeedo.factory.HttpRequestFactoryProviderImpl;
import neeedo.imimaprx.htw.de.neeedo.models.ActiveUser;
import neeedo.imimaprx.htw.de.neeedo.models.DemandsModel;
import neeedo.imimaprx.htw.de.neeedo.models.OffersModel;
import neeedo.imimaprx.htw.de.neeedo.rest.BaseAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.utils.ServerConstantsUtils;

/**
 * Creates experimental Offerslist to the given Demand in DemandModel
 */
public class GetOffersToDemandAsyncTask extends BaseAsyncTask {

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            final String url = ServerConstantsUtils.getActiveServer() + "/matching/demand/1/1";
            final ActiveUser activeUser = ActiveUser.getInstance();
            HttpHeaders requestHeaders = new HttpHeaders();
            HttpBasicAuthentication authentication = new HttpBasicAuthentication(activeUser.getUsername(), activeUser.getUserPassword());
            requestHeaders.setAuthorization(authentication);
            List<MediaType> acceptableMediaTypes = new ArrayList<>();
            acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
            requestHeaders.setAccept(acceptableMediaTypes);
            HttpEntity<Demand> requestEntity = new HttpEntity<Demand>(DemandsModel.getInstance().getPostDemand(), requestHeaders);
            RestTemplate restTemplate = new RestTemplate(HttpRequestFactoryProviderImpl.getClientHttpRequestFactorySSLSupport(5000));
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<Offers> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Offers.class);
            Offers offers = responseEntity.getBody();
            OffersModel.getInstance().setOffers(offers);
            return ReturnType.SUCCESS;
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
            return ReturnType.FAILED;
        }
    }
}
