package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.support.v4.app.Fragment;

import neeedo.imimaprx.htw.de.neeedo.service.EventService;

public class SuperFragment extends Fragment {

    private EventService eventService = EventService.getInstance();

    @Override
    public void onResume() {
        super.onResume();
        eventService.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        eventService.unregister(this);
    }
}
