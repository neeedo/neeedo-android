package neeedo.imimaprx.htw.de.neeedo.rest.favorites;

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

import neeedo.imimaprx.htw.de.neeedo.entities.favorites.FavoritMin;
import neeedo.imimaprx.htw.de.neeedo.factory.HttpRequestFactoryProviderImpl;
import neeedo.imimaprx.htw.de.neeedo.rest.util.BaseAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.util.returntype.RestResult;
import neeedo.imimaprx.htw.de.neeedo.utils.ServerConstantsUtils;


public class CreateDeleteFavoritAsyncTask extends BaseAsyncTask {

    private FavoritMin favoritMin;
    private FavoritOption favoritOption;

    public CreateDeleteFavoritAsyncTask(FavoritOption favoritOption, FavoritMin favoritMin) {
        this.favoritMin = favoritMin;
        this.favoritOption = favoritOption;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            String url = ServerConstantsUtils.getActiveServer() + "/favorites";
            HttpMethod httpMethod = null;

            switch (favoritOption) {
                case CREATE:
                    httpMethod = HttpMethod.POST;
                    break;
                case DELETE:
                    httpMethod = HttpMethod.DELETE;
                    break;
            }

            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.APPLICATION_JSON);

            setAuthorisationHeaders(requestHeaders);
            List<MediaType> acceptableMediaTypes = new ArrayList<>();
            acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
            requestHeaders.setAccept(acceptableMediaTypes);

            HttpEntity<FavoritMin> requestEntity = new HttpEntity<FavoritMin>(favoritMin, requestHeaders);
            RestTemplate restTemplate = new RestTemplate(HttpRequestFactoryProviderImpl.getClientHttpRequestFactorySSLSupport(9000));
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<FavoritMin> response = restTemplate.exchange(url, httpMethod, requestEntity, FavoritMin.class);

            FavoritMin favoritMin = response.getBody();
            //TODO something with the response body

            return new RestResult( RestResult.ReturnType.SUCCESS);
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage(), e);

            return new RestResult( RestResult.ReturnType.FAILED);
        }
    }
}
