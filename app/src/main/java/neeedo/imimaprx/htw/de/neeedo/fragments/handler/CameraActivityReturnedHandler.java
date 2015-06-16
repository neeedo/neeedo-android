package neeedo.imimaprx.htw.de.neeedo.fragments.handler;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageButton;

import java.io.File;

import neeedo.imimaprx.htw.de.neeedo.entities.Image;
import neeedo.imimaprx.htw.de.neeedo.rest.image.PostCreateImageAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.utils.ImageUtils;

public class CameraActivityReturnedHandler extends AsyncTask {

    private final ImageButton addImageButton;
    private final File photoFile;

    public CameraActivityReturnedHandler(File photoFile, ImageButton addImageButton) {
        this.photoFile = photoFile;
        this.addImageButton = addImageButton;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        BitmapFactory.Options options = new BitmapFactory.Options();

        options.inSampleSize = 8;

        Bitmap bitmap = BitmapFactory.decodeFile(photoFile.getPath(), options);

        bitmap = ImageUtils.rotateBitmap(bitmap, photoFile);
        bitmap = ImageUtils.scaleBitmapKeepingAspectRatio(bitmap);

        addImageButton.setImageBitmap(bitmap);
    }

    @Override
    protected Object doInBackground(Object[] params) {
        new PostCreateImageAsyncTask(photoFile).execute();
        return null;
    }
}
