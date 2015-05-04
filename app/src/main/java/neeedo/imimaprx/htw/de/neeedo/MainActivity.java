package neeedo.imimaprx.htw.de.neeedo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.splunk.mint.Mint;

import neeedo.imimaprx.htw.de.neeedo.fragments.DiolorSwipeFragment;
import neeedo.imimaprx.htw.de.neeedo.fragments.ListDemandsFragment;
import neeedo.imimaprx.htw.de.neeedo.fragments.ListOffersFragment;
import neeedo.imimaprx.htw.de.neeedo.fragments.MainFragment;
import neeedo.imimaprx.htw.de.neeedo.fragments.NavigationDrawerFragment;
import neeedo.imimaprx.htw.de.neeedo.fragments.NewDemandFragment;
import neeedo.imimaprx.htw.de.neeedo.fragments.NewOfferFragment;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    private NavigationDrawerFragment mNavigationDrawerFragment;
    private CharSequence mTitle;
    private View actionBarLoggedIn;
    private View actionBarLogin;

    private static final int LOGIN_REQUEST_CODE = 8945;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Mint.initAndStartSession(this, "9b8bdf4b");

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();
        mNavigationDrawerFragment.setUp(R.id.navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout));


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
                fragment = new NewOfferFragment();
                break;
            case 2:
                fragment = new NewDemandFragment();
                break;
            case 3:
                fragment = new ListOffersFragment();
                break;
            case 4:
                fragment = new ListDemandsFragment();
                break;
            case 5:
                fragment = new DiolorSwipeFragment();
                break;
            default:
                fragment = new MainFragment();
                break;
        }
        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
        setLoginButtonState();
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK || requestCode == LOGIN_REQUEST_CODE) {
            Toast.makeText(this, "login returned!", Toast.LENGTH_SHORT).show();
            setLoginButtonState();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return super.onOptionsItemSelected(item);
        } else if (id == R.id.action_bar_login) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivityForResult(intent, LOGIN_REQUEST_CODE);

            Toast.makeText(this, "Example action.", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setLoginButtonState() {
        actionBarLogin = findViewById(R.id.action_bar_login);
        actionBarLoggedIn = findViewById(R.id.action_bar_logged_in);
        if (actionBarLoggedIn == null || actionBarLogin == null)
            return;
        actionBarLogin.setVisibility(View.GONE);
        actionBarLoggedIn.setVisibility(View.VISIBLE);
    }
}
