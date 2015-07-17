package neeedo.imimaprx.htw.de.neeedo.models;

import java.util.ArrayList;

import neeedo.imimaprx.htw.de.neeedo.entities.offer.Offer;
import neeedo.imimaprx.htw.de.neeedo.entities.offer.Offers;
import neeedo.imimaprx.htw.de.neeedo.entities.offer.SingleOffer;

public class OffersModel {

    private Offers offers;
    private SingleOffer singleOffer;

    private Offer draft;

    public static OffersModel getInstance() {
        if (offersModel == null)
            offersModel = new OffersModel();
        return offersModel;
    }

    private OffersModel() {
    }

    private static OffersModel offersModel;

    public ArrayList<Offer> getOffers() {
        if (offers == null) {
            offers = new Offers();
            offers.setOffers(new ArrayList<Offer>());
        }
        return offers.getOffers();
    }

    public void setOffers(Offers offers) {
        this.offers = offers;
    }

    public Offer getSingleOffer() {
        if (singleOffer == null) {
            return null;
        }
        return singleOffer.getOffer();
    }

    public void setSingleOffer(SingleOffer singleOffer) {
        this.singleOffer = singleOffer;
    }

    public Offer getDraft() {
        if (draft == null)
            draft = new Offer();
        return draft;
    }

    public void setDraft(Offer draft) {
        this.draft = draft;
    }
}
