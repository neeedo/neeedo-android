package neeedo.imimaprx.htw.de.neeedo.rest.offer;

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

import neeedo.imimaprx.htw.de.neeedo.entities.Offers;
import neeedo.imimaprx.htw.de.neeedo.factory.HttpRequestFactoryProviderImpl;
import neeedo.imimaprx.htw.de.neeedo.models.ActiveUser;
import neeedo.imimaprx.htw.de.neeedo.models.OffersModel;
import neeedo.imimaprx.htw.de.neeedo.models.UserModel;
import neeedo.imimaprx.htw.de.neeedo.rest.BaseAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.utils.ServerConstantsUtils;

public class GetOffersAsyncTask extends BaseAsyncTask {

    private GetEntitiesMode getEntitiesMode;

    /**
     * Get the constant from enum in {@link BaseAsyncTask}
     *
     * @param getEntitiesMode
     */
    public GetOffersAsyncTask(GetEntitiesMode getEntitiesMode) {

        if (getEntitiesMode == null) {
            throw new IllegalArgumentException("No Mode given.");
        }

        this.getEntitiesMode = getEntitiesMode;
    }


    @Override
    protected Object doInBackground(Object[] params) {
        try {


            HttpHeaders requestHeaders = new HttpHeaders();
            String url = ServerConstantsUtils.getActiveServer();

            //Case get all offers to the user ID in ActiveUser
            if (getEntitiesMode == GetEntitiesMode.GET_BY_USER) {
                url += "offers/users/" + UserModel.getInstance().getUser().getId();
                final ActiveUser activeUser = ActiveUser.getInstance();
                HttpBasicAuthentication authentication = new HttpBasicAuthentication(activeUser.getUsername(), activeUser.getUserPassword());
                requestHeaders.setAuthorization(authentication);
            }

            //Get Random pool of offers, no authentication needed
            if (getEntitiesMode == GetEntitiesMode.GET_RANDOM) {
                url += "offers";
            }

            List<MediaType> acceptableMediaTypes = new ArrayList<>();
            acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
            requestHeaders.setAccept(acceptableMediaTypes);

            HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);

            RestTemplate restTemplate = new RestTemplate(HttpRequestFactoryProviderImpl.getClientHttpRequestFactorySSLSupport(5000));

            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            ResponseEntity<Offers> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
                    Offers.class);

            final Offers offers = responseEntity.getBody();

            OffersModel.getInstance().setOffers(offers);


            return ReturnTyp.SUCCESS;
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
            return ReturnTyp.FAILED;
        }
    }
}
