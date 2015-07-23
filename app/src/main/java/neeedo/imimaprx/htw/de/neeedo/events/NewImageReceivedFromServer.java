package neeedo.imimaprx.htw.de.neeedo.events;

import android.graphics.Bitmap;

import neeedo.imimaprx.htw.de.neeedo.fragments.OfferImage;
import neeedo.imimaprx.htw.de.neeedo.fragments.handler.OfferImageBitmap;

public class NewImageReceivedFromServer {

    private final OfferImage offerImage;

    public NewImageReceivedFromServer(OfferImage offerImage) {
        this.offerImage = offerImage;
    }

    public OfferImage getOfferImage() {
        return offerImage;
    }

}
