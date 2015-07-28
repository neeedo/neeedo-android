package neeedo.imimaprx.htw.de.neeedo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;

import java.util.Timer;
import java.util.TimerTask;

import neeedo.imimaprx.htw.de.neeedo.fragments.MainFragment;
import neeedo.imimaprx.htw.de.neeedo.models.ActiveUser;
import neeedo.imimaprx.htw.de.neeedo.models.ApplicationContextModel;
import neeedo.imimaprx.htw.de.neeedo.models.MessagesModel;
import neeedo.imimaprx.htw.de.neeedo.rest.message.GetMessagesByUserIdAndReadStateAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.user.GetUserByEmailAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.utils.CertificateTrustService;
import neeedo.imimaprx.htw.de.neeedo.vo.RequestCodes;

public class MainActivity extends ActionBarActivity {

    private Fragment mFragment;
    private FragmentManager mFragmentManager;
    private static Timer timer = new Timer();

    private final ActiveUser activeUser = ActiveUser.getInstance();

    public static final String STATE_FRAGMENT = "current_fragment_state";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        initOrRestoreState(savedInstanceState);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        CertificateTrustService.trustAllCerts();

        initSingletons();
        attempLogin();

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                MessagesModel.getInstance().setNewMessagesCounter(0);
                if (activeUser.userCredentialsAvailable()) {
                    new GetMessagesByUserIdAndReadStateAsyncTask(ActiveUser.getInstance().getUserId(), false, true).execute();
                } else {
                    MessagesModel.getInstance().changeCount();
                }
            }
        }, 3000, 30000);


    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        if (mFragment != null && mFragment.isAdded()) {
            mFragmentManager.putFragment(savedInstanceState, STATE_FRAGMENT, mFragment);
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        initOrRestoreState(savedInstanceState);
    }

    private void attempLogin() {
        if (activeUser.isAlreadyStarted()) {
            return;
        }
        activeUser.setContext(getApplicationContext());
        activeUser.loadValuesFromPreferences();
        if (activeUser.userCredentialsAvailable()) {
            new GetUserByEmailAsyncTask().execute();
            activeUser.setAlreadyStarted(true);
        } else {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivityForResult(intent, RequestCodes.LOGIN_REQUEST_CODE);
        }
    }

    private void initSingletons() {
        ApplicationContextModel.getInstance().setApplicationContext(getApplicationContext());
    }

    private void initOrRestoreState(Bundle savedInstanceState) {
        mFragmentManager = getSupportFragmentManager();

        if (savedInstanceState != null) {
            mFragment = mFragmentManager.getFragment(savedInstanceState, STATE_FRAGMENT);
        } else {
            mFragment = new MainFragment();
        }

        changeFragment(mFragment);
    }

    private void changeFragment(Fragment fragment) {
        mFragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.container, fragment)
                .commit();
    }
}
