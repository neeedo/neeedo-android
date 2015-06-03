package neeedo.imimaprx.htw.de.neeedo.rest;

import android.os.AsyncTask;

import neeedo.imimaprx.htw.de.neeedo.events.ServerResponseEvent;
import neeedo.imimaprx.htw.de.neeedo.service.EventService;

public abstract class BaseAsyncTask extends AsyncTask {

    public enum GetEntitiesMode {
        GET_BY_USER, GET_RANDOM
    }

    public enum EntityType {
        DEMAND, OFFER, USER
    }

    public enum ReturnType {
        SUCCESS, FAILED
    }

    public enum SendMode {
        CREATE, UPDATE
    }

    protected EventService eventService = EventService.getInstance();

    @Override
    protected void onPostExecute(Object o) {
        eventService.post(new ServerResponseEvent());
    }
}
