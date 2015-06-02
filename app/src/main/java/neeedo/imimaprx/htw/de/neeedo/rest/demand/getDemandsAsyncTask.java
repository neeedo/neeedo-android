package neeedo.imimaprx.htw.de.neeedo.rest.demand;

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
import neeedo.imimaprx.htw.de.neeedo.factory.HttpRequestFactoryProviderImpl;
import neeedo.imimaprx.htw.de.neeedo.models.ActiveUser;
import neeedo.imimaprx.htw.de.neeedo.models.DemandsModel;
import neeedo.imimaprx.htw.de.neeedo.models.UserModel;
import neeedo.imimaprx.htw.de.neeedo.rest.BaseAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.utils.ServerConstantsUtils;

public class GetDemandsAsyncTask extends BaseAsyncTask {


    private GetEntitiesMode getEntitiesMode;

    /**
     * Get the constant from enum in {@link BaseAsyncTask}
     *
     * @param getEntitiesMode
     */
    public GetDemandsAsyncTask(GetEntitiesMode getEntitiesMode) {
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
            switch (getEntitiesMode) {
                case GET_BY_USER: {
                    url += "demands/users/" + UserModel.getInstance().getUser().getId();
                }
                break;
                case GET_RANDOM: {
                    url += "demands";
                }
                break;
            }
            final ActiveUser activeUser = ActiveUser.getInstance();
            HttpBasicAuthentication authentication = new HttpBasicAuthentication(activeUser.getUsername(), activeUser.getUserPassword());
            requestHeaders.setAuthorization(authentication);
            List<MediaType> acceptableMediaTypes = new ArrayList<>();
            acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
            requestHeaders.setAccept(acceptableMediaTypes);
            HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);
            RestTemplate restTemplate = new RestTemplate(HttpRequestFactoryProviderImpl.getClientHttpRequestFactorySSLSupport(5000));
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<Demands> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Demands.class);
            final Demands demands = responseEntity.getBody();
            DemandsModel.getInstance().setDemands(demands);
            return ReturnTyp.SUCCESS;
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
            return ReturnTyp.FAILED;
        }
    }

}
