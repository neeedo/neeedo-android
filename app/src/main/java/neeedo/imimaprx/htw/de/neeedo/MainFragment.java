package neeedo.imimaprx.htw.de.neeedo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import neeedo.imimaprx.htw.de.neeedo.Entities.Demand;
import neeedo.imimaprx.htw.de.neeedo.Entities.Demands;
import neeedo.imimaprx.htw.de.neeedo.Entities.LocalDemands;
import neeedo.imimaprx.htw.de.neeedo.Entities.Location;
import neeedo.imimaprx.htw.de.neeedo.Entities.Price;
import neeedo.imimaprx.htw.de.neeedo.RestService.HttpGetActivity;
import neeedo.imimaprx.htw.de.neeedo.RestService.HttpGetByIDActivity;

public class MainFragment extends Fragment implements View.OnClickListener {

    public static final int GET_OPERATION = 1;
    public static final int POST_OPERATION = 2;
    public static final int GET_SINGLE_BY_ID_OPERATION = 3;


    private Button btnGetOperation;
    private Button btnPostOperation;
    private Button btnGetSingleOperation;

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
    public void onClick(View v) {

        Intent intent;
        switch (v.getId()) {
            case R.id.BtnGet:
                showLoadingProgressDialog();
                intent = new Intent(activity, HttpGetActivity.class);
                startActivityForResult(intent, GET_OPERATION);
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

                intent = new Intent(activity, HttpGetByIDActivity.class);
                startActivityForResult(intent, POST_OPERATION);

                break;
            case R.id.BtnGetSingle:
                showLoadingProgressDialog();
                intent = new Intent(activity, HttpGetByIDActivity.class);
                startActivityForResult(intent, GET_SINGLE_BY_ID_OPERATION);
                break;
        }
    }

    @Override
    public void onActivityResult(final int requestCode, final int resultCode, final Intent data) {

        dismissProgressDialog();
        String result = "Server not available";

        if (resultCode == Activity.RESULT_OK && requestCode == GET_OPERATION) {

            if (data.getStringExtra("result") != null)
                result = data.getStringExtra("result");

            }

        if (resultCode == Activity.RESULT_OK && requestCode == GET_SINGLE_BY_ID_OPERATION) {

            if (data.getStringExtra("result") != null)
                result = data.getStringExtra("result");



        }

        if (resultCode == Activity.RESULT_OK && requestCode == POST_OPERATION) {

            if (data.getStringExtra("result") != null)
                result = data.getStringExtra("result");


        }

        Toast.makeText(getActivity(), result, Toast.LENGTH_SHORT).show();

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