package neeedo.imimaprx.htw.de.neeedo.entities;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.Assert;

import java.util.ArrayList;

public class DemandsTest {

    private Demands demands;
    private boolean setUpIsDone = false;

    @Before
    public void setUp() {
        if(setUpIsDone) return;
        demands = new Demands();

        ArrayList<Demand> list = new ArrayList<>();

        Demand demand = new Demand();
        demand.setDistance(6);
        demand.setId("Bla");
        demand.setPrice(new Price(5, 5));

        list.add(demand);

        demands.setDemands(list);

        setUpIsDone = true;



    }


    @Test
    public void testSetDemands() throws Exception {

        ArrayList<Demand> list = new ArrayList<>();

        Demand demand = new Demand();
        demand.setDistance(6);
        demand.setId("Something else");
        demand.setPrice(new Price(5, 5));

        list.add(demand);

        demands.setDemands(list);

    }

    @Test
    public void testGetDemands() throws Exception {
        ArrayList<Demand> list = demands.getDemands();

        Assert.assertTrue(list.get(0).getId().equals("Something else"));
    }


    @Test
    public void testToString() throws Exception {

        Assert.assertFalse(demands.getDemands().isEmpty());

    }
}