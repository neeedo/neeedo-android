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
import neeedo.imimaprx.htw.de.neeedo.entities.demand.DemandMin;
import neeedo.imimaprx.htw.de.neeedo.entities.offer.Offers;
import neeedo.imimaprx.htw.de.neeedo.entities.user.UserMin;
import neeedo.imimaprx.htw.de.neeedo.events.FoundMatchesEvent;
import neeedo.imimaprx.htw.de.neeedo.factory.HttpRequestFactoryProviderImpl;
import neeedo.imimaprx.htw.de.neeedo.models.OffersModel;
import neeedo.imimaprx.htw.de.neeedo.rest.util.BaseAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.util.returntype.RestResult;
import neeedo.imimaprx.htw.de.neeedo.utils.ServerConstantsUtils;

/**
 * Creates experimental Offerslist to the given Demand in DemandModel
 */
public class GetOffersToDemandAsyncTask extends BaseAsyncTask {

    private Demand demand;

    public GetOffersToDemandAsyncTask(Demand demand) {
        this.demand = demand;
    }

    @Override
    protected void onPostExecute(Object o) {
        eventService.post(new FoundMatchesEvent());
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {

            DemandMin demandMin = createDemandMinByDemand(demand);
            final String url = ServerConstantsUtils.getActiveServer() + "matching/demand";
            HttpHeaders requestHeaders = new HttpHeaders();
            setAuthorisationHeaders(requestHeaders);

            List<MediaType> acceptableMediaTypes = new ArrayList<>();
            acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
            requestHeaders.setAccept(acceptableMediaTypes);
            HttpEntity<DemandMin> requestEntity = new HttpEntity<DemandMin>(demandMin, requestHeaders);
            RestTemplate restTemplate = new RestTemplate(HttpRequestFactoryProviderImpl.getClientHttpRequestFactorySSLSupport(5000));
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<Offers> responseEntity = restTemplate.exchange(url, HttpMethod.POST, requestEntity, Offers.class);
            Offers offers = responseEntity.getBody();
            OffersModel.getInstance().setOffers(offers);
            return new RestResult(this.getClass().getSimpleName(), RestResult.ReturnType.SUCCESS);
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
            return new RestResult(this.getClass().getSimpleName(), RestResult.ReturnType.FAILED);
        }
    }


    //Needed because Json ignore does not work somehow
    private DemandMin createDemandMinByDemand(Demand demand) {

        DemandMin demandMin = new DemandMin();
        UserMin userMin = new UserMin(demand.getUser().getId(), demand.getUser().getEmail());
        demandMin.setVersion(demand.getVersion());
        demandMin.setId(demand.getId());
        demandMin.setDistance(demand.getDistance());
        demandMin.setLocation(demand.getLocation());
        demandMin.setMustTags(demand.getMustTags());
        demandMin.setShouldTags(demand.getShouldTags());
        demandMin.setPrice(demand.getPrice());
        demandMin.setUser(userMin);
        return demandMin;

    }
}
