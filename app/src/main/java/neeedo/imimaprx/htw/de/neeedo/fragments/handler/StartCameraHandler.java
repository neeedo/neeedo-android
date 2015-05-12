package neeedo.imimaprx.htw.de.neeedo.fragments.handler;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.vo.RequestCodes;

public class StartCameraHandler implements View.OnClickListener {

    private final Fragment fragment;
    private final File outputFile;

    public StartCameraHandler( Fragment fragment, File outputFile) {
        this.fragment = fragment;
        this.outputFile = outputFile;
    }

    @Override
    public void onClick(View v) {
        Context context = fragment.getActivity();
        PackageManager packageManager = context.getPackageManager();

        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA) == false) {
            Toast.makeText(context, R.string.no_camera, Toast.LENGTH_SHORT).show();
            return;
        }

        File path = new File(Environment.getExternalStorageDirectory(), "foo/bar");
        if (!path.exists()) path.mkdirs();

        Uri imageCaptureUri = Uri.fromFile(outputFile);

        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageCaptureUri);
        //cameraIntent.putExtra("return-data", true);
        fragment.startActivityForResult(cameraIntent, RequestCodes.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }


}
