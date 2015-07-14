package neeedo.imimaprx.htw.de.neeedo.fragments.adapters;

import java.util.ArrayList;

import neeedo.imimaprx.htw.de.neeedo.entities.offer.Offer;

public class SwipeCardViewItem {

    private final Offer offer;

    private final String description;
    private final String title;
    private final ArrayList<String> images;

    public SwipeCardViewItem(Offer offer) {
        this.offer = offer;
        this.title = offer.getName();
        this.description = offer.getTagsString();
        this.images = offer.getImages();
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public Offer getOffer() {
        return offer;
    }
}
