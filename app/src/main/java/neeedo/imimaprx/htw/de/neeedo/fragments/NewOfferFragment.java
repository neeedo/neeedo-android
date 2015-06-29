package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.io.File;
import java.util.ArrayList;

import neeedo.imimaprx.htw.de.neeedo.MainActivity;
import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.util.Location;
import neeedo.imimaprx.htw.de.neeedo.events.NewEanTagsReceivedEvent;
import neeedo.imimaprx.htw.de.neeedo.events.NewImageReceivedFromServer;
import neeedo.imimaprx.htw.de.neeedo.events.ServerResponseEvent;
import neeedo.imimaprx.htw.de.neeedo.fragments.handler.ImageUploadHandler;
import neeedo.imimaprx.htw.de.neeedo.fragments.handler.SendNewOfferHandler;
import neeedo.imimaprx.htw.de.neeedo.fragments.handler.StartCameraHandler;
import neeedo.imimaprx.htw.de.neeedo.fragments.handler.StartLocationChooserHandler;
import neeedo.imimaprx.htw.de.neeedo.fragments.handler.StartNewBarcodeScanHandler;
import neeedo.imimaprx.htw.de.neeedo.helpers.LocationHelper;
import neeedo.imimaprx.htw.de.neeedo.models.ActiveUser;
import neeedo.imimaprx.htw.de.neeedo.rest.outpan.GetOutpanByEANAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.vo.RequestCodes;


public class NewOfferFragment extends SuperFragment {
    public static final String STATE_CAMERA_OUTPUT = "new_camera_output_file";
    public static final String STATE_IMAGE_LIST = "image_file_list";

    private final ActiveUser activeUser = ActiveUser.getInstance();
    private ArrayList<String> uploadedImages = new ArrayList<String>();
    private EditText etTags;
    private EditText etLocationLat;
    private EditText etLocationLon;
    private EditText etPrice;
    private Button btnSubmit;
    private Button btnBarcode;
    private LinearLayout imagesContainer;
    private LocationHelper locationHelper;
    private Location currentLocation;
    private MapView mapView;
    private double locationLatitude;
    private double locationLongitude;
    private boolean locationAvailable;
    private Button btnSetLocation;
    private File newCameraOutputFile;
    private ArrayList<Bitmap> imageFiles;

    public void setNewCameraOutputFile(File newCameraOutputFile) {
        this.newCameraOutputFile = newCameraOutputFile;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationHelper = new LocationHelper(getActivity());

        //TODO extract and make async
        currentLocation = locationHelper.getLocation();
        locationLatitude = currentLocation.getLat();
        locationLongitude = currentLocation.getLon();
        locationAvailable = locationHelper.isLocationAvailable();

        imageFiles = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.form_offer_view, container, false);

        etTags = (EditText) view.findViewById(R.id.etTags);
        etLocationLat = (EditText) view.findViewById(R.id.etLocationLat);
        etLocationLon = (EditText) view.findViewById(R.id.etLocationLon);
        etPrice = (EditText) view.findViewById(R.id.etPrice);
        btnBarcode = (Button) view.findViewById(R.id.newOffer_Barcode_Button);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        imagesContainer = (LinearLayout) view.findViewById(R.id.imagesContainer);
        btnSetLocation = (Button) view.findViewById(R.id.btnChooseLocation);

        initOrRestore(savedInstanceState);

        if (locationAvailable) {
            etLocationLat.setText(String.valueOf(locationLatitude));
            etLocationLon.setText(String.valueOf(locationLongitude));
        }

        imagesContainer.setOnClickListener(new StartCameraHandler(this));
        btnSubmit.setOnClickListener(new SendNewOfferHandler(etTags, etLocationLat, etLocationLon, etPrice));
        btnBarcode.setOnClickListener(new StartNewBarcodeScanHandler(this));
        btnSetLocation.setOnClickListener(new StartLocationChooserHandler(this));

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
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode != Activity.RESULT_OK) {
            Toast.makeText(getActivity(), R.string.fail, Toast.LENGTH_SHORT).show();
            return;
        }

        if (requestCode == RequestCodes.CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            new ImageUploadHandler(newCameraOutputFile, getActivity()).execute();
        } else if (requestCode == RequestCodes.BARCODE_SCAN_REQUEST_CODE) {
            String barcodeEAN = intent.getStringExtra("SCAN_RESULT");
            new GetOutpanByEANAsyncTask(barcodeEAN, getActivity()).execute();
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable(STATE_CAMERA_OUTPUT, newCameraOutputFile);
        savedInstanceState.putParcelableArrayList(STATE_IMAGE_LIST, imageFiles);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        initOrRestore(savedInstanceState);
    }

    private void initOrRestore(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            newCameraOutputFile = (File) savedInstanceState.get(STATE_CAMERA_OUTPUT);
            ArrayList<Parcelable> imageFiles = savedInstanceState.getParcelableArrayList(STATE_IMAGE_LIST);
            for (Parcelable image : imageFiles) {
                this.imageFiles.add((Bitmap) image);

              //TODO  addImageButton.setImageBitmap((Bitmap) image);
            }
        }
    }

    @Subscribe
    public void handleNewEanTagsReceived(NewEanTagsReceivedEvent event) {
        etTags.setText(event.getOutpanResult().getTags());
    }

    @Subscribe
    public void handleNewImageReceivedFromServer(NewImageReceivedFromServer event) {
        Bitmap finalImageFile = event.getfinalOptimizedBitmap();
        addImage(finalImageFile);
        imageFiles.add(finalImageFile);
    }

    @Subscribe
    public void handleServerResponse(ServerResponseEvent e) {
        redirectToFragment(ListOffersFragment.class, MainActivity.MENU_LIST_OFFERS);
    }

    private void addImage(Bitmap image) {
        ImageButton imageButton = new ImageButton(getActivity());
        imageButton.setImageBitmap(image);
        LayoutParams layoutParameters = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        imageButton.setLayoutParams(layoutParameters);
        imageButton.setScaleType(ScaleType.FIT_START);
        imageButton.setAdjustViewBounds(true);
//        imageButton.setOnClickListener(ClickListener);
        imageButton.setBackgroundColor(Color.TRANSPARENT);
//        imageButton.setTag(i);
//        imageButton.setId(i);

        imagesContainer.addView(imageButton);
    }
}
