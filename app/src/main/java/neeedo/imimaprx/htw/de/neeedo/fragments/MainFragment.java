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

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.rest.BaseAsyncTask;


public class MainFragment extends SuperFragment implements View.OnClickListener {

    private Button btnNewOfferOperation;
    private Button btnNewDemandOperation;

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

        BaseAsyncTask asyncTask;

        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = null;

        switch (v.getId()) {
            case R.id.btnNewDemand:
                fragment = new NewDemandFragment();
                fragmentManager.beginTransaction()
                        .addToBackStack("4") // 4 = position of new demand menu item
                        .replace(R.id.container, fragment)
                        .commit();
                break;

            case R.id.btnNewOffer:
                fragment = new NewOfferFragment();
                fragmentManager.beginTransaction()
                        .addToBackStack("2") // 2 = position of new offer menu item
                        .replace(R.id.container, fragment)
                        .commit();
                break;
        }
    }
}