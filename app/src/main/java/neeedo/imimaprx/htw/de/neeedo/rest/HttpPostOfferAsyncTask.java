package neeedo.imimaprx.htw.de.neeedo.rest;

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

import neeedo.imimaprx.htw.de.neeedo.entities.Offer;
import neeedo.imimaprx.htw.de.neeedo.entities.SingleOffer;
import neeedo.imimaprx.htw.de.neeedo.factory.HttpRequestFactoryProviderImpl;
import neeedo.imimaprx.htw.de.neeedo.models.ActiveUser;
import neeedo.imimaprx.htw.de.neeedo.models.OffersModel;

public class HttpPostOfferAsyncTask extends SuperHttpAsyncTask {
    @Override
    protected Object doInBackground(Object[] params) {
        try {
            final String url = ServerConstants.getActiveServer() + "offers";

            OffersModel offersModel = OffersModel.getInstance();
            final ActiveUser activeUser = ActiveUser.getInstance();

            HttpBasicAuthentication authentication = new HttpBasicAuthentication(activeUser.getUsername(), activeUser.getUserPassword());

            HttpHeaders requestHeaders = new HttpHeaders();

            requestHeaders.setAuthorization(authentication);
            requestHeaders.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Offer> requestEntity = new HttpEntity<Offer>(offersModel.getPostOffer(), requestHeaders);

            RestTemplate restTemplate = new RestTemplate(HttpRequestFactoryProviderImpl.getClientHttpRequestFactorySSLSupport(9000));

            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            ResponseEntity<SingleOffer> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, SingleOffer.class);

            SingleOffer singleOffer = response.getBody();
            offersModel.setSingleOffer(singleOffer);

            //Is needed to merge {@link SingleOffer} object into offers list.
            offersModel.getOffers().getOffers().add(singleOffer.getOffer());

            return "Success";
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
            return "Failed";
        }
    }
}
