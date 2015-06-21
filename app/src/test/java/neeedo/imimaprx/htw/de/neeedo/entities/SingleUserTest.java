package neeedo.imimaprx.htw.de.neeedo.entities;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import neeedo.imimaprx.htw.de.neeedo.entities.offer.Offer;
import neeedo.imimaprx.htw.de.neeedo.entities.offer.SingleOffer;
import neeedo.imimaprx.htw.de.neeedo.entities.util.Location;

public class SingleUserTest extends Assert {

    private SingleOffer singleOffer;
    private Offer offer;

    @Before
    public void setUp() {

        offer = new Offer();
        offer.setPrice(55d);
        offer.setLocation(new Location(6, 6));
        offer.setId("testlauf");
        offer.setVersion(6);
        offer.setUserId("some number");

        ArrayList<String> list = new ArrayList<>();
        list.add("zeugs");
        list.add("other zeugs");

        offer.setTags(list);

        singleOffer = new SingleOffer();
        singleOffer.setOffer(offer);

    }


    @Test
    public void testGetOffer() throws Exception {

        assertTrue(singleOffer.getOffer().getId().equals(offer.getId()));


    }

    @Test
    public void testSetOffer() throws Exception {

        Offer localOffer = new Offer();
        localOffer.setPrice(55d);
        localOffer.setLocation(new Location(6, 6));
        localOffer.setId("blakeks");
        localOffer.setVersion(6);
        localOffer.setUserId("some number");

        ArrayList<String> list = new ArrayList<>();
        list.add("zeugs");
        list.add("other zeugs");

        localOffer.setTags(list);

        singleOffer.setOffer(localOffer);

        assertFalse(singleOffer.getOffer().getId().equals(offer.getId()));


    }


}