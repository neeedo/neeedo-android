package neeedo.imimaprx.htw.de.neeedo.rest.outpan;

import android.util.Log;
import android.widget.EditText;

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
import java.util.Objects;

import neeedo.imimaprx.htw.de.neeedo.entities.Article;
import neeedo.imimaprx.htw.de.neeedo.factory.HttpRequestFactoryProviderImpl;
import neeedo.imimaprx.htw.de.neeedo.rest.util.BaseAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.util.returntype.OutpanResult;
import neeedo.imimaprx.htw.de.neeedo.rest.util.returntype.RestResult;
import neeedo.imimaprx.htw.de.neeedo.utils.ServerConstantsUtils;


public class GetOutpanByEANAsyncTask extends BaseAsyncTask {

    private final String ean;
    private final EditText etTags;

    public GetOutpanByEANAsyncTask(String ean, EditText etTags) {
        this.etTags = etTags;
        this.ean = ean;
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

            return new OutpanResult(this.getClass().getSimpleName(), RestResult.ReturnType.SUCCESS, tagsString);
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
            return new RestResult(this.getClass().getSimpleName(), RestResult.ReturnType.FAILED);
        }
    }

    @Override
    protected void onPostExecute(Object result) {
        if (result instanceof OutpanResult) {
            OutpanResult outpanResult = (OutpanResult) result;
            etTags.setText(outpanResult.getTags());
        } else if (result instanceof RestResult) {
            if (((RestResult) result).getResult() == RestResult.ReturnType.FAILED)
                //TODO get from R.strings
                etTags.setText("failed");
        }

    }

    private String getTagsFromArticle(Article article) {
        String tagsString = "";
        if (!(article.getName() == null)) {
            String[] tempTags = article.getName().split("[ ]+");
            for (String currentTag : tempTags) {
                tagsString += currentTag + " ";
            }
        }
        return tagsString;
    }
}
