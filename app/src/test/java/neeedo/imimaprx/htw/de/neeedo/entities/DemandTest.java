package neeedo.imimaprx.htw.de.neeedo.entities;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import neeedo.imimaprx.htw.de.neeedo.entities.Demand;
import neeedo.imimaprx.htw.de.neeedo.entities.Location;
import neeedo.imimaprx.htw.de.neeedo.entities.Price;

public class DemandTest extends Assert {

    private Demand demand;
    private Demand original;

    @Before
    public void setUp() throws Exception {
        demand = new Demand();
        demand.setPrice(new Price(5, 5));
        demand.setId("stuff");
        demand.setDistance(55);
        demand.setLocation(new Location(6, 60));

        ArrayList<String> list = new ArrayList<>();
        list.add("zeugs");
        list.add("pink");
        demand.setMustTags(list);
        demand.setShouldTags(list);

        original = new Demand();
        original.setPrice(new Price(5, 5));
        original.setId("stuff");
        original.setDistance(55);
        original.setLocation(new Location(6, 60));
        original.setMustTags(list);
        original.setShouldTags(list);


    }

    @Test
    public void testGetId() throws Exception {
        assertTrue(demand.getId().equals(original.getId()));
    }

    @Test
    public void testSetId() throws Exception {
        demand.setId("other stuff");
        assertTrue(demand.getId().equals("other stuff"));
    }

    @Test
    public void testGetLocation() throws Exception {
        Location loc = demand.getLocation();
        Location loc2 = original.getLocation();

        assertTrue((loc.getLat() == loc2.getLat()) & (loc.getLon() == loc2.getLon()));

    }

    @Test
    public void testSetLocation() throws Exception {
        demand.setLocation(new Location(5, 5));
        assertTrue(demand.getLocation().getLat() == 5);

    }

    @Test
    public void testGetDistance() throws Exception {

        assertTrue(demand.getDistance() == original.getDistance());

    }

    @Test
    public void testSetDistance() throws Exception {


        demand.setDistance(5);
        assertFalse(demand.getDistance() == original.getDistance());
    }

    @Test
    public void testGetPrice() throws Exception {
        assertTrue(demand.getPrice().getMax() == original.getPrice().getMax());
    }

    @Test
    public void testSetPrice() throws Exception {
        demand.setPrice(new Price(85, 85));
        assertFalse(demand.getPrice().getMax() == original.getPrice().getMax());

    }

    @Test
    public void testGetUserId() throws Exception {

        assertTrue(demand.getId().equals(original.getId()));

    }

    @Test
    public void testSetUserId() throws Exception {

        demand.setId("keks");

        assertFalse(demand.getId().equals(original.getId()));

    }

    @Test
    public void testGetMustTags() throws Exception {
        assertTrue(demand.getMustTags().get(0).equals(original.getMustTags().get(0)));
    }

    @Test
    public void testSetMustTags() throws Exception {

        ArrayList<String> list = new ArrayList<>();
        list.add("asdfasdf");

        demand.setMustTags(list);

        assertFalse(demand.getMustTags().get(0).equals(original.getMustTags().get(0)));

    }

    @Test
    public void testGetShouldTags() throws Exception {
        assertTrue(demand.getShouldTags().get(0).equals(original.getShouldTags().get(0)));

    }

    @Test
    public void testSetShouldTags() throws Exception {
        ArrayList<String> list = new ArrayList<>();
        list.add("asdfasdf");

        demand.setShouldTags(list);

        assertFalse(demand.getShouldTags().get(0).equals(original.getShouldTags().get(0)));

    }


}