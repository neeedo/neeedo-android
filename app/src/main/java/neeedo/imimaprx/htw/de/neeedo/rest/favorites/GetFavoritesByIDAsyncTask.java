package neeedo.imimaprx.htw.de.neeedo.rest.favorites;

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

import neeedo.imimaprx.htw.de.neeedo.entities.favorites.Favorites;
import neeedo.imimaprx.htw.de.neeedo.events.UserFavoritesLoadedEvent;
import neeedo.imimaprx.htw.de.neeedo.factory.HttpRequestFactoryProviderImpl;
import neeedo.imimaprx.htw.de.neeedo.models.FavoritesModel;
import neeedo.imimaprx.htw.de.neeedo.rest.util.BaseAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.util.returntype.RestResult;
import neeedo.imimaprx.htw.de.neeedo.utils.ServerConstantsUtils;

public class GetFavoritesByIDAsyncTask extends BaseAsyncTask {

    private String userId;

    public GetFavoritesByIDAsyncTask(String userId) {
        this.userId = userId;
    }


    @Override
    protected void onPostExecute(Object result) {
        if (result instanceof RestResult)
            eventService.post(new UserFavoritesLoadedEvent());
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            HttpHeaders requestHeaders = new HttpHeaders();
            String url = ServerConstantsUtils.getActiveServer() + "favorites/" + userId;
            setAuthorisationHeaders(requestHeaders);

            List<MediaType> acceptableMediaTypes = new ArrayList<>();
            acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
            requestHeaders.setAccept(acceptableMediaTypes);
            HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);
            RestTemplate restTemplate = new RestTemplate(HttpRequestFactoryProviderImpl.getClientHttpRequestFactorySSLSupport(5000));
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<Favorites> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Favorites.class);
            final Favorites favorites = responseEntity.getBody();
            FavoritesModel.getInstance().setFavorites(favorites);
            return new RestResult(RestResult.ReturnType.SUCCESS);
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
            if (!e.getMessage().contains("404")) {
                String message = getErrorMessage(e.getMessage());
                showToast(message);
            }
            return new RestResult(RestResult.ReturnType.FAILED);
        }
    }

}
