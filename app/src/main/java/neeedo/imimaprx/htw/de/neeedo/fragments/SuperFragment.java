package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import neeedo.imimaprx.htw.de.neeedo.LoginActivity;
import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.service.EventService;
import neeedo.imimaprx.htw.de.neeedo.vo.RequestCodes;

public class SuperFragment extends Fragment {

    protected EventService eventService = EventService.getInstance();

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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK || requestCode == RequestCodes.LOGIN_REQUEST_CODE) {
            Toast.makeText(getActivity(), "login returned!", Toast.LENGTH_SHORT).show();
            setLoginButtonState();
        }
    }

    private void setLoginButtonState() {
        View actionBarLogin = getActivity().findViewById(R.id.action_bar_login);
        View actionBarLoggedIn = getActivity().findViewById(R.id.action_bar_logged_in);
        if (actionBarLoggedIn == null || actionBarLogin == null)
            return;
        actionBarLogin.setVisibility(View.GONE);
        actionBarLoggedIn.setVisibility(View.VISIBLE);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return super.onOptionsItemSelected(item);
        } else if (id == R.id.action_bar_login) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivityForResult(intent, RequestCodes.LOGIN_REQUEST_CODE);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void redirectToListFragment() {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = new ListDemandsFragment();
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }
}
