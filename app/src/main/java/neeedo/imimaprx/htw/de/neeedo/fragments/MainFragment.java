package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.squareup.otto.Subscribe;

import neeedo.imimaprx.htw.de.neeedo.entities.LocalDemands;
import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.Demand;
import neeedo.imimaprx.htw.de.neeedo.entities.Location;
import neeedo.imimaprx.htw.de.neeedo.entities.Price;
import neeedo.imimaprx.htw.de.neeedo.events.ServerResponseEvent;
import neeedo.imimaprx.htw.de.neeedo.rest.HttpGetAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.HttpGetByIDAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.HttpPostAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.SuperHttpAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.service.EventService;

public class MainFragment extends Fragment implements View.OnClickListener {

    private Button btnGetOperation;
    private Button btnPostOperation;
    private Button btnGetSingleOperation;

    private Activity activity;
    private ProgressDialog progressDialog;

    private EventService eventService = EventService.getInstance();

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
        super.onCreate(savedState);

        activity = getActivity();

        btnGetOperation = (Button) activity.findViewById(R.id.BtnGet);
        btnPostOperation = (Button) activity.findViewById(R.id.BtnPost);
        btnGetSingleOperation = (Button) activity.findViewById(R.id.BtnGetSingle);

        btnGetOperation.setOnClickListener(this);
        btnPostOperation.setOnClickListener(this);
        btnGetSingleOperation.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        eventService.register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        eventService.unregister(this);
    }

    @Override
    public void onClick(View v) {
        //TODO extract whole function into proper controller

        SuperHttpAsyncTask asyncTask;

        switch (v.getId()) {
            case R.id.BtnGet:
                showLoadingProgressDialog();

                asyncTask = new HttpGetAsyncTask();
                asyncTask.execute();

                break;

            case R.id.BtnPost:
                showLoadingProgressDialog();

                Demand demand = new Demand();
                demand.setDistance(100);
                demand.setLocation(new Location(55l, 33l));
                demand.setPrice(new Price(100, 500));
                demand.setUserId(1l);
                demand.setTags("Whatever");
                LocalDemands.getInstance().setPostDemand(demand);

                asyncTask = new HttpGetByIDAsyncTask();
                asyncTask.execute();

                break;

            case R.id.BtnGetSingle:
                showLoadingProgressDialog();

                asyncTask = new HttpPostAsyncTask();
                asyncTask.execute();

                break;
        }
    }

    @Subscribe
    public void handleNewServerData(ServerResponseEvent e) {
        System.out.println();
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {

//        dismissProgressDialog();
//        String result = "Server not available";
//
//        if (resultCode == Activity.RESULT_OK && requestCode == GET_OPERATION) {
//
//            if (data.getStringExtra("result") != null)
//                result = data.getStringExtra("result");
//
//            Demands demands = LocalDemands.getInstance().getDemands();
//            TextView textView = (TextView) getActivity().findViewById(R.id.demandTextView);
//            textView.setText("");
//            for (Demand demand : demands.getDemands()) {
//                textView.append(demand.toString());
//            }
//
//        }
//
//        if (resultCode == Activity.RESULT_OK && requestCode == GET_SINGLE_BY_ID_OPERATION) {
//
//            if (data.getStringExtra("result") != null)
//                result = data.getStringExtra("result");
//
//            SingleDemand singleDemand = LocalDemands.getInstance().getSingleDemand();
//            TextView textView = (TextView) getActivity().findViewById(R.id.demandTextView);
//            textView.setText(singleDemand.getDemand().toString());
//
//        }
//
//        if (resultCode == Activity.RESULT_OK && requestCode == POST_OPERATION) {
//
//            if (data.getStringExtra("result") != null)
//                result = data.getStringExtra("result");
//            TextView textView = (TextView) getActivity().findViewById(R.id.demandTextView);
//            textView.setText("");
//
//        }
//
//        Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();
    }

    public void showLoadingProgressDialog() {
        this.showProgressDialog("Loading. Please wait...");
    }

    public void showProgressDialog(final CharSequence message) {
        if (this.progressDialog == null) {
            this.progressDialog = new ProgressDialog(activity);
            this.progressDialog.setIndeterminate(true);
        }
        this.progressDialog.setMessage(message);
        this.progressDialog.show();
    }

    public void dismissProgressDialog() {
        if (this.progressDialog != null) {
            this.progressDialog.dismiss();
        }
    }
}