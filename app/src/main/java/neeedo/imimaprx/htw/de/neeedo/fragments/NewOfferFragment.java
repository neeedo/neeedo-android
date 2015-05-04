package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.media.ExifInterface;
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
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.Location;
import neeedo.imimaprx.htw.de.neeedo.entities.Offer;
import neeedo.imimaprx.htw.de.neeedo.events.ServerResponseEvent;
import neeedo.imimaprx.htw.de.neeedo.helpers.LocationHelper;
import neeedo.imimaprx.htw.de.neeedo.models.OffersModel;
import neeedo.imimaprx.htw.de.neeedo.rest.HttpPostOfferAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.SuperHttpAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.utils.ImageUtils;


public class NewOfferFragment extends SuperFragment {

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 12345;

    private File photoFile;

    private EditText etTags;
    private EditText etLocationLat;
    private EditText etLocationLon;
    private EditText etPrice;
    private Button btnSubmit;
    private ImageButton addImageButton;
    private LocationHelper locationHelper;
    private Location currentLocation;
    private double locationLatitude;
    private double locationLongitude;
    private boolean locationAvailable;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locationHelper = new LocationHelper(getActivity());
        currentLocation = locationHelper.getLocation();
        locationLatitude = currentLocation.getLat();
        locationLongitude = currentLocation.getLon();
        locationAvailable = locationHelper.isLocationAvailable();
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
        addImageButton = (ImageButton) view.findViewById(R.id.addImageButton);

        if (locationAvailable) {
            etLocationLat.setText(String.valueOf(locationLatitude));
            etLocationLon.setText(String.valueOf(locationLongitude));
        }

        //TODO extract
        addImageButton.setOnClickListener(new View.OnClickListener() {


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

                Uri imageCaptureUri = Uri.fromFile(getOutputMediaFile());

                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageCaptureUri);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK || requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {

            BitmapFactory.Options options = new BitmapFactory.Options();

            options.inSampleSize = 8;

            Bitmap bitmap = BitmapFactory.decodeFile(photoFile.getPath(), options);

            bitmap = ImageUtils.rotateBitmap(bitmap, photoFile);
            bitmap = ImageUtils.scaleBitmapKeepingAspectRatio(bitmap);

            addImageButton.setImageBitmap(bitmap);
        } else {
            Toast.makeText(getActivity(), R.string.camera_failed
                    , Toast.LENGTH_SHORT).show();
        }
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
