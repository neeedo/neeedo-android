package neeedo.imimaprx.htw.de.neeedo.rest.matching;


import android.util.Log;

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

import neeedo.imimaprx.htw.de.neeedo.entities.demand.Demand;
import neeedo.imimaprx.htw.de.neeedo.entities.offer.Offers;
import neeedo.imimaprx.htw.de.neeedo.events.FoundMatchesEvent;
import neeedo.imimaprx.htw.de.neeedo.factory.HttpRequestFactoryProviderImpl;
import neeedo.imimaprx.htw.de.neeedo.models.DemandsModel;
import neeedo.imimaprx.htw.de.neeedo.rest.util.BaseAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.util.returntype.RestResult;
import neeedo.imimaprx.htw.de.neeedo.utils.ServerConstantsUtils;

public class GetOffersToDemandAsyncTask extends BaseAsyncTask {

    private Demand demand;

    public GetOffersToDemandAsyncTask(Demand demand) {
        this.demand = demand;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            final String url = ServerConstantsUtils.getActiveServer() + "matching/demand";
            HttpHeaders requestHeaders = new HttpHeaders();
            setAuthorisationHeaders(requestHeaders);

            requestHeaders.setContentType(MediaType.APPLICATION_JSON);

            List<MediaType> acceptableMediaTypes = new ArrayList<>();
            acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
            requestHeaders.setAccept(acceptableMediaTypes);
            HttpEntity<Demand> requestEntity = new HttpEntity<Demand>(demand, requestHeaders);
            RestTemplate restTemplate = new RestTemplate(HttpRequestFactoryProviderImpl.getClientHttpRequestFactorySSLSupport(5000));
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<Offers> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Offers.class);
            Offers offers = responseEntity.getBody();

            DemandsModel.getInstance().addOffersToCurrentDemand(demand, offers);

            return new RestResult(RestResult.ReturnType.SUCCESS);
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
            String message = getErrorMessage(e.getMessage());
            showToast(message);
            return new RestResult(RestResult.ReturnType.FAILED);
        }
    }

    @Override
    protected void onPostExecute(Object o) {
        if(((RestResult) o).getResult() == RestResult.ReturnType.SUCCESS) {
            eventService.post(new FoundMatchesEvent());
        }
    }

}
