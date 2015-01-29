package neeedo.imimaprx.htw.de.neeedo.rest;

import android.os.AsyncTask;

import neeedo.imimaprx.htw.de.neeedo.events.ServerResponseEvent;
import neeedo.imimaprx.htw.de.neeedo.service.EventService;

public abstract class SuperHttpAsyncTask extends AsyncTask {

    private EventService eventService = EventService.getInstance();

    @Override
    protected void onPostExecute(Object o) {
        eventService.post(new ServerResponseEvent());
    }
}
