package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import neeedo.imimaprx.htw.de.neeedo.MainActivity;
import neeedo.imimaprx.htw.de.neeedo.R;


public class MainFragment extends SuperFragment implements View.OnClickListener {

    private Button btnNewOfferOperation;
    private Button btnNewDemandOperation;
    private Activity activity;

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
        int navigationIndex = 0;
        switch (v.getId()) {
            case R.id.btnNewDemand:
                navigationIndex = MainActivity.MENU_NEW_DEMAND;
                break;

            case R.id.btnNewOffer:
                navigationIndex = MainActivity.MENU_NEW_OFFER;
                break;
        }
    }
}