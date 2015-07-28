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
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import neeedo.imimaprx.htw.de.neeedo.LoginActivity;
import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.events.UserStateChangedEvent;
import neeedo.imimaprx.htw.de.neeedo.models.ActiveUser;
import neeedo.imimaprx.htw.de.neeedo.models.DemandsModel;
import neeedo.imimaprx.htw.de.neeedo.models.MessagesModel;
import neeedo.imimaprx.htw.de.neeedo.models.OffersModel;
import neeedo.imimaprx.htw.de.neeedo.rest.util.BaseAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.service.EventService;
import neeedo.imimaprx.htw.de.neeedo.vo.RequestCodes;

import android.support.v4.app.DialogFragment;

public class SuperFragment extends Fragment {

    protected EventService eventService = EventService.getInstance();

    private MenuItem actionBarLogout;
    private MenuItem actionBarLogin;
    private MenuItem actionBarMessage;

    private TextView tvBadge;

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
//            Toast.makeText(getActivity(), getString(R.string.login_finished), Toast.LENGTH_SHORT).show();
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
        actionBarMessage = menu.findItem(R.id.new_messages_icon);

        if (isUserLoggedIn) {
            actionBarLogin.setVisible(false);
            actionBarLogout.setVisible(true);
        } else {
            actionBarLogin.setVisible(true);
            actionBarLogout.setVisible(false);
        }

        RelativeLayout badgeLayout = (RelativeLayout) actionBarMessage.getActionView();
        tvBadge = (TextView) badgeLayout.findViewById(R.id.tvBadge);
        tvBadge.setVisibility(View.INVISIBLE);

        MessagesModel messagesModel = MessagesModel.getInstance();
        messagesModel.setMessageCounter(tvBadge);
        messagesModel.changeCount();

        badgeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectToFragment(MessageFragment.class);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_bar_login) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivityForResult(intent, RequestCodes.LOGIN_REQUEST_CODE);
            return true;
        } else if (id == R.id.action_bar_logout) {
            DialogFragment dialog = new DialogFragment() {
                @Override
                public Dialog onCreateDialog(Bundle savedInstanceState) {
                    return new ProgressDialog.Builder(getActivity()).
                            setMessage(getResources().getString(R.string.dialog_logout)).
                            setPositiveButton(R.string.dialog_yes,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            ActiveUser.getInstance().clearUserInformation();
                                            DemandsModel.getInstance().clearDemands();
                                            OffersModel.getInstance().clearOffers();

                                            Toast.makeText(getActivity(), getString(R.string.logout_finished), Toast.LENGTH_SHORT).show();
                                            setLoginButtonState();
                                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                                            startActivityForResult(intent, RequestCodes.LOGIN_REQUEST_CODE);
                                        }
                                    }).
                            setNegativeButton(R.string.dialog_no,
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            // dismiss, nothing else
                                        }
                                    }).
                            create();
                }
            };
            dialog.show(getFragmentManager(), getResources().getString(R.string.confirm));

        } else if (id == R.id.new_messages_icon) {
            redirectToFragment(MessageFragment.class);
        } else if (id == R.id.favorites_icon) {
            redirectToFragment(ListFavoritesFragment.class);
        }

        return super.onOptionsItemSelected(item);
    }

    public void redirectToFragment(Class type) {
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = null;

        try {
            fragment = (Fragment) type.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
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
                                    if (fragmentRedirect != null) {
                                        fragment.redirectToFragment(fragmentRedirect);
                                    }
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
