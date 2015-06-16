package neeedo.imimaprx.htw.de.neeedo.rest.image;

import android.util.Log;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.ResourceHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.File;

import neeedo.imimaprx.htw.de.neeedo.entities.Image;
import neeedo.imimaprx.htw.de.neeedo.factory.HttpRequestFactoryProviderImpl;
import neeedo.imimaprx.htw.de.neeedo.models.OffersModel;
import neeedo.imimaprx.htw.de.neeedo.rest.util.BaseAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.util.returntype.ImageUploadResult;
import neeedo.imimaprx.htw.de.neeedo.rest.util.returntype.RestResult;
import neeedo.imimaprx.htw.de.neeedo.utils.ServerConstantsUtils;

public class PostCreateImageAsyncTask extends BaseAsyncTask {

    private File photo;

    public PostCreateImageAsyncTask(File photo) {
        this.photo = photo;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            String url = ServerConstantsUtils.getActiveServer() + "images";
            HttpMethod httpMethod = HttpMethod.POST;

            MultiValueMap<String, Object> parts = new LinkedMultiValueMap<>();
            parts.add("name", "image");
            parts.add("filename", new FileSystemResource(photo));
            parts.add("Content-Type", "image/jpeg");

            HttpHeaders requestHeaders = new HttpHeaders();
            setAuthorisationHeaders(requestHeaders);
            requestHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);

            HttpMessageConverter<Object> jackson = new MappingJackson2HttpMessageConverter();
            HttpMessageConverter<Resource> resource = new ResourceHttpMessageConverter();
            FormHttpMessageConverter formHttpMessageConverter = new FormHttpMessageConverter();
            formHttpMessageConverter.addPartConverter(jackson);
            formHttpMessageConverter.addPartConverter(resource);

            HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<MultiValueMap<String, Object>>(parts, requestHeaders);

            RestTemplate restTemplate = new RestTemplate(HttpRequestFactoryProviderImpl.getClientHttpRequestFactorySSLSupport(9000));
            //restTemplate.getMessageConverters().add(new ProgressFormHttpMessageConverter());
            restTemplate.getMessageConverters().add(formHttpMessageConverter);
            ResponseEntity<Image> response = restTemplate.exchange(url, httpMethod, requestEntity, Image.class);

            Image image = response.getBody();

            return new ImageUploadResult(this, RestResult.ReturnType.SUCCESS, "id");
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
            return new RestResult(this, RestResult.ReturnType.FAILED);
        }
    }

    @Override
    protected void onPostExecute(Object o) {
        if (wasSuccessful(o)) {
            ImageUploadResult imageUploadResult = (ImageUploadResult) o;
            OffersModel.getInstance().getDraft().addSingleImageURL(imageUploadResult.getImageId());
            eventService.post(new ImageUploadCompleteEvent());
        }
    }
}
