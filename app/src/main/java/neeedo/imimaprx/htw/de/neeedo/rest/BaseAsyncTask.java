package neeedo.imimaprx.htw.de.neeedo.rest;


import android.os.AsyncTask;

import neeedo.imimaprx.htw.de.neeedo.events.ServerResponseEvent;
import neeedo.imimaprx.htw.de.neeedo.service.EventService;

public abstract class BaseAsyncTask extends AsyncTask {

    public enum GetEntitiesMode {
        GET_BY_USER, GET_RANDOM
    }

    public enum EntityTyp {
        DEMAND, OFFER, USER
    }

    public enum ReturnTyp {
        SUCCESS, FAILED
    }


    private EventService eventService = EventService.getInstance();

    @Override
    protected void onPostExecute(Object o) {
        eventService.post(new ServerResponseEvent());
    }


}
