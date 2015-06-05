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
import neeedo.imimaprx.htw.de.neeedo.service.S3Service;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private NavigationDrawerFragment mNavigationDrawerFragment;

    private final ActiveUser activeUser = ActiveUser.getInstance();
    private final S3Service s3Service = S3Service.getInstance();

    private static final String NAVIGATION_KEY = "current_navigation_item_selected";
    private int mCurrentNavigationIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Mint.initAndStartSession(this, "9b8bdf4b");

        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mCurrentNavigationIndex = savedInstanceState.getInt(NAVIGATION_KEY);
        }

        setContentView(R.layout.activity_main);

        onNavigationDrawerItemSelected(mCurrentNavigationIndex);

        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));

        initSingletons();
        attempLogin();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putInt(NAVIGATION_KEY, mCurrentNavigationIndex);
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
        s3Service.initialize(this);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = new MainFragment();
                break;
            case 1:
                fragment = new ListOffersFragment();
                break;
            case 2:
                fragment = new NewOfferFragment();
                break;
            case 3:
                fragment = new ListDemandsFragment();
                break;
            case 4:
                fragment = new NewDemandFragment();
                break;
            case 5:
                fragment = new DiolorSwipeFragment();
                break;
            default:
                fragment = new MainFragment();
                break;
        }
        fragmentManager.beginTransaction()
                .addToBackStack(String.valueOf(position))
                .replace(R.id.container, fragment)
                .commit();

        mCurrentNavigationIndex = position;
    }
}
