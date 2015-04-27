package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.Location;
import neeedo.imimaprx.htw.de.neeedo.entities.Offer;
import neeedo.imimaprx.htw.de.neeedo.events.ServerResponseEvent;
import neeedo.imimaprx.htw.de.neeedo.models.OffersModel;
import neeedo.imimaprx.htw.de.neeedo.rest.HttpPostOfferAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.SuperHttpAsyncTask;

public class NewOfferFragment extends SuperFragment {

    // fields
    private EditText etTags;
    private EditText etLocationLat;
    private EditText etLocationLon;
    private EditText etPrice;
    private Button btnSubmit;
    private ImageButton addImageButton;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.new_offer_form, container, false);

        // initalize fields
        etTags = (EditText) view.findViewById(R.id.etTags);
        etLocationLat = (EditText) view.findViewById(R.id.etLocationLat);
        etLocationLon = (EditText) view.findViewById(R.id.etLocationLon);
        etPrice = (EditText) view.findViewById(R.id.etPrice);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        addImageButton = (ImageButton) view.findViewById(R.id.addImageButton);


        //TODO extract
        addImageButton.setOnClickListener(new View.OnClickListener() {
            public static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 12345;
            public File photoFile;

            @Override
            public void onClick(View v) {
                Context context = getActivity();
                PackageManager packageManager = context.getPackageManager();

                if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA) == false) {
                    Toast.makeText(getActivity(), R.string.no_camera, Toast.LENGTH_SHORT).show();
                    return;
                }

                File path = new File(Environment.getExternalStorageDirectory(), "foo/bar");
                if (!path.exists()) path.mkdirs();
                photoFile = getOutputMediaFile();

                Uri mImageCaptureUri1 = Uri.fromFile(getOutputMediaFile());

                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, mImageCaptureUri1);
//        cameraIntent.putExtra("return-data", true);
                startActivityForResult(cameraIntent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
            }

            private File getOutputMediaFile() {
                File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "bla");

                if (!mediaStorageDir.exists()) {
                    if (!mediaStorageDir.mkdirs()) {
                        return null;
                    }
                }

                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                File mediaFile;
                mediaFile = new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");


                return mediaFile;
            }
        });

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
                    SuperHttpAsyncTask asyncTask = new HttpPostOfferAsyncTask();
                    asyncTask.execute();

                } catch (Exception e) {
                    // show error
                    Toast.makeText(getActivity(), getString(R.string.error_empty_or_wrong_format), Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }

    @Subscribe
    public void redirectToList(ServerResponseEvent e) {
        // show message
        Toast.makeText(getActivity(), getString(R.string.new_card_submit_successful_offer), Toast.LENGTH_SHORT).show();

        // go to list cards view
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = new ListOffersFragment();

        fragmentManager.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }
}
