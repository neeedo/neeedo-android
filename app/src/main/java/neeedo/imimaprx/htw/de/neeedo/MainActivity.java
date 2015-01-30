package neeedo.imimaprx.htw.de.neeedo;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;

import neeedo.imimaprx.htw.de.neeedo.fragments.ListCardsFragment;
import neeedo.imimaprx.htw.de.neeedo.fragments.MainFragment;
import neeedo.imimaprx.htw.de.neeedo.fragments.NavigationDrawerFragment;
import neeedo.imimaprx.htw.de.neeedo.fragments.NewDemandCardFragment;
import neeedo.imimaprx.htw.de.neeedo.fragments.NewOfferCardFragment;
import neeedo.imimaprx.htw.de.neeedo.fragments.TinderFragment;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
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
                fragment = new NewOfferCardFragment();
                break;
            case 2:
                fragment = new NewDemandCardFragment();
                break;
            case 3:
                fragment = new ListCardsFragment();
                break;
            case 4:
                fragment = new TinderFragment();
                break;
            default:
                fragment = new MainFragment();
                break;
        }
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    //TODO delete? where is this being used?
    //    public void onSectionAttached(int number) {
    //        switch (number) {
    //            case 1:
    //                mTitle = getString(R.string.title_section1);
    //                break;
    //            case 2:
    //                mTitle = getString(R.string.title_section2);
    //                break;
    //            case 3:
    //                mTitle = getString(R.string.title_section3);
    //                break;
    //            case 4:
    //                mTitle = getString(R.string.title_section4);
    //                break;
    //        }
    //    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
