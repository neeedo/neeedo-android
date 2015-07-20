package neeedo.imimaprx.htw.de.neeedo.models;

import java.util.ArrayList;

import neeedo.imimaprx.htw.de.neeedo.entities.offer.Offer;
import neeedo.imimaprx.htw.de.neeedo.entities.offer.Offers;
import neeedo.imimaprx.htw.de.neeedo.entities.offer.SingleOffer;

public class OffersModel {

    private Offers offers;

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

    public Offer getDraft() {
        if (draft == null)
            draft = new Offer();
        return draft;
    }

    public void setDraft(Offer draft) {
        this.draft = draft;
    }

    public void addOffer(Offer offer) {
        offers.addSingleOfferOnFirstPostion(offer);
    }

    public void replaceOffer(Offer offer) {
        offers.replaceOffer(offer);
    }

}
