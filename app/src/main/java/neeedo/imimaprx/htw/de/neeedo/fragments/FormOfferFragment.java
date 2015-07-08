package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.apmem.tools.layouts.FlowLayout;
import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.entities.util.Location;
import neeedo.imimaprx.htw.de.neeedo.events.GetSuggestionEvent;
import neeedo.imimaprx.htw.de.neeedo.events.NewEanTagsReceivedEvent;
import neeedo.imimaprx.htw.de.neeedo.events.NewImageReceivedFromServer;
import neeedo.imimaprx.htw.de.neeedo.fragments.handler.ImageUploadHandler;
import neeedo.imimaprx.htw.de.neeedo.fragments.handler.StartCameraHandler;
import neeedo.imimaprx.htw.de.neeedo.fragments.handler.StartLocationChooserHandler;
import neeedo.imimaprx.htw.de.neeedo.fragments.handler.StartNewBarcodeScanHandler;
import neeedo.imimaprx.htw.de.neeedo.rest.outpan.GetOutpanByEANAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.util.BaseAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.utils.AutocompletionOnFocusChangeListener;
import neeedo.imimaprx.htw.de.neeedo.utils.AutocompletionTextWatcher;
import neeedo.imimaprx.htw.de.neeedo.vo.RequestCodes;

public class FormOfferFragment extends FormFragment {
    protected Button btnSubmit;
    protected Button btnBarcode;
    protected LinearLayout imagesContainer;
    protected MapView mapView;
    protected Button btnSetLocation;
    protected ImageButton addImageButton;

    // stateful
    // TODO those 2
    protected MultiAutoCompleteTextView etTags;
    protected EditText etPrice;

    protected FlowLayout flTagSuggestions;

    protected File newCameraOutputFile;
    protected ArrayList<Bitmap> imageBitmaps = new ArrayList<Bitmap>();
    protected ArrayList<String> imageNamesOnServer = new ArrayList<String>();
    protected GeoPoint selectedGeoPoint;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.form_offer_view, container, false);

        etTags = (MultiAutoCompleteTextView) view.findViewById(R.id.etTags);
        etPrice = (EditText) view.findViewById(R.id.etPrice);
        flTagSuggestions = (FlowLayout) view.findViewById(R.id.flTagSuggestions);
        btnBarcode = (Button) view.findViewById(R.id.newOffer_Barcode_Button);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        imagesContainer = (LinearLayout) view.findViewById(R.id.imagesContainer);
        btnSetLocation = (Button) view.findViewById(R.id.btnChooseLocation);
        addImageButton = (ImageButton) view.findViewById(R.id.addImageButton);

        addImageButton.setOnClickListener(new StartCameraHandler(this));
        btnBarcode.setOnClickListener(new StartNewBarcodeScanHandler(this));
        btnSetLocation.setOnClickListener(new StartLocationChooserHandler(this));

        etTags.addTextChangedListener(new AutocompletionTextWatcher(this, etTags, BaseAsyncTask.CompletionType.TAG));

        etTags.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());

        etTags.addTextChangedListener(new AutocompletionTextWatcher(this, etTags, BaseAsyncTask.CompletionType.PHRASE));

        etTags.setOnFocusChangeListener(new AutocompletionOnFocusChangeListener(flTagSuggestions));

        return view;
    }

    @Override
    public void fillSuggestions(GetSuggestionEvent e) {
        super.fillSuggestions(e);

        etTags.setAdapter(completionsAdapter);

        super.showSuggestionTags(flTagSuggestions, etTags, e);

        completionsAdapter.notifyDataSetChanged();
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
            new GetOutpanByEANAsyncTask(barcodeEAN,getActivity()).execute();
        } else if (requestCode == RequestCodes.FIND_LOCATION_REQUEST_CODE) {
            String latitude = intent.getStringExtra("latitude");
            String longitude = intent.getStringExtra("longitude");
            selectedGeoPoint = new GeoPoint(Double.parseDouble(latitude), Double.parseDouble(longitude));
            setLocation(selectedGeoPoint);
        }
    }

    public void setNewCameraOutputFile(File newCameraOutputFile) {
        this.newCameraOutputFile = newCameraOutputFile;
    }

    public void handleNewEanTagsReceived(NewEanTagsReceivedEvent event) {
        etTags.setText(event.getOutpanResult().getTags());
    }

    public void handleNewImageReceivedFromServer(NewImageReceivedFromServer event) {
        String imageFileNamesOnServer = event.getImageFileNameOnServer();
        imageNamesOnServer.add(imageFileNamesOnServer);

        Bitmap finalImageFile = event.getfinalOptimizedBitmap();
        imageBitmaps.add(finalImageFile);
        addImage(finalImageFile);
    }

    protected void addImage(Bitmap image) {
        LinearLayout.LayoutParams layoutParameters = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParameters.setMargins(15, 0, 0, 0);

        ImageButton imageButton = new ImageButton(getActivity());
        imageButton.setImageBitmap(image);
        imageButton.setScaleType(ImageView.ScaleType.FIT_START);
        imageButton.setPadding(0, 0, 0, 0);
        imageButton.setAdjustViewBounds(true);
        imageButton.setLayoutParams(layoutParameters);

        imagesContainer.addView(imageButton);
    }

    protected void setLocation(final GeoPoint geoPoint) { // TODO put this into super class and use it also for demands form
        mapView = new MapView(getActivity(), null);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        mapView.getController().setZoom(18);
        mapView.setClickable(true);
        mapView.setBuiltInZoomControls(true);
        mapView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        RelativeLayout mapContainer = (RelativeLayout) getActivity().findViewById(R.id.mapContainer);
        mapContainer.setVisibility(View.VISIBLE);
        mapContainer.addView(mapView);

        ResourceProxy resourceProxy = new DefaultResourceProxyImpl(getActivity());
        ArrayList<OverlayItem> ownOverlay = new ArrayList<OverlayItem>();
        ownOverlay.add(new OverlayItem("", "", (GeoPoint) geoPoint));
        ItemizedIconOverlay userLocationOverlay = new ItemizedIconOverlay<OverlayItem>(ownOverlay, getResources().getDrawable(R.drawable.map_marker), null, resourceProxy);
        mapView.getOverlays().add(userLocationOverlay);

        // this is a hack to get around one of the osmdroid bugs
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mapView.getController().animateTo(geoPoint);
            }
        }, 200);
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
