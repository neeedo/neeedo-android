package neeedo.imimaprx.htw.de.neeedo.entities;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Root(name = "offer")
public class SingleOffer implements Serializable {

    @Element
    private Offer offer;

    public Offer getDemand() {
        return offer;
    }

    public void setDemand(Offer offer) {
        this.offer = offer;
    }

    @Override
    public String toString() {
        return "SingleOffer{" +
                "offer=" + offer +
                '}';
    }
}
