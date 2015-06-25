package neeedo.imimaprx.htw.de.neeedo.fragments.handler;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import java.io.File;

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.fragments.NewOfferFragment;
import neeedo.imimaprx.htw.de.neeedo.fragments.SuperFragment;
import neeedo.imimaprx.htw.de.neeedo.utils.ImageUtils;
import neeedo.imimaprx.htw.de.neeedo.vo.RequestCodes;

public class StartCameraHandler implements View.OnClickListener {

    private NewOfferFragment newOfferFragment;
    private File outputFile;

    public StartCameraHandler(NewOfferFragment newOfferFragment) {
        this.newOfferFragment = newOfferFragment;
    }

    @Override
    public void onClick(View v) {
        outputFile = ImageUtils.getNewOutputImageFile();
        newOfferFragment.setNewCameraOutputFile(outputFile);

        Context context = newOfferFragment.getActivity();
        PackageManager packageManager = context.getPackageManager();

        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA) == false) {
            Toast.makeText(context, R.string.no_camera, Toast.LENGTH_SHORT).show();
            return;
        }

        Uri imageCaptureUri = Uri.fromFile(outputFile);

        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageCaptureUri);
        newOfferFragment.startActivityForResult(cameraIntent, RequestCodes.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }
}
