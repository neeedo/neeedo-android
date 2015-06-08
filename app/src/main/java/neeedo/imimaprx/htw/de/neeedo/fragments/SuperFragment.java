package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import neeedo.imimaprx.htw.de.neeedo.LoginActivity;
import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.events.UserStateChangedEvent;
import neeedo.imimaprx.htw.de.neeedo.models.ActiveUser;
import neeedo.imimaprx.htw.de.neeedo.service.EventService;
import neeedo.imimaprx.htw.de.neeedo.vo.RequestCodes;

public class SuperFragment extends Fragment {

    protected EventService eventService = EventService.getInstance();

    private MenuItem actionBarLogout;
    private MenuItem actionBarLogin;

    private Menu menu;

    //TODO WHAT!?
    protected static final String OFFERS_LIST_KEY = "offers";
    protected static final String DEMANDS_LIST_KEY = "demands";

    @Override
    public void onResume() {
        super.onResume();
        eventService.register(this);
        setLoginButtonState();
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
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        inflater.inflate(R.menu.main, menu);

        this.menu = menu;

        setLoginButtonState();
    }

    private void setLoginButtonState() {
        if (menu == null)
            return;

        boolean isUserLoggedIn = ActiveUser.getInstance().hasActiveUser();

        actionBarLogin = menu.findItem(R.id.action_bar_login);
        actionBarLogout = menu.findItem(R.id.action_bar_logout);

        if (isUserLoggedIn) {
            actionBarLogin.setVisible(false);
            actionBarLogout.setVisible(true);
        } else {
            actionBarLogin.setVisible(true);
            actionBarLogout.setVisible(false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            //do nothing for now
        } else if (id == R.id.action_bar_login) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivityForResult(intent, RequestCodes.LOGIN_REQUEST_CODE);
            return true;
        } else if (id == R.id.action_bar_logout) {
            ActiveUser.getInstance().clearUserInformation();
            Toast.makeText(getActivity(), "Logout finished.", Toast.LENGTH_SHORT).show();
            setLoginButtonState();
        }

        return super.onOptionsItemSelected(item);
    }

    public void redirectToListFragment(String type) {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = null;

        // TODO redirect to detail view instead of list

        if (type.equals(DEMANDS_LIST_KEY)) {
            fragment = new ListDemandsFragment();
        } else if (type.equals(OFFERS_LIST_KEY)) {
            fragment = new ListOffersFragment();
        } else {
            return;
        }

        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    @Subscribe
    public void handleUserStateChanged(UserStateChangedEvent event) {
        setLoginButtonState();
    }
}
