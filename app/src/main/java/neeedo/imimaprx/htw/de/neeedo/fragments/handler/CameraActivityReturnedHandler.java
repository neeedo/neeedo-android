package neeedo.imimaprx.htw.de.neeedo.fragments.handler;

import android.os.AsyncTask;
import android.widget.ImageButton;

import java.io.File;

import neeedo.imimaprx.htw.de.neeedo.rest.image.PostCreateImageAsyncTask;

public class CameraActivityReturnedHandler extends AsyncTask {

    private final ImageButton addImageButton;
    private final File photoFile;

    public CameraActivityReturnedHandler(File photoFile, ImageButton addImageButton) {
        this.photoFile = photoFile;
        this.addImageButton = addImageButton;
    }


    @Override
    protected Object doInBackground(Object[] params) {
        new PostCreateImageAsyncTask(photoFile).execute();
        return null;
    }
}
