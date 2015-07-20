package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import neeedo.imimaprx.htw.de.neeedo.R;

public class NavigationDrawerFragment extends SuperFragment {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);

        drawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);
        drawerToggle = new ActionBarDrawerToggle(
                getActivity(),
                NavigationDrawerFragment.this.drawerLayout,
                R.drawable.ic_drawer,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        ) {
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                if (!isAdded()) {
                    return;
                }

                getActivity().supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                if (!isAdded()) {
                    return;
                }

                getActivity().supportInvalidateOptionsMenu();

                // close soft keyboard if open
                if (drawerView != null) {
                    InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(drawerView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }
        };


        drawerLayout.post(new Runnable() {
            @Override
            public void run() {
                drawerToggle.syncState();
            }
        });
        drawerLayout.setDrawerListener(drawerToggle);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LinearLayout navigationDrawerLayout = (LinearLayout) inflater.inflate(R.layout.fragment_navigation_drawer, container, false);

        View newOfferBtn = navigationDrawerLayout.findViewById(R.id.create_new_offer);
        newOfferBtn.setOnClickListener(new OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               openFragmentAndCloseDrawer(NewOfferFragment.class);
                                           }
                                       }
        );

        View listOffersBtn = navigationDrawerLayout.findViewById(R.id.list_offers);
        listOffersBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragmentAndCloseDrawer(ListOffersFragment.class);
            }
        });

        View newDemandBtn = navigationDrawerLayout.findViewById(R.id.create_new_demand);
        newDemandBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragmentAndCloseDrawer(NewDemandFragment.class);
            }
        });

        View listDemandsBtn = navigationDrawerLayout.findViewById(R.id.list_demands);
        listDemandsBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragmentAndCloseDrawer(ListDemandsFragment.class);
            }
        });

        return navigationDrawerLayout;
    }

    private void openFragmentAndCloseDrawer(Class clazz) {
        redirectToFragment(clazz, 1);
        drawerLayout.closeDrawers();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
