package neeedo.imimaprx.htw.de.neeedo.entities.offer;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.ArrayList;

import neeedo.imimaprx.htw.de.neeedo.entities.util.BaseEntity;

@Root(name = "offers")
public class Offers implements Serializable, BaseEntity {

    @Element
    private ArrayList<Offer> offers = new ArrayList<>();

    public Offers() {
    }

    public ArrayList<Offer> getOffers() {
        return offers;
    }

    public void setOffers(ArrayList<Offer> offers) {
        this.offers = offers;
    }


    public void addOffer(Offer offer) {
        offers.add(0, offer);
    }

    public void replaceOffer(Offer offer) {

        String id = offer.getId();
        boolean notfound = true;

        for (int i = 0; i < offers.size(); i++) {
            if (offers.get(i).getId().equals(id)) {
                offers.set(i, offer);
                notfound = false;
            }
        }
        if (notfound) {
            offers.add(offer);
        }

    }

    public Offer getOfferByID(String id) {
        Offer offer = null;
        for (Offer o : offers) {
            if (o.getId().equals(id))
                offer = o;
        }
        return offer;
    }

    public void removeByID(String id) {
        Offer toRemove = null;
        for (Offer o : offers) {
            if (o.getId().equals(id)) {
                toRemove = o;
            }
        }
        if (toRemove != null) {
            offers.remove(toRemove);
        }
    }

    public boolean checkIfOfferWithIdExists(String id) {
        boolean temp = false;

        for (Offer offer : offers) {
            if (offer.getId().equals(id)) {
                temp = true;
                break;
            }
        }
        return temp;
    }

    @Override
    public String toString() {
        return "Offers{" +
                "soffers=" + offers +
                '}';
    }

}
