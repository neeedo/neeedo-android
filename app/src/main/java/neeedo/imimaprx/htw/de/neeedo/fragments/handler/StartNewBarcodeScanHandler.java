package neeedo.imimaprx.htw.de.neeedo.fragments.handler;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.EditText;

import com.google.zxing.integration.android.IntentIntegrator;

import java.util.ArrayList;
import java.util.Arrays;

import neeedo.imimaprx.htw.de.neeedo.entities.Location;
import neeedo.imimaprx.htw.de.neeedo.entities.Offer;
import neeedo.imimaprx.htw.de.neeedo.models.OffersModel;
import neeedo.imimaprx.htw.de.neeedo.models.UserModel;
import neeedo.imimaprx.htw.de.neeedo.rest.offer.PostCreateUpdateOfferAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.util.BaseAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.vo.RequestCodes;

public class StartNewBarcodeScanHandler implements View.OnClickListener {

    private final Fragment fragment;

    public StartNewBarcodeScanHandler(Fragment fragment) {
        this.fragment = fragment;
    }

    @Override
    public void onClick(View v) {
        IntentIntegrator integrator = new IntentIntegrator(fragment.getActivity());
        integrator.setDesiredBarcodeFormats(IntentIntegrator.PRODUCT_CODE_TYPES);
        integrator.setPrompt("Scan a barcode");
        integrator.setResultDisplayDuration(0);
        integrator.setCameraId(0);
        Intent barcodeScanIntent = integrator.createScanIntent();
        fragment.startActivityForResult(barcodeScanIntent, RequestCodes.BARCODE_SCAN_REQUEST_CODE);
    }
}
