package neeedo.imimaprx.htw.de.neeedo.fragments.handler;

import android.app.Activity;
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

    private final Activity activity;

    public StartCameraHandler(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {
        Context context = activity;
        PackageManager packageManager = context.getPackageManager();

        if (packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA) == false) {
            Toast.makeText(activity, R.string.no_camera, Toast.LENGTH_SHORT).show();
            return;
        }

        File path = new File(Environment.getExternalStorageDirectory(), "foo/bar");
        if (!path.exists()) path.mkdirs();

        Uri imageCaptureUri = Uri.fromFile(getOutputMediaFile());

        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageCaptureUri);
        //cameraIntent.putExtra("return-data", true);
        activity.startActivityForResult(cameraIntent, RequestCodes.CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
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

}
