package neeedo.imimaprx.htw.de.neeedo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
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

    public static final String NAVIGATION_KEY = "current_navigation_item_selected";
    public static final String STATE_FRAGMENT = "current_fragment_state";

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

        savedInstanceState.putInt(NAVIGATION_KEY, mCurrentNavigationIndex);

        getSupportFragmentManager().putFragment(savedInstanceState, STATE_FRAGMENT, mFragment);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        initOrRestoreState(savedInstanceState);
    }

    public void setNavigationIndex(int currentNavigationIndex) {
        mCurrentNavigationIndex = currentNavigationIndex;
    }

    public void setFragment(Fragment fragment) {
        mFragment = fragment;
    }

    private void attempLogin() {
        ActiveUser activeUser = ActiveUser.getInstance();
        activeUser.setContext(getApplicationContext());
        activeUser.loadValuesFromPreferences();
        if (activeUser.userCredentialsAvailable()) {
            new GetUserInfosAsyncTask().execute();
        }
    }

    private void initSingletons() {
        activeUser.setContext(getApplicationContext());
    }

    private void initOrRestoreState(Bundle savedInstanceState) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        if (savedInstanceState != null) {
            mFragment = getSupportFragmentManager().getFragment(savedInstanceState, STATE_FRAGMENT);
            mCurrentNavigationIndex = savedInstanceState.getInt(NAVIGATION_KEY);
        } else {
            mFragment = new MainFragment();
            mCurrentNavigationIndex = 0;
        }

        fragmentManager.beginTransaction()
                .addToBackStack(String.valueOf(mCurrentNavigationIndex))
                .replace(R.id.container, mFragment)
                .commit();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        switch (position) {
            case 0:
                mFragment = new MainFragment();
                break;
            case 1:
                mFragment = new ListOffersFragment();
                break;
            case 2:
                mFragment = new NewOfferFragment();
                break;
            case 3:
                mFragment = new ListDemandsFragment();
                break;
            case 4:
                mFragment = new NewDemandFragment();
                break;
            case 5:
                mFragment = new DiolorSwipeFragment();
                break;
            default:
                mFragment = new MainFragment();
                break;
        }
        fragmentManager.beginTransaction()
                .addToBackStack(String.valueOf(position))
                .replace(R.id.container, mFragment)
                .commit();

        mCurrentNavigationIndex = position;
    }
}
