package neeedo.imimaprx.htw.de.neeedo.entities;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import neeedo.imimaprx.htw.de.neeedo.entities.offer.Offer;
import neeedo.imimaprx.htw.de.neeedo.entities.util.Location;

public class OfferTest extends Assert {

    private Offer offer;
    private Offer original;


    @Before
    public void setUp() throws Exception {

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


        original = new Offer();
        original.setPrice(55d);
        original.setLocation(new Location(6, 6));
        original.setId("testlauf");
        original.setVersion(6);
        original.setUserId("some number");
        original.setTags(list);

    }

    @Test
    public void testGetId() throws Exception {

        assertTrue(offer.getId().equals(original.getId()));

    }

    @Test
    public void testSetId() throws Exception {

        offer.setId("stuff");

        assertFalse(offer.getId().equals(original.getId()));

    }

    @Test
    public void testGetLocation() throws Exception {

        Location location = offer.getLocation();
        Location orgLocation = original.getLocation();

        assertTrue(location.getLat() == orgLocation.getLat() & location.getLon() == orgLocation.getLon());

    }

    @Test
    public void testSetLocation() throws Exception {

        offer.setLocation(new Location(777, 777));

        Location location = offer.getLocation();
        Location orgLocation = original.getLocation();


        assertFalse(location.getLat() == orgLocation.getLat() & location.getLon() == orgLocation.getLon());


    }

    @Test
    public void testGetPrice() throws Exception {

        assertTrue(offer.getPrice().longValue() == original.getPrice().longValue());

    }

    @Test
    public void testSetPrice() throws Exception {

        offer.setPrice(98656d);

        assertFalse(offer.getPrice().longValue() == original.getPrice().longValue());

    }

    @Test
    public void testGetUserId() throws Exception {

        assertTrue(offer.getUserId().equals(original.getUserId()));


    }

    @Test
    public void testSetUserId() throws Exception {

        offer.setUserId("stuff");

        assertFalse(offer.getUserId().equals(original.getUserId()));


    }

    @Test
    public void testGetVersion() throws Exception {

        assertTrue(offer.getVersion() == original.getVersion());

    }

    @Test
    public void testSetVersion() throws Exception {

        offer.setVersion(77);

        assertFalse(offer.getVersion() == original.getVersion());

    }

    @Test
    public void testGetTags() throws Exception {

        assertTrue(offer.getTags().get(0).equals(original.getTags().get(0)));

    }

    @Test
    public void testSetTags() throws Exception {

        ArrayList<String> tags = new ArrayList<>();
        tags.add("askdjfbjasndf");

        offer.setTags(tags);

        assertFalse(offer.getTags().get(0).equals(original.getTags().get(0)));


    }


}