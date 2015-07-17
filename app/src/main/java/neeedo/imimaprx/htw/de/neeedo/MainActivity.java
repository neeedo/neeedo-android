package neeedo.imimaprx.htw.de.neeedo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;

import java.util.Timer;
import java.util.TimerTask;

import neeedo.imimaprx.htw.de.neeedo.fragments.ListDemandsFragment;
import neeedo.imimaprx.htw.de.neeedo.fragments.ListFavoritesFragment;
import neeedo.imimaprx.htw.de.neeedo.fragments.ListOffersFragment;
import neeedo.imimaprx.htw.de.neeedo.fragments.MainFragment;
import neeedo.imimaprx.htw.de.neeedo.fragments.MessageFragment;
import neeedo.imimaprx.htw.de.neeedo.fragments.NavigationDrawerFragment;
import neeedo.imimaprx.htw.de.neeedo.fragments.NewDemandFragment;
import neeedo.imimaprx.htw.de.neeedo.fragments.NewOfferFragment;
import neeedo.imimaprx.htw.de.neeedo.models.ActiveUser;
import neeedo.imimaprx.htw.de.neeedo.models.MessagesModel;
import neeedo.imimaprx.htw.de.neeedo.rest.message.GetMessagesByUserIdAndReadStateAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.user.GetUserByEmailAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.utils.CertificateTrustService;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private Fragment mFragment;
    private FragmentManager mFragmentManager;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private int mCurrentNavigationIndex = 0;
    private static Timer timer = new Timer();

    private final ActiveUser activeUser = ActiveUser.getInstance();

    public static final String STATE_NAVIGATION_INDEX = "current_navigation_state_index";
    public static final String STATE_FRAGMENT = "current_fragment_state";

    public static final int MENU_HOME = 0;
    public static final int MENU_LIST_OFFERS = 1;
    public static final int MENU_NEW_OFFER = 2;
    public static final int MENU_LIST_DEMANDS = 3;
    public static final int MENU_NEW_DEMAND = 4;
    public static final int MENU_MESSAGE = 6;
    public static final int FAVORITES = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        initOrRestoreState(savedInstanceState);

        mNavigationDrawerFragment = (NavigationDrawerFragment) mFragmentManager.findFragmentById(R.id.navigation_drawer);
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));

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

        savedInstanceState.putInt(STATE_NAVIGATION_INDEX, mCurrentNavigationIndex);

        if (mFragment != null && mFragment.isAdded()) {
            mFragmentManager.putFragment(savedInstanceState, STATE_FRAGMENT, mFragment);
        }
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        initOrRestoreState(savedInstanceState);
    }

    public void setFragment(Fragment fragment) {
        mFragment = fragment;
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
        }
    }

    private void initSingletons() {
        activeUser.setContext(getApplicationContext());
    }

    private void initOrRestoreState(Bundle savedInstanceState) {
        mFragmentManager = getSupportFragmentManager();

        if (savedInstanceState != null) {
            mFragment = mFragmentManager.getFragment(savedInstanceState, STATE_FRAGMENT);
            mCurrentNavigationIndex = savedInstanceState.getInt(STATE_NAVIGATION_INDEX);
        } else {
            mFragment = new MainFragment();
            mCurrentNavigationIndex = 0;
        }

        changeFragment(mFragment, mCurrentNavigationIndex);
    }

    private void changeFragment(Fragment fragment, int navigationIndex) {
        try {
            mFragmentManager.beginTransaction()
                    .addToBackStack(String.valueOf(navigationIndex))
                    .replace(R.id.container, fragment)
                    .commit();
        } catch (NullPointerException e) {
            // TODO is there a way to avoid this exception?
        }
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        switch (position) {
            case MENU_HOME:
                mFragment = new MainFragment();
                break;
            case MENU_LIST_OFFERS:
                mFragment = new ListOffersFragment();
                break;
            case MENU_NEW_OFFER:
                mFragment = new NewOfferFragment();
                break;
            case MENU_LIST_DEMANDS:
                mFragment = new ListDemandsFragment();
                break;
            case MENU_NEW_DEMAND:
                mFragment = new NewDemandFragment();
                break;
            case MENU_MESSAGE:
                mFragment = new MessageFragment();
                break;
            case FAVORITES:
                mFragment = new ListFavoritesFragment();
                break;
            default:
                mFragment = new MainFragment();
                break;
        }
        changeFragment(mFragment, position);
        mCurrentNavigationIndex = position;
    }


}
