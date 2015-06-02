package neeedo.imimaprx.htw.de.neeedo.rest.user;

import android.os.AsyncTask;
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
import neeedo.imimaprx.htw.de.neeedo.rest.BaseAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.utils.ServerConstantsUtils;

public class GetUserInfosAsyncTask extends AsyncTask {

    final ActiveUser activeUser = ActiveUser.getInstance();

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            String url = ServerConstantsUtils.getActiveServer() + "users/mail/";
            url += activeUser.getUsername();
            HttpBasicAuthentication authentication = new HttpBasicAuthentication(activeUser.getUsername(), activeUser.getUserPassword());
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setAuthorization(authentication);
            HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);
            RestTemplate restTemplate = new RestTemplate(HttpRequestFactoryProviderImpl.getClientHttpRequestFactorySSLSupport(5000));
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<SingleUser> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, SingleUser.class);
            SingleUser singleUser = responseEntity.getBody();
            UserModel.getInstance().setUser(singleUser.getUser());
            return BaseAsyncTask.ReturnTyp.SUCCESS;

        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
            activeUser.clearUserInformation();
            return BaseAsyncTask.ReturnTyp.FAILED;
        }
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }
}
