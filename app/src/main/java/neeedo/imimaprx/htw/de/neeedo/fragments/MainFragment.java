package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import neeedo.imimaprx.htw.de.neeedo.MainActivity;
import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.rest.util.BaseAsyncTask;


public class MainFragment extends SuperFragment implements View.OnClickListener {

    private Button btnNewOfferOperation;
    private Button btnNewDemandOperation;
    private int mCurrentNavigationIndex;

    private Activity activity;
    private ProgressDialog progressDialog;

    public MainFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onActivityCreated(final Bundle savedState) {
        super.onActivityCreated(savedState);

        activity = getActivity();

        btnNewOfferOperation = (Button) activity.findViewById(R.id.btnNewOffer);
        btnNewDemandOperation = (Button) activity.findViewById(R.id.btnNewDemand);

        btnNewOfferOperation.setOnClickListener(this);
        btnNewDemandOperation.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //TODO extract whole function into proper controller

        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = null;
        int navigationIndex = 0;

        switch (v.getId()) {
            case R.id.btnNewDemand:
                navigationIndex = 4; // 4 = position of new demand menu item
                fragment = new NewDemandFragment();
                break;

            case R.id.btnNewOffer:
                navigationIndex = 2; // 2 = position of new offer menu item
                fragment = new NewOfferFragment();
                break;
        }

        fragmentManager.beginTransaction()
                .addToBackStack(Integer.toString(navigationIndex))
                .replace(R.id.container, fragment)
                .commit();

        ((MainActivity) getActivity()).setNavigationIndex(navigationIndex);
    }
}