package neeedo.imimaprx.htw.de.neeedo.fragments;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

import neeedo.imimaprx.htw.de.neeedo.utils.ServerConstantsUtils;

public class OfferImage implements Serializable {
    private final String imageUrl;
    private final String imageName;

    public OfferImage(String imageName) {
        this.imageName = imageName;
        this.imageUrl = ServerConstantsUtils.getActiveServer() + "images/" + imageName;
    }

    public String getImageName() {
        return imageName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

}
