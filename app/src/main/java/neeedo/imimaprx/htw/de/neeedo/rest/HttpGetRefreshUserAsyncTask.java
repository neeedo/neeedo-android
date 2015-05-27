package neeedo.imimaprx.htw.de.neeedo.rest;

import android.os.AsyncTask;
import android.util.Log;

import com.splunk.mint.Mint;

import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import neeedo.imimaprx.htw.de.neeedo.LoginActivity;
import neeedo.imimaprx.htw.de.neeedo.entities.SingleUser;
import neeedo.imimaprx.htw.de.neeedo.factory.HttpRequestFactoryProviderImpl;
import neeedo.imimaprx.htw.de.neeedo.models.ActiveUser;
import neeedo.imimaprx.htw.de.neeedo.models.UserModel;

public class HttpGetRefreshUserAsyncTask extends AsyncTask {

    private final String username;
    private final String password;
    private final LoginActivity loginActivity;

    final ActiveUser activeUser = ActiveUser.getInstance();

    public HttpGetRefreshUserAsyncTask(String username, String password, LoginActivity loginActivity) {
        this.username = username;
        this.password = password;
        this.loginActivity = loginActivity;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            String url = ServerConstants.getActiveServer() + "users/mail/";

            url += username;

            HttpBasicAuthentication authentication = new HttpBasicAuthentication(username, password);

            HttpHeaders requestHeaders = new HttpHeaders();

            requestHeaders.setAuthorization(authentication);
            HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);

            RestTemplate restTemplate = new RestTemplate(HttpRequestFactoryProviderImpl.getClientHttpRequestFactorySSLSupport(5000));

            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

            ResponseEntity<SingleUser> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
                    SingleUser.class);

            SingleUser singleUser = responseEntity.getBody();

            activeUser.setUsername(username);
            activeUser.setUserPassword(password);

            UserModel.getInstance().setUser(singleUser.getUser());

            loginActivity.finish();

            return true;

        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage(), e);

            activeUser.clearUserinformation();

            loginActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loginActivity.setWrongCredentials();
                }
            });

            return false;
        }
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }
}
