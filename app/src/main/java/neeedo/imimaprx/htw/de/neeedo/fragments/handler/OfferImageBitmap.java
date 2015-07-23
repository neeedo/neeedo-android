package neeedo.imimaprx.htw.de.neeedo.fragments.handler;

import android.graphics.Bitmap;

import neeedo.imimaprx.htw.de.neeedo.entities.offer.Offer;
import neeedo.imimaprx.htw.de.neeedo.fragments.OfferImage;

public class OfferImageBitmap extends OfferImage {
    private final Bitmap imageBitmap;

    public OfferImageBitmap(String imageName, Bitmap imageBitmap) {
        super(imageName);
        this.imageBitmap = imageBitmap;
    }

    public Bitmap getImageBitmap() {
        return imageBitmap;
    }
}
