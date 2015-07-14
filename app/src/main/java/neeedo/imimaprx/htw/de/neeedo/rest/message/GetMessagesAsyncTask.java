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

import neeedo.imimaprx.htw.de.neeedo.entities.message.Message;
import neeedo.imimaprx.htw.de.neeedo.entities.message.Messages;
import neeedo.imimaprx.htw.de.neeedo.events.MessagesLoadedEvent;
import neeedo.imimaprx.htw.de.neeedo.factory.HttpRequestFactoryProviderImpl;
import neeedo.imimaprx.htw.de.neeedo.models.MessagesModel;
import neeedo.imimaprx.htw.de.neeedo.rest.util.BaseAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.util.returntype.RestResult;
import neeedo.imimaprx.htw.de.neeedo.utils.ServerConstantsUtils;

public class GetMessagesAsyncTask extends BaseAsyncTask {

    private String userId1;
    private String userId2;
    private boolean statusRequest = false;


    public GetMessagesAsyncTask(String userId1, String userId2) {
        this.userId1 = userId1;
        this.userId2 = userId2;
    }

    public GetMessagesAsyncTask(String userId1, String userId2, boolean statusRequest) {
        this.userId1 = userId1;
        this.userId2 = userId2;
        this.statusRequest = statusRequest;
    }

    @Override
    protected void onPostExecute(Object result) {
        if (result instanceof RestResult)
            eventService.post(new MessagesLoadedEvent());
    }


    @Override
    protected Object doInBackground(Object[] params) {
        try {
            HttpHeaders requestHeaders = new HttpHeaders();
            String url = ServerConstantsUtils.getActiveServer() + "/messages/" + userId1 + "/" + userId2;

            setAuthorisationHeaders(requestHeaders);

            List<MediaType> acceptableMediaTypes = new ArrayList<>();
            acceptableMediaTypes.add(MediaType.APPLICATION_JSON);
            requestHeaders.setAccept(acceptableMediaTypes);
            HttpEntity<?> requestEntity = new HttpEntity<Object>(requestHeaders);
            RestTemplate restTemplate = new RestTemplate(HttpRequestFactoryProviderImpl.getClientHttpRequestFactorySSLSupport(5000));
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<Messages> responseEntity = restTemplate.exchange(url, HttpMethod.GET, requestEntity, Messages.class);
            final Messages messages = responseEntity.getBody();

            if (statusRequest) {
                checkMessageSenderState(messages);
            } else {
                MessagesModel.getInstance().setMessages(messages);
            }

            return new RestResult(RestResult.ReturnType.SUCCESS);
        } catch (Exception e) {
            Log.e(this.getClass().getSimpleName(), e.getMessage(), e);
            String message = getErrorMessage(e.getMessage());
            showToast(message);
            return new RestResult(RestResult.ReturnType.FAILED);
        }
    }

    private void checkMessageSenderState(Messages messages) {

        ArrayList<Message> list = messages.getMessages();

        int counter = 0;
        for (Message m : list) {
            if (m.getRecipient().getId().equals(userId1) && !m.isRead()) {
                counter++;
            }
        }
        if (counter > 0) {
            MessagesModel.getInstance().increaseMessageCounter(counter);
        }

    }

}
