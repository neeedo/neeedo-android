package neeedo.imimaprx.htw.de.neeedo.rest;

import android.util.Log;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import neeedo.imimaprx.htw.de.neeedo.entities.SingleUser;
import neeedo.imimaprx.htw.de.neeedo.entities.User;
import neeedo.imimaprx.htw.de.neeedo.factory.HttpRequestFactoryProviderImpl;
import neeedo.imimaprx.htw.de.neeedo.models.UserModel;

/**
 * Creates a new {@link neeedo.imimaprx.htw.de.neeedo.entities.User} based on the user in {@link neeedo.imimaprx.htw.de.neeedo.models.UserModel}.
 * User id has to be null or it will cause an http 400.
 */

public class HttpPostUserAsyncTask extends SuperHttpAsyncTask {
    @Override
    protected Object doInBackground(Object[] params) {
        try {

            final String url = ServerConstants.ACTIVE_SERVER + "users";

            UserModel userModel = UserModel.getInstance();
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<User> requestEntity = new HttpEntity<User>(userModel.getUser(), requestHeaders);


            RestTemplate restTemplate = new RestTemplate(HttpRequestFactoryProviderImpl.getClientHttpRequestFactorySSLSupport(9000));


            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            ResponseEntity<SingleUser> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, SingleUser.class);

            SingleUser singleUser = response.getBody();
            userModel.setUser(singleUser.getUser());

            return "Success";
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
            return "Failed";
        }
    }
}
