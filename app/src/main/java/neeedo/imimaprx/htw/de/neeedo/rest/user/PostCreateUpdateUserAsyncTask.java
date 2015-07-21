package neeedo.imimaprx.htw.de.neeedo.rest.user;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import neeedo.imimaprx.htw.de.neeedo.LoginActivity;
import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.user.SingleUser;
import neeedo.imimaprx.htw.de.neeedo.entities.user.User;
import neeedo.imimaprx.htw.de.neeedo.factory.HttpRequestFactoryProviderImpl;
import neeedo.imimaprx.htw.de.neeedo.models.ActiveUser;
import neeedo.imimaprx.htw.de.neeedo.models.ApplicationContextModel;
import neeedo.imimaprx.htw.de.neeedo.models.UserModel;
import neeedo.imimaprx.htw.de.neeedo.rest.util.BaseAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.util.returntype.RestResult;
import neeedo.imimaprx.htw.de.neeedo.utils.ServerConstantsUtils;


public class PostCreateUpdateUserAsyncTask extends AsyncTask {

    private final LoginActivity loginActivity;
    private BaseAsyncTask.SendMode sendMode;
    private final ActiveUser activeUser = ActiveUser.getInstance();

    public PostCreateUpdateUserAsyncTask(LoginActivity loginActivity, BaseAsyncTask.SendMode sendMode) {
        if (sendMode == null) {
            throw new IllegalArgumentException("No mode is given.");
        }
        this.loginActivity = loginActivity;
        this.sendMode = sendMode;
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            String url = ServerConstantsUtils.getActiveServer();
            UserModel userModel = UserModel.getInstance();
            HttpMethod httpMethod = HttpMethod.POST;
            HttpHeaders requestHeaders = new HttpHeaders();
            User user = userModel.getUser();

            switch (sendMode) {
                case CREATE: {
                    url += "users";
                }
                break;
                case UPDATE: {
                    url += "users/" + user.getId() + "/" + user.getVersion();
                    //to avoid to include these in the json
                    user.setId(null);
                    user.setVersion(0);
                    httpMethod = HttpMethod.PUT;
                    //Update needs authentification
                    HttpBasicAuthentication authentication = new HttpBasicAuthentication(activeUser.getUsername(), activeUser.getUserPassword());
                    requestHeaders.setAuthorization(authentication);
                }
                break;
            }

            requestHeaders.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<User> requestEntity = new HttpEntity<User>(user, requestHeaders);
            RestTemplate restTemplate = new RestTemplate(HttpRequestFactoryProviderImpl.getClientHttpRequestFactorySSLSupport(9000));
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<SingleUser> response = restTemplate.exchange(url, httpMethod, requestEntity, SingleUser.class);

            SingleUser singleUser = response.getBody();
            userModel.putCurrentLoginInformationInActiveUser();
            userModel.setUser(singleUser.getUser());

            loginActivity.finish();
            return new RestResult(RestResult.ReturnType.SUCCESS);

        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
            final String message = getErrorMessage(e.getMessage());

            //activeUser.clearUserInformation();
            loginActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loginActivity.setErrorState(message);
                }
            });
            return new RestResult(RestResult.ReturnType.FAILED);
        }
    }

    public String getErrorMessage(String exception) {
        Context context = ApplicationContextModel.getInstance().getApplicationContext();
        String message = context.getString(R.string.exception_message_error_unkown);


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

