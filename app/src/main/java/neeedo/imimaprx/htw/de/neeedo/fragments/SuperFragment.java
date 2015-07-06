package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import neeedo.imimaprx.htw.de.neeedo.rest.util.BaseAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.service.EventService;
import neeedo.imimaprx.htw.de.neeedo.vo.RequestCodes;

public class SuperFragment extends Fragment {

    protected EventService eventService = EventService.getInstance();


    private MenuItem actionBarLogout;

    private MenuItem actionBarLogin;

    private Menu menu;

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
        if (requestCode == RequestCodes.LOGIN_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
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

    public void redirectToFragment(Class type, Integer navItem) {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = null;

        try {
            fragment = (Fragment) type.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        String backstackItem = null;
        if(navItem != null) {
            backstackItem = String.valueOf(navItem);
        }

        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(backstackItem)
                .commit();
    }

    public void redirectToFragment(Class type) {
        redirectToFragment(type, null);
    }

    @Subscribe
    public void handleUserStateChanged(UserStateChangedEvent event) {
        setLoginButtonState();
    }

    public static class ConfirmDialogFragment extends android.support.v4.app.DialogFragment {
        private static ConfirmDialogFragment dialog;
        private static BaseAsyncTask task;
        private static Class fragmentRedirect;
        private static String message;

        public static ConfirmDialogFragment newInstance(BaseAsyncTask asyncTask, Class fragmentType, String dialogMessage) {
            dialog = new ConfirmDialogFragment();

            task = asyncTask;
            fragmentRedirect = fragmentType;
            message = dialogMessage;

            return dialog;
        }

        public Dialog onCreateDialog(Bundle savedInstanceState) {
            return new ProgressDialog.Builder(getActivity()).
                    setMessage(message).
                    setPositiveButton(R.string.dialog_yes,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    task.execute();
                                    SuperFragment fragment = (SuperFragment) getFragmentManager().findFragmentById(R.id.container);
                                    fragment.redirectToFragment(fragmentRedirect, null);
                                }
                            }).
                    setNegativeButton(R.string.dialog_no,
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialog.dismiss();
                                }
                            }).
                    create();
        }
    }
}
