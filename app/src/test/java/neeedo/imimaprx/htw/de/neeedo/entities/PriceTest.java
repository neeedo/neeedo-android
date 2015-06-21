package neeedo.imimaprx.htw.de.neeedo.entities;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import neeedo.imimaprx.htw.de.neeedo.entities.util.Price;

public class PriceTest extends Assert {

    private Price price;
    private Price original;

    @Before
    public void setUp(){

        price = new Price(5,55);
        original = new Price(5,55);

    }


    @Test
    public void testGetMin() throws Exception {

        assertTrue(price.getMin() == original.getMin());

    }

    @Test
    public void testSetMin() throws Exception {

        price.setMin(8);

        assertFalse(price.getMin() == original.getMin());

    }

    @Test
    public void testGetMax() throws Exception {

        assertTrue(price.getMax() == original.getMax());

    }

    @Test
    public void testSetMax() throws Exception {

        price.setMax(90);

        assertFalse(price.getMax() == original.getMax());

    }

}