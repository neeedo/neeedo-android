package neeedo.imimaprx.htw.de.neeedo.entities;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import neeedo.imimaprx.htw.de.neeedo.entities.demand.Demand;
import neeedo.imimaprx.htw.de.neeedo.entities.demand.Demands;
import neeedo.imimaprx.htw.de.neeedo.entities.util.Price;

public class DemandsTest extends Assert{

    private Demands demands;

    private boolean setUpIsDone = false;

    //TODO change to static @BeforeClass
    @Before
    public void setUp() {
        if(setUpIsDone) return;
        demands = new Demands();

        ArrayList<Demand> demandsList = new ArrayList<>();

        Demand demand = new Demand();
        demand.setDistance(6);
        demand.setId("Bla");
        demand.setPrice(new Price(5, 5));

        demandsList.add(demand);

        demands.setDemands(demandsList);

        setUpIsDone = true;
    }

    @Test
    public void testSetDemands() throws Exception {
        ArrayList<Demand> list = new ArrayList<>();

        Demand demand = new Demand();
        demand.setDistance(6);
        demand.setId("foobar");
        demand.setPrice(new Price(5, 5));

        list.add(demand);

        demands.setDemands(list);

        assertTrue(demands.getDemands().get(0).getId().equals("foobar"));
    }

    @Test
    public void testGetDemands() throws Exception {
        List<Demand> list = demands.getDemands();

        assertTrue(list.get(0).getId().equals("Bla"));
    }

    @Test
    public void testToString() throws Exception {
        assertFalse(demands.getDemands().isEmpty());
    }
}