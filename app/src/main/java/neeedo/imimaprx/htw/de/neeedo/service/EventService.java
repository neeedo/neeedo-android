package neeedo.imimaprx.htw.de.neeedo.service;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

public class EventService {

    private Bus bus = new Bus(ThreadEnforcer.ANY);

    //singleton
    private static EventService instance;

    public static EventService getInstance() {
        if (EventService.instance == null) {
            EventService.instance = new EventService();
        }
        return EventService.instance;
    }

    public void post(Object event) {
        bus.post(event);
    }

    public void register(java.lang.Object object) {
        bus.register(object);
    }

    public void unregister(java.lang.Object object) {
        bus.unregister(object);
    }
}
