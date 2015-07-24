package neeedo.imimaprx.htw.de.neeedo.models;

import java.util.ArrayList;

import neeedo.imimaprx.htw.de.neeedo.entities.offer.Offer;
import neeedo.imimaprx.htw.de.neeedo.entities.offer.Offers;

public class OffersModel {

    private Offers offers = new Offers();

    private Offer draft;

    private boolean useLocalList = false;

    private String lastDeletedEntityId = "";

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
        offers.addOffer(offer);
    }

    public boolean isUseLocalList() {
        return useLocalList;
    }

    public void setUseLocalList(boolean useLocalList) {
        this.useLocalList = useLocalList;
    }

    public void replaceOffer(Offer offer) {
        offers.replaceOffer(offer);
    }

    public void removeOfferByID(String id) {
        offers.removeByID(id);
    }

    public void appendOffers(ArrayList<Offer> newOffers) {
        for (Offer newOffer : newOffers) {
            if (!offers.checkIfOfferWithIdExists(newOffer.getId())) {
                if (lastDeletedEntityId.equals(newOffer.getId()))
                    continue;
                offers.addOffer(newOffer);
            } else {
                replaceOffer(newOffer);
            }
        }
    }

    public void clearOffers() {
        offers.setOffers(new ArrayList<Offer>());
    }

    public String getLastDeletedEntityId() {
        return lastDeletedEntityId;
    }

    public void setLastDeletedEntityId(String lastDeletedEntityId) {
        this.lastDeletedEntityId = lastDeletedEntityId;
    }
}
