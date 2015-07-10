package neeedo.imimaprx.htw.de.neeedo.rest.offer;

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

import neeedo.imimaprx.htw.de.neeedo.entities.offer.Offers;
import neeedo.imimaprx.htw.de.neeedo.entities.util.Location;
import neeedo.imimaprx.htw.de.neeedo.events.GetOfferFinishedEvent;
import neeedo.imimaprx.htw.de.neeedo.factory.HttpRequestFactoryProviderImpl;
import neeedo.imimaprx.htw.de.neeedo.models.OffersModel;
import neeedo.imimaprx.htw.de.neeedo.models.UserModel;
import neeedo.imimaprx.htw.de.neeedo.rest.util.BaseAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.util.returntype.RestResult;
import neeedo.imimaprx.htw.de.neeedo.service.EventService;
import neeedo.imimaprx.htw.de.neeedo.utils.ServerConstantsUtils;

public class GetOffersAsyncTask extends BaseAsyncTask {

    private GetEntitiesMode getEntitiesMode;
    private Integer limit;
    private Integer offset;
    private Location location;
    protected EventService eventService = EventService.getInstance();

    public GetOffersAsyncTask(GetEntitiesMode getEntitiesMode, Location location) {
        this(getEntitiesMode);
        this.location = location;
    }

    public GetOffersAsyncTask(GetEntitiesMode getEntitiesMode) {
        this.getEntitiesMode = getEntitiesMode;
    }

    public GetOffersAsyncTask(GetEntitiesMode getEntitiesMode, Integer limit, Integer offset) {
        this(getEntitiesMode);
        this.limit = limit;
        this.offset = offset;
    }

    @Override
    protected void onPostExecute(Object result) {
        if (result instanceof RestResult)
            eventService.post(new GetOfferFinishedEvent());
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {

            HttpHeaders requestHeaders = new HttpHeaders();
            String url = ServerConstantsUtils.getActiveServer();
            switch (getEntitiesMode) {
                case GET_BY_USER: {
                    url += "offers/users/" + UserModel.getInstance().getUser().getId();
                    if (limit != null & offset != null)
                        url += "?limit=" + limit + "&offset=" + offset;
                    else if (location != null)
                        url += "?lon=" + location.getLon() + "&lat=" + location.getLat();
                    setAuthorisationHeaders(requestHeaders);
                }
                break;

                case GET_RANDOM: {
                    url += "offers";
                    if (limit != null & offset != null)
                        url += "?limit=" + limit + "&offset=" + offset;
                    else if (location != null)
                        url += "?lon=" + location.getLon() + "&lat=" + location.getLat();
                }
                break;
            }

            List<MediaType> acceptableMediaTypes = new ArrayList<>();
            acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
            requestHeaders.setAccept(acceptableMediaTypes);
            HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);
            RestTemplate restTemplate = new RestTemplate(HttpRequestFactoryProviderImpl.getClientHttpRequestFactorySSLSupport(5000));
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<Offers> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Offers.class);
            final Offers offers = responseEntity.getBody();
            OffersModel.getInstance().setOffers(offers);
            return new RestResult(RestResult.ReturnType.SUCCESS);
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
            String message = getErrorMessage(e.getMessage());
            showToast(message);
            return new RestResult(RestResult.ReturnType.FAILED);
        }
    }
}
