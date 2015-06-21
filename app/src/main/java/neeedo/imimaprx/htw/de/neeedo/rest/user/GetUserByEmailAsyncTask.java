package neeedo.imimaprx.htw.de.neeedo.rest.user;

import android.util.Log;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import neeedo.imimaprx.htw.de.neeedo.entities.user.SingleUser;
import neeedo.imimaprx.htw.de.neeedo.events.UserStateChangedEvent;
import neeedo.imimaprx.htw.de.neeedo.factory.HttpRequestFactoryProviderImpl;
import neeedo.imimaprx.htw.de.neeedo.models.ActiveUser;
import neeedo.imimaprx.htw.de.neeedo.models.UserModel;
import neeedo.imimaprx.htw.de.neeedo.rest.util.BaseAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.util.returntype.RestResult;
import neeedo.imimaprx.htw.de.neeedo.utils.ServerConstantsUtils;

public class GetUserByEmailAsyncTask extends BaseAsyncTask {

    final ActiveUser activeUser = ActiveUser.getInstance();

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            String url = ServerConstantsUtils.getActiveServer() + "users/mail/";
            url += activeUser.getUsername();

            HttpHeaders requestHeaders = new HttpHeaders();
            setAuthorisationHeaders(requestHeaders);
            HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);
            RestTemplate restTemplate = new RestTemplate(HttpRequestFactoryProviderImpl.getClientHttpRequestFactorySSLSupport(5000));
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<SingleUser> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, SingleUser.class);
            SingleUser singleUser = responseEntity.getBody();
            UserModel.getInstance().setUser(singleUser.getUser());

            return new RestResult(this.getClass().getSimpleName(), RestResult.ReturnType.SUCCESS);

        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
            activeUser.clearUserInformation();
            return new RestResult(this.getClass().getSimpleName(), RestResult.ReturnType.FAILED);
        }
    }

    @Override
    protected void onPostExecute(Object o) {
        eventService.post(new UserStateChangedEvent());
    }
}
