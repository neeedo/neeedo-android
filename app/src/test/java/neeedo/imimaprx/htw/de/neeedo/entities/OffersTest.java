package neeedo.imimaprx.htw.de.neeedo.entities;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class OffersTest extends Assert {

    private Offers offers;
    private boolean alreadySetUp = false;


    @Before
    public void setUp() {

        if (alreadySetUp) return;
        offers = new Offers();

        ArrayList<Offer> list = new ArrayList<>();
        Offer offer = new Offer();
        offer.setId("ekek");
        offer.setLocation(new Location(66, 66));
        offer.setPrice(77d);
        list.add(offer);

        offers.setOffers(list);


    }

    @Test
    public void testGetOffers() throws Exception {
        assertFalse(offers.getOffers().isEmpty());

    }

    @Test
    public void testSetOffers() throws Exception {

        ArrayList<Offer> list = new ArrayList<>();
        Offer offer = new Offer();
        offer.setId("temp");
        offer.setLocation(new Location(46, 68));
        offer.setPrice(67d);
        list.add(offer);

        offers.setOffers(list);

        assertTrue(offers.getOffers().get(0).getId().equals("temp"));

    }


}