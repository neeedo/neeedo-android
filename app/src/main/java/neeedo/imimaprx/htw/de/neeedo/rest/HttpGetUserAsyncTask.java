package neeedo.imimaprx.htw.de.neeedo.rest;

import android.util.Log;

import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import neeedo.imimaprx.htw.de.neeedo.entities.SingleUser;
import neeedo.imimaprx.htw.de.neeedo.factory.HttpRequestFactoryProviderImpl;
import neeedo.imimaprx.htw.de.neeedo.models.ActiveUser;
import neeedo.imimaprx.htw.de.neeedo.models.UserModel;

/**
 * Collects User information by the given E-Mail in {@link neeedo.imimaprx.htw.de.neeedo.models.UserModel}.
 */
public class HttpGetUserAsyncTask extends SuperHttpAsyncTask {


    @Override
    protected Object doInBackground(Object[] params) {
        try {

            final ActiveUser activeUser = ActiveUser.getInstance();
            String email = activeUser.getUsername();
            String url = ServerConstants.ACTIVE_SERVER + "users/mail/";

            if (email == null || email == "") {
                return "Failed, no E-Mail is given";
            }

            url += email;

            HttpBasicAuthentication authentication = new HttpBasicAuthentication(email, activeUser.getUserPassword());

            HttpHeaders requestHeaders = new HttpHeaders();

            requestHeaders.setAuthorization(authentication);
            HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);

            RestTemplate restTemplate = new RestTemplate(HttpRequestFactoryProviderImpl.getClientHttpRequestFactorySSLSupport(5000));

            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            ResponseEntity<SingleUser> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
                    SingleUser.class);

            SingleUser singleUser = responseEntity.getBody();

            UserModel.getInstance().setUser(singleUser.getUser());

            return "Success"; // TODO use proper entities


        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
            return "Failed"; // TODO use proper entities
        }
    }
}
