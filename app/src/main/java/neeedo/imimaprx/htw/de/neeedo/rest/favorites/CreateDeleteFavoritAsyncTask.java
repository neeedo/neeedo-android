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

import neeedo.imimaprx.htw.de.neeedo.entities.favorites.Favorite;
import neeedo.imimaprx.htw.de.neeedo.entities.favorites.SingleFavorite;
import neeedo.imimaprx.htw.de.neeedo.events.FavoritesActionEvent;
import neeedo.imimaprx.htw.de.neeedo.factory.HttpRequestFactoryProviderImpl;
import neeedo.imimaprx.htw.de.neeedo.rest.util.BaseAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.util.returntype.RestResult;
import neeedo.imimaprx.htw.de.neeedo.utils.ServerConstantsUtils;


public class CreateDeleteFavoritAsyncTask extends BaseAsyncTask {

    private Favorite favorite;
    private FavoritOption favoritOption;

    public CreateDeleteFavoritAsyncTask(FavoritOption favoritOption, Favorite favorite) {
        this.favorite = favorite;
        this.favoritOption = favoritOption;
    }

    @Override
    protected void onPostExecute(Object result) {
        if (result instanceof RestResult)
            eventService.post(new FavoritesActionEvent());
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            String url = ServerConstantsUtils.getActiveServer() + "favorites";
            HttpMethod httpMethod = null;
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.APPLICATION_JSON);

            setAuthorisationHeaders(requestHeaders);
            List<MediaType> acceptableMediaTypes = new ArrayList<>();
            acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
            requestHeaders.setAccept(acceptableMediaTypes);
            HttpEntity requestEntity = null;
            RestTemplate restTemplate = new RestTemplate(HttpRequestFactoryProviderImpl.getClientHttpRequestFactorySSLSupport(9000));
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());


            switch (favoritOption) {
                case CREATE:
                    httpMethod = HttpMethod.POST;
                    requestEntity = new HttpEntity<Favorite>(favorite, requestHeaders);
                    break;

                case DELETE:
                    httpMethod = HttpMethod.DELETE;
                    url += "/" + favorite.getUserId() + "/" + favorite.getOfferId();
                    requestEntity = new HttpEntity(requestHeaders);
                    break;
            }

            ResponseEntity<SingleFavorite> response = restTemplate.exchange(url, httpMethod, requestEntity, SingleFavorite.class);

            SingleFavorite favorite = response.getBody();
            //TODO something with the response body

            return new RestResult(RestResult.ReturnType.SUCCESS);
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
            String message = getErrorMessage(e.getMessage());
            showToast(message);
            return new RestResult(RestResult.ReturnType.FAILED);
        }
    }
}
