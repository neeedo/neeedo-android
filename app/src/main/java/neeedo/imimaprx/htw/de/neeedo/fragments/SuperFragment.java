package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.Fragment;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import neeedo.imimaprx.htw.de.neeedo.LoginActivity;
import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.service.EventService;
import neeedo.imimaprx.htw.de.neeedo.utils.ImageUtils;

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
