package neeedo.imimaprx.htw.de.neeedo.rest.image;

import android.util.Log;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.util.List;

import neeedo.imimaprx.htw.de.neeedo.entities.Image;
import neeedo.imimaprx.htw.de.neeedo.entities.Offer;
import neeedo.imimaprx.htw.de.neeedo.entities.SingleOffer;
import neeedo.imimaprx.htw.de.neeedo.factory.HttpRequestFactoryProviderImpl;
import neeedo.imimaprx.htw.de.neeedo.models.OffersModel;
import neeedo.imimaprx.htw.de.neeedo.rest.util.BaseAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.util.returntype.RestResult;
import neeedo.imimaprx.htw.de.neeedo.utils.ServerConstantsUtils;

public class PostCreateImageAsyncTask extends BaseAsyncTask {

    private File photo;
    private List<Image> images;

    public PostCreateImageAsyncTask(File photo, List<Image> images) {
        this.photo = photo;
        this.images = images;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            String url = ServerConstantsUtils.getActiveServer()+"images/";
            HttpMethod httpMethod = HttpMethod.POST;

            HttpHeaders requestHeaders = new HttpHeaders();
            setAuthorisationHeaders(requestHeaders);
            requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
            HttpEntity<File> requestEntity = new HttpEntity<File>(photo, requestHeaders);

            RestTemplate restTemplate = new RestTemplate(HttpRequestFactoryProviderImpl.getClientHttpRequestFactorySSLSupport(9000));
            //restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<Image> response = restTemplate.exchange(url, httpMethod, requestEntity, Image.class);

            Image image = response.getBody();

            images.add(image);

            return new RestResult(this.getClass().getSimpleName(), RestResult.ReturnType.SUCCESS);
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
            return new RestResult(this.getClass().getSimpleName(), RestResult.ReturnType.FAILED);
        }
    }
}
