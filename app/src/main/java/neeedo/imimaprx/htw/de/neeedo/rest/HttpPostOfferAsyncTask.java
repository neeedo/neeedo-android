package neeedo.imimaprx.htw.de.neeedo.rest;

import android.util.Log;

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
import neeedo.imimaprx.htw.de.neeedo.models.OffersModel;

public class HttpPostOfferAsyncTask extends SuperHttpAsyncTask {
    @Override
    protected Object doInBackground(Object[] params) {
        try {


            final String url = ServerConstants.ACTIVE_SERVER + "offers";

            OffersModel offersModel = OffersModel.getInstance();
            HttpHeaders requestHeaders;
            requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Offer> requestEntity = new HttpEntity<Offer>(offersModel.getPostOffer(), requestHeaders);

            //Change to {@link HttpRequestFactoryProviderImpl.getClientHttpRequestFactorySSLSupport} for SSL support.
            RestTemplate restTemplate = new RestTemplate(HttpRequestFactoryProviderImpl.getClientHttpRequestFactory(9000));


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
