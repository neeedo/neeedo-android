package neeedo.imimaprx.htw.de.neeedo.rest.completion;

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

import neeedo.imimaprx.htw.de.neeedo.entities.util.Tag;
import neeedo.imimaprx.htw.de.neeedo.events.GetSuggestionEvent;
import neeedo.imimaprx.htw.de.neeedo.factory.HttpRequestFactoryProviderImpl;
import neeedo.imimaprx.htw.de.neeedo.rest.util.BaseAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.util.returntype.RestResult;
import neeedo.imimaprx.htw.de.neeedo.rest.util.returntype.TagResult;
import neeedo.imimaprx.htw.de.neeedo.utils.ServerConstantsUtils;

public class GetCompletionAsyncTask extends BaseAsyncTask {
    private String text;
    private CompletionType completionType;
    private CharSequence charSequence;

    public GetCompletionAsyncTask(String text, CompletionType completionType, CharSequence charSequence) {
        this.text = text;
        this.completionType = completionType;
        this.charSequence = charSequence;
    }

    @Override
    protected void onPostExecute(Object result) {
        if (result instanceof TagResult)
            eventService.post(new GetSuggestionEvent((TagResult) result, completionType, charSequence));
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {


            HttpHeaders requestHeaders = new HttpHeaders();

            String url = ServerConstantsUtils.getActiveServer() + "completion/";
            switch (completionType) {
                case PHRASE: {
                    // URLEncoder doesn't seem to work like excepted
                    // String query = URLEncoder.encode(text, "UTF-8");
                    String query = text.replace(" ", "");
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

            return new TagResult(RestResult.ReturnType.SUCCESS, tag);
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
            String message = getErrorMessage(e.getMessage());
            showToast(message);

            return new RestResult(RestResult.ReturnType.FAILED);
        }
    }

}
