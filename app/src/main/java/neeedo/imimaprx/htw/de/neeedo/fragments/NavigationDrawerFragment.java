package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import neeedo.imimaprx.htw.de.neeedo.R;

public class NavigationDrawerFragment extends SuperFragment {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private ImageView imageView;
    private TextView newOfferBtn;
    private TextView listOffersBtn;
    private TextView newDemandBtn;
    private TextView listDemandsBtn;

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

            @Override
            public void onDrawerStateChanged(int newState) {
                super.onDrawerStateChanged(newState);
                if (newState == DrawerLayout.STATE_SETTLING || newState == DrawerLayout.STATE_IDLE)
                    highlightCurrentFragment();
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

        imageView = (ImageView) navigationDrawerLayout.findViewById(R.id.navigation_drawer_logo);
        imageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToFragment(new MainFragment());
            }
        });


        newOfferBtn = (TextView) navigationDrawerLayout.findViewById(R.id.create_new_offer);
        newOfferBtn.setOnClickListener(new OnClickListener() {
                                           @Override
                                           public void onClick(View v) {
                                               redirectToFragment(new NewOfferFragment());
                                           }
                                       }
        );

        listOffersBtn = (TextView) navigationDrawerLayout.findViewById(R.id.list_offers);
        listOffersBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToFragment(new ListOffersFragment());
            }
        });

        newDemandBtn = (TextView) navigationDrawerLayout.findViewById(R.id.create_new_demand);
        newDemandBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToFragment(new NewDemandFragment());
            }
        });

        listDemandsBtn = (TextView) navigationDrawerLayout.findViewById(R.id.list_demands);
        listDemandsBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToFragment(new ListDemandsFragment());
            }
        });

        return navigationDrawerLayout;
    }

    private void redirectToFragment(SuperFragment fragment) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.container, fragment)
                .commit();
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

    public void highlightCurrentFragment() {
        List<Fragment> fragments = getActivity().getSupportFragmentManager().getFragments();

        Fragment currentlyDisplayesFragment = null;

        for (Fragment fragment : fragments) {
            if (fragment.isVisible() && !(fragment instanceof NavigationDrawerFragment)) {
                System.out.println(fragment.getClass().getName());
                currentlyDisplayesFragment = fragment;
                break;
            }
        }

        if (currentlyDisplayesFragment == null)
            return;

        int inactiveBackgroundColor = 0xcc111111;
        int inactiveTextColor = 0xffdddddd;

        setColorsToTextField(newOfferBtn, inactiveBackgroundColor, inactiveTextColor);
        setColorsToTextField(listOffersBtn, inactiveBackgroundColor, inactiveTextColor);
        setColorsToTextField(newDemandBtn, inactiveBackgroundColor, inactiveTextColor);
        setColorsToTextField(listDemandsBtn, inactiveBackgroundColor, inactiveTextColor);

        int activeBackgroundColor = inactiveTextColor;
        int activeTextColor = inactiveBackgroundColor;

        if (currentlyDisplayesFragment instanceof NewOfferFragment)
            setColorsToTextField(newOfferBtn, activeBackgroundColor, activeTextColor);
        if (currentlyDisplayesFragment instanceof ListOffersFragment)
            setColorsToTextField(listOffersBtn, activeBackgroundColor, activeTextColor);
        if (currentlyDisplayesFragment instanceof NewDemandFragment)
            setColorsToTextField(newDemandBtn, activeBackgroundColor, activeTextColor);
        if (currentlyDisplayesFragment instanceof ListDemandsFragment)
            setColorsToTextField(listDemandsBtn, activeBackgroundColor, activeTextColor);
    }

    private void setColorsToTextField(TextView textView, int backgroundColor, int textColor) {
        textView.setBackgroundColor(backgroundColor);
        textView.setTextColor(textColor);
    }
}
