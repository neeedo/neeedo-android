package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.otto.Subscribe;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.util.Location;
import neeedo.imimaprx.htw.de.neeedo.events.NewEanTagsReceivedEvent;
import neeedo.imimaprx.htw.de.neeedo.events.NewImageReceivedFromServer;
import neeedo.imimaprx.htw.de.neeedo.fragments.handler.SendNewOfferHandler;
import neeedo.imimaprx.htw.de.neeedo.fragments.handler.StartCameraHandler;
import neeedo.imimaprx.htw.de.neeedo.fragments.handler.StartLocationChooserHandler;
import neeedo.imimaprx.htw.de.neeedo.fragments.handler.StartNewBarcodeScanHandler;

public class NewOfferFragment extends FormOfferFragment {

    public void setNewCameraOutputFile(File newCameraOutputFile) {
        this.newCameraOutputFile = newCameraOutputFile;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = super.onCreateView(inflater, container, savedInstanceState);

        addImageButton.setOnClickListener(new StartCameraHandler(this));
        btnSubmit.setOnClickListener(new SendNewOfferHandler(this));
        btnBarcode.setOnClickListener(new StartNewBarcodeScanHandler(this));
        btnSetLocation.setOnClickListener(new StartLocationChooserHandler(this));

        return view;
    }

    @Subscribe
    public void handleNewEanTagsReceived(NewEanTagsReceivedEvent event) {
        super.handleNewEanTagsReceived(event);
    }

    @Subscribe
    public void handleNewImageReceivedFromServer(NewImageReceivedFromServer event) {
        super.handleNewImageReceivedFromServer(event);
    }

    public ArrayList<String> getOfferTags() {
        return new ArrayList<String>(Arrays.asList(etTags.getText().toString().split(",")));
    }

    public Location getLocation() {
        return new Location(selectedGeoPoint);
    }

    public Double getPrice() {
        return Double.parseDouble(etPrice.getText().toString());
    }

    public ArrayList<String> getImages() {
        return imageNamesOnServer;
    }

    public boolean validateData() {
        boolean isValidInput = true;
        if (selectedGeoPoint == null) {
            //TODO show error
            isValidInput = false;
        }
        if (imageNamesOnServer.isEmpty()) {
            //TODO show error
            isValidInput = false;
        }
        if (etTags.getText().toString().matches("")) {
            etTags.setError(getString(R.string.error_empty_field));
            isValidInput = false;
        }
        if (etPrice.getText().toString().matches("")) {
            etTags.setError(getString(R.string.error_empty_field));
            isValidInput = false;
        }
        return isValidInput;
    }
}
