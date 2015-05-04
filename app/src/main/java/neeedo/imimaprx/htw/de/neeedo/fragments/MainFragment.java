package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.squareup.otto.Subscribe;

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.events.ServerResponseEvent;
import neeedo.imimaprx.htw.de.neeedo.models.DemandsModel;
import neeedo.imimaprx.htw.de.neeedo.rest.SuperHttpAsyncTask;


public class MainFragment extends SuperFragment implements View.OnClickListener {

//    private Button btnGetOperation;
//    private Button btnPostOperation;
//    private Button btnGetSingleOperation;
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

//        btnGetOperation = (Button) activity.findViewById(R.id.BtnGet);
//        btnPostOperation = (Button) activity.findViewById(R.id.BtnPost);
//        btnGetSingleOperation = (Button) activity.findViewById(R.id.BtnGetSingle);
        btnNewOfferOperation = (Button) activity.findViewById(R.id.btnNewOffer);
        btnNewDemandOperation = (Button) activity.findViewById(R.id.btnNewDemand);

//        btnGetOperation.setOnClickListener(this);
//        btnPostOperation.setOnClickListener(this);
//        btnGetSingleOperation.setOnClickListener(this);
        btnNewOfferOperation.setOnClickListener(this);
        btnNewDemandOperation.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        //TODO extract whole function into proper controller

        SuperHttpAsyncTask asyncTask;

        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = null;

        switch (v.getId()) {
//            case R.id.BtnGet:
//                showLoadingProgressDialog();
//
//                asyncTask = new HttpGetAsyncTask();
//                asyncTask.execute();
//
//                break;
//
//            case R.id.BtnPost:
//                showLoadingProgressDialog();
//
//                ArrayList<String> list = new ArrayList<String>();
//                list.add("Kekse");
//                list.add("more Kekse");
//                list.add("still more Kekse");
//
//                Demand demand = new Demand();
//                demand.setDistance(100);
//                demand.setLocation(new Location(55, 33));
//                demand.setPrice(new Price(100, 500));
//                demand.setUserId("1");
//                demand.setMustTags(list);
//                demand.setShouldTags(list);
//                DemandsModel.getInstance().setPostDemand(demand);
//
//                asyncTask = new HttpPostDemandAsyncTask();
//
//                asyncTask.execute();
//
//                break;
//
//            case R.id.BtnGetSingle:
//                showLoadingProgressDialog();
//                asyncTask = new HttpGetByIDAsyncTask();
//
//                asyncTask.execute();
//
//                break;

            case R.id.btnNewDemand:
                fragment = new NewDemandFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();
                break;

            case R.id.btnNewOffer:
                fragment = new NewOfferFragment();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, fragment)
                        .commit();
                break;
        }
    }

    @Subscribe
    public void handleNewServerData(ServerResponseEvent e) {

        System.out.println(DemandsModel.getInstance().getDemands());

        dismissProgressDialog();
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        //TODO make DRY, pull code into handle...() function and make use of proper entities

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