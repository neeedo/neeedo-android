package neeedo.imimaprx.htw.de.neeedo.utils;

import neeedo.imimaprx.htw.de.neeedo.entities.offer.Offer;
import neeedo.imimaprx.htw.de.neeedo.fragments.OfferImage;

public class OfferImageUrl extends OfferImage {
    private final String imageUrl;

    public OfferImageUrl(String imageName, String imageUrl) {
        super(imageName);
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
