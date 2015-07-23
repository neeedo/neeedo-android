package neeedo.imimaprx.htw.de.neeedo.fragments;

import neeedo.imimaprx.htw.de.neeedo.entities.offer.Offer;

abstract public class OfferImage {
    private String imageName;

    public OfferImage(String imageName ){
        this.imageName = imageName;
    }

    public String getImageName() {
        return imageName;
    }
}
