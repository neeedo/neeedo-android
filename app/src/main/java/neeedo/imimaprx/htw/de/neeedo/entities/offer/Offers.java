package neeedo.imimaprx.htw.de.neeedo.entities.offer;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.ArrayList;

import neeedo.imimaprx.htw.de.neeedo.entities.util.BaseEntity;

@Root(name = "offers")
public class Offers implements Serializable, BaseEntity {

    @Element
    private ArrayList<Offer> offers;

    public Offers() {
    }

    public ArrayList<Offer> getOffers() {
        return offers;
    }

    public void setOffers(ArrayList<Offer> offers) {
        this.offers = offers;
    }

    @Override
    public String toString() {
        return "Offers{" +
                "soffers=" + offers +
                '}';
    }

    public void addSingleOfferOnFirstPostion(Offer offer) {
        if (offers == null) {
            offers = new ArrayList<>();
        }

        offers.add(0, offer);
    }

    public void replaceOffer(Offer offer) {

        String id = offer.getId();

        for (int i = 0; i < offers.size(); i++) {
            if (offers.get(i).getId().equals(id)) {
                offers.set(i, offer);
            }
        }

    }

}
