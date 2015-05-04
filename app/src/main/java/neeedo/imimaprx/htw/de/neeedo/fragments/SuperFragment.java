package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.internal.view.menu.ActionMenuItem;
import android.view.MenuItem;
import android.widget.Toast;

import neeedo.imimaprx.htw.de.neeedo.LoginActivity;
import neeedo.imimaprx.htw.de.neeedo.R;
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



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if( id == R.id.action_settings ){
            return super.onOptionsItemSelected(item);
        }else if (id == R.id.login) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);

            Toast.makeText(getActivity(), "Example action.", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
