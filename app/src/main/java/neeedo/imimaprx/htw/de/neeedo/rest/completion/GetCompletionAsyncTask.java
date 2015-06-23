package neeedo.imimaprx.htw.de.neeedo.rest.completion;

import android.util.Log;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import neeedo.imimaprx.htw.de.neeedo.entities.util.Tag;
import neeedo.imimaprx.htw.de.neeedo.factory.HttpRequestFactoryProviderImpl;
import neeedo.imimaprx.htw.de.neeedo.rest.util.BaseAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.util.returntype.RestResult;
import neeedo.imimaprx.htw.de.neeedo.rest.util.returntype.TagResult;
import neeedo.imimaprx.htw.de.neeedo.utils.ServerConstantsUtils;

public class GetCompletionAsyncTask extends BaseAsyncTask {

    private String text;
    private CompletionType completionType;

    public GetCompletionAsyncTask(String text, CompletionType completionType) {
        this.text = text;
        this.completionType = completionType;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {


            HttpHeaders requestHeaders = new HttpHeaders();

            String url = ServerConstantsUtils.getActiveServer() + "completion/";
            switch (completionType) {
                case SUGGEST: {
                    String query = URLEncoder.encode(text, "utf-8");
                    url += "suggest/" + query;
                }
                break;
                case TAG: {
                    url += "tag/" + text;
                }
                break;
            }

            List<MediaType> acceptableMediaTypes = new ArrayList<>();
            acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
            requestHeaders.setAccept(acceptableMediaTypes);
            HttpEntity requestEntity = new HttpEntity(requestHeaders);
            RestTemplate restTemplate = new RestTemplate(HttpRequestFactoryProviderImpl.getClientHttpRequestFactorySSLSupport(5000));
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<Tag> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Tag.class);
            Tag tag = responseEntity.getBody();

            return new TagResult(this.getClass().getSimpleName(), RestResult.ReturnType.SUCCESS, tag);
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
            return new RestResult(this.getClass().getSimpleName(), RestResult.ReturnType.FAILED);
        }
    }

}
