package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.squareup.otto.Subscribe;

import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.io.File;
import java.util.ArrayList;

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.events.NewEanTagsReceivedEvent;
import neeedo.imimaprx.htw.de.neeedo.events.NewImageReceivedFromServer;
import neeedo.imimaprx.htw.de.neeedo.fragments.handler.ImageUploadHandler;
import neeedo.imimaprx.htw.de.neeedo.fragments.handler.SendNewOfferHandler;
import neeedo.imimaprx.htw.de.neeedo.fragments.handler.StartCameraHandler;
import neeedo.imimaprx.htw.de.neeedo.fragments.handler.StartLocationChooserHandler;
import neeedo.imimaprx.htw.de.neeedo.fragments.handler.StartNewBarcodeScanHandler;
import neeedo.imimaprx.htw.de.neeedo.rest.outpan.GetOutpanByEANAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.vo.RequestCodes;


public class NewOfferFragment extends SuperFragment {

    private EditText etTags;
    private EditText etPrice;
    private Button btnSubmit;
    private Button btnBarcode;
    private LinearLayout imagesContainer;
    private MapView mapView;
    private Button btnSetLocation;
    private ImageButton addImageButton;

    //stateful
    private File newCameraOutputFile;
    private ArrayList<Bitmap> imageBitmaps = new ArrayList<Bitmap>();
    private ArrayList<String> imageNamesOnServer = new ArrayList<String>();
    private GeoPoint selectedGeoPoint;

    public void setNewCameraOutputFile(File newCameraOutputFile) {
        this.newCameraOutputFile = newCameraOutputFile;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.form_offer_view, container, false);

        etTags = (EditText) view.findViewById(R.id.etTags);
        etPrice = (EditText) view.findViewById(R.id.etPrice);
        btnBarcode = (Button) view.findViewById(R.id.newOffer_Barcode_Button);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        imagesContainer = (LinearLayout) view.findViewById(R.id.imagesContainer);
        btnSetLocation = (Button) view.findViewById(R.id.btnChooseLocation);
        addImageButton = (ImageButton) view.findViewById(R.id.addImageButton);

        addImageButton.setOnClickListener(new StartCameraHandler(this));
        btnSubmit.setOnClickListener(new SendNewOfferHandler(this));
        btnBarcode.setOnClickListener(new StartNewBarcodeScanHandler(this));
        btnSetLocation.setOnClickListener(new StartLocationChooserHandler(this));

        return view;
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        initOrRestore(savedInstanceState);
    }

    private void initOrRestore(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            newCameraOutputFile = (File) savedInstanceState.getSerializable("newCameraOutputFile");
            selectedGeoPoint = (GeoPoint) savedInstanceState.getSerializable("selectedGeoPoint");
            imageNamesOnServer = (ArrayList<String>) savedInstanceState.getSerializable("imageNamesOnServer");
            imageBitmaps = (ArrayList<Bitmap>) savedInstanceState.getSerializable("imageBitmaps");

            for (Bitmap image : imageBitmaps) {
                addImage(image);
            }

            if (selectedGeoPoint != null) {
                setLocation(selectedGeoPoint);
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putSerializable("newCameraOutputFile", newCameraOutputFile);
        savedInstanceState.putSerializable("selectedGeoPoint", selectedGeoPoint);
        savedInstanceState.putSerializable("imageBitmaps", imageBitmaps);
        savedInstanceState.putSerializable("imageNamesOnServer", imageNamesOnServer);

        super.onSaveInstanceState(savedInstanceState);
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
        } else if (requestCode == RequestCodes.FIND_LOCATION_REQUEST_CODE) {
            String latitude = intent.getStringExtra("latitude");
            String longitude = intent.getStringExtra("longitude");
            selectedGeoPoint = new GeoPoint(Double.parseDouble(latitude), Double.parseDouble(longitude));
            setLocation(selectedGeoPoint);
        }
    }

    @Subscribe
    public void handleNewEanTagsReceived(NewEanTagsReceivedEvent event) {
        etTags.setText(event.getOutpanResult().getTags());
    }

    @Subscribe
    public void handleNewImageReceivedFromServer(NewImageReceivedFromServer event) {
        String imageFileNamesOnServer = event.getImageFileNameOnServer();
        imageNamesOnServer.add(imageFileNamesOnServer);

        Bitmap finalImageFile = event.getfinalOptimizedBitmap();
        imageBitmaps.add(finalImageFile);
        addImage(finalImageFile);
    }

    private void addImage(Bitmap image) {
        LayoutParams layoutParameters = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);

        ImageButton imageButton = new ImageButton(getActivity());
        imageButton.setImageBitmap(image);
        imageButton.setLayoutParams(layoutParameters);
        imageButton.setScaleType(ScaleType.FIT_START);
        imageButton.setAdjustViewBounds(true);

        imagesContainer.addView(imageButton);
    }

    private void setLocation(final GeoPoint geoPoint) {
        mapView = new MapView(getActivity(), null);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        mapView.getController().setZoom(18);
        mapView.setClickable(true);
        mapView.setBuiltInZoomControls(true);
        mapView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        RelativeLayout mapContainer = (RelativeLayout) getActivity().findViewById(R.id.mapContainer);
        mapContainer.setVisibility(View.VISIBLE);
        mapContainer.addView(mapView);

        ResourceProxy resourceProxy = new DefaultResourceProxyImpl(getActivity());
        ArrayList<OverlayItem> ownOverlay = new ArrayList<OverlayItem>();
        ownOverlay.add(new OverlayItem("", "", (GeoPoint) geoPoint));
        ItemizedIconOverlay userLocationOverlay = new ItemizedIconOverlay<OverlayItem>(ownOverlay, getResources().getDrawable(R.drawable.map_marker), null, resourceProxy);
        mapView.getOverlays().add(userLocationOverlay);

        //this is a hack to get around one of the osmdroid bugs
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mapView.getController().animateTo(geoPoint);
            }
        }, 200);
    }
}
