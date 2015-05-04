package neeedo.imimaprx.htw.de.neeedo.utils;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.File;
import java.io.IOException;

public class ImageUtils {

    public static Bitmap rotateBitmap(Bitmap bitmap, File photoFile) {
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(photoFile.getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        int orientation = orientString != null ? Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
        int rotationAngle = 0;

        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;

        Matrix matrix = new Matrix();
        matrix.setRotate(rotationAngle, (float) bitmap.getWidth() / 2, (float) bitmap.getHeight() / 2);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        return rotatedBitmap;
    }

    public static Bitmap scaleBitmapKeepingAspectRatio(Bitmap bitmap) {
        float ratio = Math.min((float) 502 / bitmap.getWidth(), (float) 375 / bitmap.getHeight());
        int width = Math.round(ratio * bitmap.getWidth());
        int height = Math.round(ratio * bitmap.getHeight());

        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }

}