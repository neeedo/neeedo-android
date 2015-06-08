package neeedo.imimaprx.htw.de.neeedo.rest.util;

import android.os.AsyncTask;

import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpHeaders;

import neeedo.imimaprx.htw.de.neeedo.events.ServerResponseEvent;
import neeedo.imimaprx.htw.de.neeedo.models.ActiveUser;
import neeedo.imimaprx.htw.de.neeedo.service.EventService;

public abstract class BaseAsyncTask extends AsyncTask {

    public enum GetEntitiesMode {
        GET_BY_USER, GET_RANDOM
    }


    public enum SendMode {
        CREATE, UPDATE
    }

    protected EventService eventService = EventService.getInstance();

    @Override
    protected void onPostExecute(Object o) {
        eventService.post(new ServerResponseEvent());
    }

    protected void setAuthorisationHeaders(HttpHeaders requestHeaders){
        final ActiveUser activeUser = ActiveUser.getInstance();
        HttpBasicAuthentication authentication = new HttpBasicAuthentication(activeUser.getUsername(), activeUser.getUserPassword());
        requestHeaders.setAuthorization(authentication);
    }

}
