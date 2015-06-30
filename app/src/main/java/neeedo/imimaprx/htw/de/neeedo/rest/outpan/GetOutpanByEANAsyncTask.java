package neeedo.imimaprx.htw.de.neeedo.rest.outpan;

import android.app.Activity;
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

import neeedo.imimaprx.htw.de.neeedo.entities.util.Article;
import neeedo.imimaprx.htw.de.neeedo.events.NewEanTagsReceivedEvent;
import neeedo.imimaprx.htw.de.neeedo.factory.HttpRequestFactoryProviderImpl;
import neeedo.imimaprx.htw.de.neeedo.rest.util.BaseAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.util.returntype.OutpanResult;
import neeedo.imimaprx.htw.de.neeedo.rest.util.returntype.RestResult;
import neeedo.imimaprx.htw.de.neeedo.service.EventService;
import neeedo.imimaprx.htw.de.neeedo.utils.ServerConstantsUtils;


public class GetOutpanByEANAsyncTask extends BaseAsyncTask {

    private EventService eventService = EventService.getInstance();
    private final String ean;
    private final Activity activity;

    public GetOutpanByEANAsyncTask(String ean, Activity activity) {

        this.ean = ean;
        this.activity = activity;
    }

    protected Object doInBackground(Object[] params) {
        try {
            final String url = ServerConstantsUtils.getOutpanServer() + ean;
            HttpBasicAuthentication authentication = new HttpBasicAuthentication("e9e42347c7677fede70a1761c1737de1:", "");
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setAuthorization(authentication);
            List<MediaType> acceptableMediaTypes = new ArrayList<>();
            acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
            requestHeaders.setAccept(acceptableMediaTypes);
            HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);
            RestTemplate restTemplate = new RestTemplate(HttpRequestFactoryProviderImpl.getClientHttpRequestFactorySSLSupport(5000));
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<Article> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Article.class);

            Article article = responseEntity.getBody();
            String tagsString = getTagsFromArticle(article);

            return new OutpanResult(RestResult.ReturnType.SUCCESS, tagsString);
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
            return new RestResult(RestResult.ReturnType.FAILED);
        }
    }

    @Override
    protected void onPostExecute(Object result) {
        if (result instanceof OutpanResult)
            eventService.post(new NewEanTagsReceivedEvent((OutpanResult) result));
    }

    private String getTagsFromArticle(Article article) {
        String tagsString = "";
        if (!(article.getName() == null)) {
            String[] tempTags = article.getName().split("[ ]+");
            for (String currentTag : tempTags) {
                tagsString += currentTag + ", ";
            }
            tagsString = tagsString.substring(0, tagsString.length() - 2);
        } else {
            throw new IllegalArgumentException("Name tag is empty");
        }
        return tagsString;
    }
}
