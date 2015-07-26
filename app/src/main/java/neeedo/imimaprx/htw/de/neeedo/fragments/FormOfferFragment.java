package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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

import com.squareup.picasso.Picasso;

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
import neeedo.imimaprx.htw.de.neeedo.vo.OfferImage;
import neeedo.imimaprx.htw.de.neeedo.vo.RequestCodes;

public class FormOfferFragment extends FormFragment {
    protected Button btnSubmit;
    protected ImageButton btnBarcode;
    protected LinearLayout imagesContainer;
    protected Button btnSetLocation;
    protected ImageButton addImageButton;
    protected ArrayList<OfferImage> images = new ArrayList<OfferImage>();

    protected MultiAutoCompleteTextView etTags;
    protected EditText etPrice;

    protected FlowLayout flTagSuggestions;

    protected RelativeLayout mapContainer;

    protected File newCameraOutputFile;
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
        btnBarcode = (ImageButton) view.findViewById(R.id.newOffer_Barcode_Button);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        imagesContainer = (LinearLayout) view.findViewById(R.id.imagesContainer);
        btnSetLocation = (Button) view.findViewById(R.id.btnChooseLocation);
        addImageButton = (ImageButton) view.findViewById(R.id.addImageButton);
        mapContainer = (RelativeLayout) view.findViewById(R.id.mapContainer);

        addImageButton.setOnClickListener(new StartCameraHandler(this));
        btnBarcode.setOnClickListener(new StartNewBarcodeScanHandler(this));
        btnSetLocation.setOnClickListener(new StartLocationChooserHandler(this, false));

        etTags.addTextChangedListener(new AutocompletionTextWatcher(this, etTags, null, BaseAsyncTask.CompletionType.TAG));
        etTags.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        etTags.addTextChangedListener(new AutocompletionTextWatcher(this, etTags, null, BaseAsyncTask.CompletionType.PHRASE));
        etTags.setOnFocusChangeListener(new AutocompletionOnFocusChangeListener(flTagSuggestions));

        validation();

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
        if (savedInstanceState != null) {
            newCameraOutputFile = (File) savedInstanceState.getSerializable("newCameraOutputFile");
            selectedGeoPoint = (GeoPoint) savedInstanceState.getSerializable("selectedGeoPoint");
            ArrayList<OfferImage> imagesRestored = (ArrayList<OfferImage>) savedInstanceState.getSerializable("images");

            for (OfferImage offerImage : imagesRestored) {
                addImage(offerImage);
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
        savedInstanceState.putSerializable("images", images);

        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        if (requestCode == RequestCodes.CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            new ImageUploadHandler(newCameraOutputFile, getActivity()).execute();
        } else if (requestCode == RequestCodes.BARCODE_SCAN_REQUEST_CODE) {
            String barcodeEAN = intent.getStringExtra("SCAN_RESULT");
            new GetOutpanByEANAsyncTask(barcodeEAN, getActivity()).execute();
        } else if (requestCode == RequestCodes.FIND_LOCATION_REQUEST_CODE) {
            selectedGeoPoint = new GeoPoint(
                    Double.parseDouble(intent.getStringExtra("latitude")),
                    Double.parseDouble(intent.getStringExtra("longitude")));
            setLocation(selectedGeoPoint);
            btnSetLocation.setError(null);
        }
    }

    public void setNewCameraOutputFile(File newCameraOutputFile) {
        this.newCameraOutputFile = newCameraOutputFile;
    }

    public void handleNewEanTagsReceived(NewEanTagsReceivedEvent event) {
        etTags.setText(event.getOutpanResult().getTags());
    }

    public void handleNewImageReceivedFromServer(NewImageReceivedFromServer event) {
        addImage(event.getOfferImage());
    }

    protected void addImage(final OfferImage offerImage) {
        images.add(offerImage);

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final RelativeLayout relativeLayout = (RelativeLayout) inflater.inflate(R.layout.new_offer_image_wrapper, null);

        ImageView imageView = (ImageView) relativeLayout.findViewById(R.id.new_offer_image);

        Picasso.with(getActivity()).load(offerImage.getImageUrl()).into(imageView);

        ImageButton deleteButton = (ImageButton) relativeLayout.findViewById(R.id.new_offer_image_delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ViewGroup) relativeLayout.getParent()).removeView(relativeLayout);
                images.remove(offerImage);
            }
        });
        imagesContainer.addView(relativeLayout, 1);
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

    public ArrayList<String> getImageNames() {
        ArrayList<String> imagesNames = new ArrayList<String>();
        for (OfferImage offerImage : images) {
            imagesNames.add(offerImage.getImageName());
        }
        return imagesNames;
    }

    protected void setLocation(final GeoPoint geoPoint) {
        final MapView mapView = new MapView(getActivity(), null);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        mapView.getController().setZoom(18);
        mapView.setClickable(true);
        mapView.setBuiltInZoomControls(true);
        mapView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

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

    protected void validation() {
        validViews.put(etTags, false);
        validViews.put(etPrice, false);
        validViews.put(btnSetLocation, false);

        etTags.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                String tags = etTags.getText().toString();
                if (!focus) {
                    if (tags.length() <= 0) {
                        etTags.setError(getResources().getString(R.string.validation_no_tag));
                    }
                    if (etTags.getError() == null) {
                        validViews.put(etTags, true);
                    }
                }
            }
        });
        etPrice.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                String price = etPrice.getText().toString();
                if (!focus) {
                    if (price.length() > 0 && Double.valueOf(price) < 0) {
                        etPrice.setError(getResources().getString(R.string.validation_value_negative));
                    }
                    if (price.length() > 0 && !price.matches("(\\d+(.(\\d{2}|\\d{0}))|\\d)")) {
                        etPrice.setError(getResources().getString(R.string.validation_price_number_format));
                    }
                    if (price.length() == 0) {
                        etPrice.setError(getResources().getString(R.string.validation_empty_field));
                    }
                    if (etPrice.getError() == null) {
                        validViews.put(etPrice, true);
                    }
                }
            }
        });
        btnSetLocation.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                if (selectedGeoPoint == null) {
                    btnSetLocation.setError(getResources().getString(R.string.validation_no_location));
                }
                if (btnSetLocation.getError() == null) {
                    validViews.put(btnSetLocation, true);
                }
            }
        });
    }

    public boolean checkValidation() {
        // force re-focus all textviews for showing errors from above
        etTags.requestFocusFromTouch();
        etPrice.requestFocusFromTouch();
        btnSetLocation.requestFocusFromTouch();
        btnSubmit.requestFocusFromTouch();

        if (validViews.containsValue(false)) {
            Log.d("Validation", "Invalid");
            return false;
        } else {
            Log.d("Validation", "Valid");
            return true;
        }
    }
}
