package neeedo.imimaprx.htw.de.neeedo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;

import com.splunk.mint.Mint;

import neeedo.imimaprx.htw.de.neeedo.fragments.DiolorSwipeFragment;
import neeedo.imimaprx.htw.de.neeedo.fragments.ListDemandsFragment;
import neeedo.imimaprx.htw.de.neeedo.fragments.ListOffersFragment;
import neeedo.imimaprx.htw.de.neeedo.fragments.MainFragment;
import neeedo.imimaprx.htw.de.neeedo.fragments.NavigationDrawerFragment;
import neeedo.imimaprx.htw.de.neeedo.fragments.NewDemandFragment;
import neeedo.imimaprx.htw.de.neeedo.fragments.NewOfferFragment;
import neeedo.imimaprx.htw.de.neeedo.models.ActiveUser;
import neeedo.imimaprx.htw.de.neeedo.rest.user.GetUserInfosAsyncTask;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private int mCurrentNavigationIndex = 0;
    private Fragment mFragment;

    private final ActiveUser activeUser = ActiveUser.getInstance();

    public static final String STATE_NAVIGATION_INDEX = "current_navigation_state_index";
    public static final String STATE_FRAGMENT = "current_fragment_state";
    public static final int MENU_HOME = 0;
    public static final int MENU_LIST_OFFERS = 1;
    public static final int MENU_NEW_OFFER = 2;
    public static final int MENU_LIST_DEMANDS = 3;
    public static final int MENU_NEW_DEMAND = 4;
    public static final int MENU_SWIPER = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Mint.initAndStartSession(this, "9b8bdf4b");

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        initOrRestoreState(savedInstanceState);

        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));

        initSingletons();
        attempLogin();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putInt(STATE_NAVIGATION_INDEX, mCurrentNavigationIndex);

        getSupportFragmentManager().putFragment(savedInstanceState, STATE_FRAGMENT, mFragment);
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
        ActiveUser activeUser = ActiveUser.getInstance();
        if (activeUser.isAlreadyStarted()) {
            return;
        }
        activeUser.setContext(getApplicationContext());
        activeUser.loadValuesFromPreferences();
        if (activeUser.userCredentialsAvailable()) {
            new GetUserInfosAsyncTask().execute();
            activeUser.setAlreadyStarted(true);
        }
    }

    private void initSingletons() {
        activeUser.setContext(getApplicationContext());
    }

    private void initOrRestoreState(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            mFragment = getSupportFragmentManager().getFragment(savedInstanceState, STATE_FRAGMENT);
            mCurrentNavigationIndex = savedInstanceState.getInt(STATE_NAVIGATION_INDEX);
        } else {
            mFragment = new MainFragment();
            mCurrentNavigationIndex = 0;
        }
        changeFragment(mFragment, mCurrentNavigationIndex);
    }

    private void changeFragment(Fragment fragment, int navigationIndex) {
        getSupportFragmentManager().beginTransaction()
                .addToBackStack(String.valueOf(navigationIndex))
                .replace(R.id.container, fragment)
                .commit();
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
            case MENU_SWIPER:
                mFragment = new DiolorSwipeFragment();
                break;
            default:
                mFragment = new MainFragment();
                break;
        }
        changeFragment(mFragment, position);
        mCurrentNavigationIndex = position;
    }
}
