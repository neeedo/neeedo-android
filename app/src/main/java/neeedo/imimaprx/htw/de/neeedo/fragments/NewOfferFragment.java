package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.squareup.otto.Subscribe;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.io.File;

import neeedo.imimaprx.htw.de.neeedo.LoginActivity;
import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.Location;
import neeedo.imimaprx.htw.de.neeedo.entities.SingleOffer;
import neeedo.imimaprx.htw.de.neeedo.events.NewEanNumberScannedEvent;
import neeedo.imimaprx.htw.de.neeedo.events.NewProductInfosRequestedEvent;
import neeedo.imimaprx.htw.de.neeedo.events.ServerResponseEvent;
import neeedo.imimaprx.htw.de.neeedo.fragments.handler.SendNewOfferHandler;
import neeedo.imimaprx.htw.de.neeedo.fragments.handler.StartCameraHandler;
import neeedo.imimaprx.htw.de.neeedo.helpers.LocationHelper;
import neeedo.imimaprx.htw.de.neeedo.models.ActiveUser;
import neeedo.imimaprx.htw.de.neeedo.models.OffersModel;
import neeedo.imimaprx.htw.de.neeedo.rest.HttpGetOutpanByEANAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.utils.ImageUtils;
import neeedo.imimaprx.htw.de.neeedo.vo.RequestCodes;


public class NewOfferFragment extends SuperFragment {
    private final ActiveUser activeUser = ActiveUser.getInstance();
    private EditText etTags;
    private EditText etLocationLat;
    private EditText etLocationLon;
    private EditText etPrice;
    private Button btnSubmit;
    private Button btnBarcode;
    private ImageButton addImageButton;
    private LocationHelper locationHelper;
    private Location currentLocation;
    private MapView mapView;
    private double locationLatitude;
    private double locationLongitude;
    private boolean locationAvailable;
    private File photoFile;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationHelper = new LocationHelper(getActivity());

        //TODO extract and make async
        currentLocation = locationHelper.getLocation();
        locationLatitude = currentLocation.getLat();
        locationLongitude = currentLocation.getLon();
        locationAvailable = locationHelper.isLocationAvailable();

        if (!activeUser.userInformationLoaded()) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        }
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

        photoFile = ImageUtils.getNewOutputImageFile();

        btnBarcode = (Button) view.findViewById(R.id.newOffer_Barcode_Button);

        addImageButton.setOnClickListener(new StartCameraHandler(this, photoFile));
        btnSubmit.setOnClickListener(new SendNewOfferHandler(etTags, etLocationLat, etLocationLon, etPrice));


        btnBarcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IntentIntegrator integrator = new IntentIntegrator(getActivity());
                integrator.setDesiredBarcodeFormats(IntentIntegrator.PRODUCT_CODE_TYPES);
                integrator.setPrompt("Scan a barcode");
                integrator.setResultDisplayDuration(0);
                //integrator.setWide();  // Wide scanning rectangle, may work better for 1D barcodes
                integrator.setCameraId(0);  // Use a specific camera of the device
                integrator.initiateScan();

            }
        });


        return view;
    }

    @Override
    public void onActivityCreated(final Bundle savedState) {
        super.onActivityCreated(savedState);

        mapView = new MapView(getActivity(), null);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        mapView.getController().setZoom(12);
        mapView.setClickable(true);
        mapView.setBuiltInZoomControls(true);
        mapView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        RelativeLayout mapContainer = (RelativeLayout) getActivity().findViewById(R.id.mapContainer);
        mapContainer.addView(mapView);

        //this is a hack to get around one of the osmdroid bugs
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mapView.getController().animateTo(new GeoPoint(52468277, 13425979));
            }
        }, 200);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK || requestCode == RequestCodes.CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {

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
    public void handleServerResponse(ServerResponseEvent e) {
        redirectToListFragment();
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
