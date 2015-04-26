package neeedo.imimaprx.htw.de.neeedo.entities;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

public class SingleDemandTest extends Assert {

    private SingleDemand singleDemand;
    private Demand demand;

    @Before
    public void setUp() {

        singleDemand = new SingleDemand();

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

        singleDemand.setDemand(demand);

    }


    @Test
    public void testGetDemand() throws Exception {

        assertTrue(singleDemand.getDemand().getId().equals(demand.getId()));

    }

    @Test
    public void testSetDemand() throws Exception {

        Demand locDemand = new Demand();
        locDemand.setPrice(new Price(5, 5));
        locDemand.setId("Was anderes");
        locDemand.setDistance(55);
        locDemand.setLocation(new Location(6, 60));

        ArrayList<String> list = new ArrayList<>();
        list.add("zeugs");
        list.add("pink");
        locDemand.setMustTags(list);
        locDemand.setShouldTags(list);

        singleDemand.setDemand(locDemand);

        assertFalse(singleDemand.getDemand().getId().equals(demand.getId()));

    }

}