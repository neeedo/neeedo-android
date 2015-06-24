package neeedo.imimaprx.htw.de.neeedo.events;

import android.graphics.Bitmap;

public class NewImageReceivedFromServer {
    private final String imageFileNameOnServer;
    private final Bitmap finalOptimizedBitmap;

    public NewImageReceivedFromServer(String imageFileNameOnServer, Bitmap finalOptimizedBitmap) {
        this.imageFileNameOnServer = imageFileNameOnServer;
        this.finalOptimizedBitmap = finalOptimizedBitmap;
    }
}
