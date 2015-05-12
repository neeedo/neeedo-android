package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.Location;
import neeedo.imimaprx.htw.de.neeedo.entities.Offer;
import neeedo.imimaprx.htw.de.neeedo.entities.SingleOffer;
import neeedo.imimaprx.htw.de.neeedo.events.NewEanNumberScannedEvent;
import neeedo.imimaprx.htw.de.neeedo.events.NewProductInfosRequestedEvent;
import neeedo.imimaprx.htw.de.neeedo.models.OffersModel;
import neeedo.imimaprx.htw.de.neeedo.rest.HttpGetOutpanByEANAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.HttpPostOfferAsyncTask;


public class NewOfferFragmentEcom extends SuperFragment {

    private EditText etTags;
    private EditText etLocationLat;
    private EditText etLocationLon;
    private EditText etPrice;
    private Button btnSubmit;
    private Button btnBarcode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        eventService.register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.new_offer_form, container, false);

        etTags = (EditText) view.findViewById(R.id.etTags);
        etLocationLat = (EditText) view.findViewById(R.id.etLocationLat);
        etLocationLon = (EditText) view.findViewById(R.id.etLocationLon);
        etPrice = (EditText) view.findViewById(R.id.etPrice);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
       // btnBarcode = (Button) view.findViewById(R.id.btnBarcode);


        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get form values
                String etTagsText = etTags.getText().toString();
                String etLocationLatText = etLocationLat.getText().toString();
                String etLocationLonText = etLocationLon.getText().toString();
                String etPriceText = etPrice.getText().toString();

                try {
                    // convert fields
                    ArrayList<String> tags = new ArrayList<String>(Arrays.asList(etTagsText.split(",")));
                    Location location = new Location(Double.parseDouble(etLocationLatText), Double.parseDouble(etLocationLonText));
                    Double price = Double.parseDouble(etPriceText);

                    // create new demand
                    Offer offer = new Offer();
                    offer.setTags(tags);
                    offer.setLocation(location);
                    offer.setPrice(price);
                    offer.setUserId("1"); // TODO use user id if implemented

                    System.out.println(offer);

                    // send data
                    OffersModel.getInstance().setPostOffer(offer);
                    HttpPostOfferAsyncTask asyncTask = new HttpPostOfferAsyncTask();
                    asyncTask.execute();

                } catch (Exception e) {
                    // show error
                    Toast.makeText(getActivity(), "Wrong data format", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IntentIntegrator integrator = new IntentIntegrator(getActivity());
                integrator.setDesiredBarcodeFormats(IntentIntegrator.PRODUCT_CODE_TYPES);
                integrator.setPrompt("Scan a barcode");
                integrator.setResultDisplayDuration(0);
                integrator.setWide();  // Wide scanning rectangle, may work better for 1D barcodes
                integrator.setCameraId(0);  // Use a specific camera of the device
                integrator.initiateScan();

            }
        });


        return view;
    }

    @Subscribe
    public void handleNewEanNumberScanned(NewEanNumberScannedEvent e) {


        String eanNumber = e.getEanNumber();

        HttpGetOutpanByEANAsyncTask eanAsyncTask = new HttpGetOutpanByEANAsyncTask(eanNumber);
        eanAsyncTask.execute();

    }

    @Subscribe
    public void handleNewProductInfos(NewProductInfosRequestedEvent e) {
        OffersModel offersModel = OffersModel.getInstance();
        SingleOffer singleOffer = offersModel.getSingleOffer();

        String tags = "";


        if (!(singleOffer.getOffer() == null)) {
            for (String s : singleOffer.getOffer().getTags()) {
                tags += s + ", ";
            }


            tags = tags.substring(0, tags.length() - 2);
            etTags.setText(tags);

        }

    }

}