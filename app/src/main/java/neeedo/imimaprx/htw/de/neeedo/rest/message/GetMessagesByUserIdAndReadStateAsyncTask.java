package neeedo.imimaprx.htw.de.neeedo.rest.message;

import android.util.Log;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import neeedo.imimaprx.htw.de.neeedo.entities.user.User;
import neeedo.imimaprx.htw.de.neeedo.entities.user.Users;
import neeedo.imimaprx.htw.de.neeedo.events.UserMessageContactsLoadedEvent;
import neeedo.imimaprx.htw.de.neeedo.factory.HttpRequestFactoryProviderImpl;
import neeedo.imimaprx.htw.de.neeedo.models.MessagesModel;
import neeedo.imimaprx.htw.de.neeedo.rest.util.BaseAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.util.returntype.RestResult;
import neeedo.imimaprx.htw.de.neeedo.utils.ServerConstantsUtils;

public class GetMessagesByUserIdAndReadStateAsyncTask extends BaseAsyncTask {

    private String userId1;
    private boolean read;
    private boolean statusRequest;

    public GetMessagesByUserIdAndReadStateAsyncTask(String userId1, boolean read, boolean statusRequest) {
        this.userId1 = userId1;
        this.read = read;
        this.statusRequest = statusRequest;
    }

    @Override
    protected void onPostExecute(Object result) {
        if (result instanceof RestResult)
            eventService.post(new UserMessageContactsLoadedEvent());
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            HttpHeaders requestHeaders = new HttpHeaders();
            String url = ServerConstantsUtils.getActiveServer() + "conversations/" + userId1 + "?read=" + read;

            setAuthorisationHeaders(requestHeaders);

            List<MediaType> acceptableMediaTypes = new ArrayList<>();
            acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
            requestHeaders.setAccept(acceptableMediaTypes);
            HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);
            RestTemplate restTemplate = new RestTemplate(HttpRequestFactoryProviderImpl.getClientHttpRequestFactorySSLSupport(5000));
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<Users> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Users.class);
            final Users users = responseEntity.getBody();

            if (!read) {
                for (User user : users.getUsers()) {
                    user.setHasNewMessages(true);
                }
            }
            if (statusRequest) {
                checkForNewMessages(users);
            }

            MessagesModel messagesModel = MessagesModel.getInstance();
            messagesModel.appendUsers(users);

            return new RestResult(RestResult.ReturnType.SUCCESS);
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
            if (statusRequest) {
                String message = getErrorMessage(e.getMessage());
                showToast(message);
            }
            return new RestResult(RestResult.ReturnType.FAILED);
        }
    }

    private void checkForNewMessages(Users users) {
        ArrayList<User> list = users.getUsers();
        if (!list.isEmpty()) {
            for (User u : list)
                new GetMessagesAsyncTask(userId1, u.getId(), true).execute();
        }

    }

}
