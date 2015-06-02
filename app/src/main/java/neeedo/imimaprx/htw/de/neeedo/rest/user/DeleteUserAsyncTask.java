package neeedo.imimaprx.htw.de.neeedo.rest.user;

import android.util.Log;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import neeedo.imimaprx.htw.de.neeedo.entities.User;
import neeedo.imimaprx.htw.de.neeedo.factory.HttpRequestFactoryProviderImpl;
import neeedo.imimaprx.htw.de.neeedo.models.UserModel;
import neeedo.imimaprx.htw.de.neeedo.rest.BaseAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.utils.ServerConstantsUtils;


public class DeleteUserAsyncTask extends BaseAsyncTask {
    @Override
    protected Object doInBackground(Object[] params) {
        try {
            UserModel userModel = UserModel.getInstance();
            User user = userModel.getUser();
            final String url = ServerConstantsUtils.getActiveServer() + "usersd/" + user.getId() + "/" + user.getVersion();
            HttpHeaders requestHeaders = new HttpHeaders();
            requestHeaders.setContentType(MediaType.APPLICATION_JSON);
            RestTemplate restTemplate = new RestTemplate(HttpRequestFactoryProviderImpl.getClientHttpRequestFactorySSLSupport(4000));
            restTemplate.delete(url);
            userModel.setUser(null);
            return ReturnTyp.SUCCESS;
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
            return ReturnTyp.FAILED;
        }
    }
}
