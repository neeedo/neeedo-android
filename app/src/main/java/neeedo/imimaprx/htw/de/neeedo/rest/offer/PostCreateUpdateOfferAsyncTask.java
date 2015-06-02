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

import neeedo.imimaprx.htw.de.neeedo.entities.Offer;
import neeedo.imimaprx.htw.de.neeedo.entities.SingleOffer;
import neeedo.imimaprx.htw.de.neeedo.factory.HttpRequestFactoryProviderImpl;
import neeedo.imimaprx.htw.de.neeedo.models.ActiveUser;
import neeedo.imimaprx.htw.de.neeedo.models.OffersModel;
import neeedo.imimaprx.htw.de.neeedo.rest.BaseAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.utils.ServerConstantsUtils;

public class PostCreateUpdateOfferAsyncTask extends BaseAsyncTask {


    private SendMode sendMode;

    /**
     * Mode of sending UPDATE or CREATE is needed, available in {@link BaseAsyncTask}
     *
     * @param sendMode
     */
    public PostCreateUpdateOfferAsyncTask(SendMode sendMode) {
        if (sendMode == null) {
            throw new IllegalArgumentException("No mode is given.");
        }
        this.sendMode = sendMode;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            String url = ServerConstantsUtils.getActiveServer();
            OffersModel offersModel = OffersModel.getInstance();
            HttpMethod httpMethod = HttpMethod.POST;
            final ActiveUser activeUser = ActiveUser.getInstance();
            Offer postOffer = offersModel.getPostOffer();

            switch (sendMode) {
                case CREATE: {
                    url += "offers";
                }
                break;
                case UPDATE: {
                    url += "offers/" + postOffer.getId() + "/" + postOffer.getVersion();
                    //to avoid to include these in the json
                    postOffer.setId(null);
                    postOffer.setVersion(0);
                    httpMethod = HttpMethod.PUT;
                }
            }

            HttpBasicAuthentication authentication = new HttpBasicAuthentication(activeUser.getUsername(), activeUser.getUserPassword());
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setAuthorization(authentication);
            requestHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Offer> requestEntity = new HttpEntity<Offer>(postOffer, requestHeaders);
            RestTemplate restTemplate = new RestTemplate(HttpRequestFactoryProviderImpl.getClientHttpRequestFactorySSLSupport(9000));
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<SingleOffer> response = restTemplate.exchange(url, httpMethod, requestEntity, SingleOffer.class);
            SingleOffer singleOffer = response.getBody();
            offersModel.setSingleOffer(singleOffer);
            offersModel.getOffers().getOffers().add(singleOffer.getOffer());
            offersModel.setPostOffer(null);
            return ReturnTyp.SUCCESS;
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
            return ReturnTyp.FAILED;
        }
    }
}
