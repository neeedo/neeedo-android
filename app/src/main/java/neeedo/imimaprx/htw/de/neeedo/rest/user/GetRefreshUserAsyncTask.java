package neeedo.imimaprx.htw.de.neeedo.rest.user;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import neeedo.imimaprx.htw.de.neeedo.LoginActivity;
import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.user.SingleUser;
import neeedo.imimaprx.htw.de.neeedo.factory.HttpRequestFactoryProviderImpl;
import neeedo.imimaprx.htw.de.neeedo.models.ActiveUser;
import neeedo.imimaprx.htw.de.neeedo.models.ApplicationContextModel;
import neeedo.imimaprx.htw.de.neeedo.models.UserModel;
import neeedo.imimaprx.htw.de.neeedo.rest.util.returntype.RestResult;
import neeedo.imimaprx.htw.de.neeedo.utils.ServerConstantsUtils;

public class GetRefreshUserAsyncTask extends AsyncTask {

    private final String username;
    private final String password;
    private final LoginActivity loginActivity;

    final ActiveUser activeUser = ActiveUser.getInstance();

    public GetRefreshUserAsyncTask(String username, String password, LoginActivity loginActivity) {
        this.username = username;
        this.password = password;
        this.loginActivity = loginActivity;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            String url = ServerConstantsUtils.getActiveServer() + "users/mail/";
            url += username;
            HttpBasicAuthentication authentication = new HttpBasicAuthentication(username, password);
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setAuthorization(authentication);
            HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);
            RestTemplate restTemplate = new RestTemplate(HttpRequestFactoryProviderImpl.getClientHttpRequestFactorySSLSupport(5000));
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<SingleUser> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, SingleUser.class);
            SingleUser singleUser = responseEntity.getBody();
            activeUser.setUsername(username);
            activeUser.setUserPassword(password);
            activeUser.setUserId(singleUser.getUser().getId());
            UserModel.getInstance().setUser(singleUser.getUser());
            loginActivity.finish();
            return new RestResult(RestResult.ReturnType.SUCCESS);

        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
            final String message = getErrorMessage(e.getMessage());

            loginActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loginActivity.setWrongCredentials(message);
                }
            });
            return new RestResult(RestResult.ReturnType.FAILED);
        }
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);
    }

    public String getErrorMessage(String exception) {
        Context context = ApplicationContextModel.getInstance().getApplicationContext();
        String message = context.getString(R.string.exception_message_error_unkown);

        if (exception.contains("403")) {
            message = context.getString(R.string.login_error_text);
        }
        if (exception.contains("503")) {
            message = context.getString(R.string.exception_message_server_not_available);
        }
        if (exception.contains("409")) {
            message = context.getString(R.string.exception_message_email_already_in_use);
        }
        if (exception.contains("500")) {
            message = context.getString(R.string.exception_message_internal_server_error);
        }
        if (exception.contains("Unable to resolve host")) {
            message = context.getString(R.string.exception_message_no_internet);
        }
        return message;
    }

}
