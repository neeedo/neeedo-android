package neeedo.imimaprx.htw.de.neeedo.models;

import java.util.ArrayList;

import neeedo.imimaprx.htw.de.neeedo.entities.Offer;
import neeedo.imimaprx.htw.de.neeedo.entities.Offers;
import neeedo.imimaprx.htw.de.neeedo.entities.SingleOffer;

public class OffersModel {

    private Offers offers;
    private SingleOffer singleOffer;
    private Offer postOffer;
    private ArrayList<String> imageUrlList;

    public static OffersModel getInstance() {
        if (offersModel == null)
            offersModel = new OffersModel();
        return offersModel;
    }


    private OffersModel() {

    }

    private static OffersModel offersModel;

    public Offers getOffers() {
        if (offers == null) {
            offers = new Offers();
            offers.setOffers(new ArrayList<Offer>());
        }
        return offers;
    }

    public void setOffers(Offers offers) {
        this.offers = offers;
    }

    public SingleOffer getSingleOffer() {
        return singleOffer;
    }

    public void setSingleOffer(SingleOffer singleOffer) {
        this.singleOffer = singleOffer;
    }

    public Offer getPostOffer() {
        return postOffer;
    }

    public void setPostOffer(Offer postOffer) {
        this.postOffer = postOffer;
    }

    public ArrayList<String> getImageUrlList() {
        return imageUrlList;
    }

    public void setImageUrlList(ArrayList<String> imageUrlList) {
        this.imageUrlList = imageUrlList;
    }
}
