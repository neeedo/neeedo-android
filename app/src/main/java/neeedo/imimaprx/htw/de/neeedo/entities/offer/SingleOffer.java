package neeedo.imimaprx.htw.de.neeedo.entities.offer;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

import neeedo.imimaprx.htw.de.neeedo.entities.util.BaseEntity;

@Root(name = "offer")
public class SingleOffer implements Serializable, BaseEntity {

    @Element
    private Offer offer;

    public Offer getOffer() {
        return offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    @Override
    public String toString() {
        return "SingleOffer{" +
                "offer=" + offer +
                '}';
    }
}
