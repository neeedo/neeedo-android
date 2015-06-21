package neeedo.imimaprx.htw.de.neeedo.entities;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import neeedo.imimaprx.htw.de.neeedo.entities.util.Location;

public class LocationTest extends Assert {

    private Location location;
    private Location original;

    @Before
    public void setUp() {

        location = new Location(60, 60);
        original = new Location(60, 60);

    }


    @Test
    public void testGetLat() throws Exception {

        assertTrue(location.getLat() == original.getLat());

    }

    @Test
    public void testSetLat() throws Exception {

        location.setLat(77);

        assertFalse(location.getLat() == original.getLat());

    }

    @Test
    public void testGetLon() throws Exception {

        assertTrue(location.getLon() == location.getLon());
    }

    @Test
    public void testSetLon() throws Exception {

        location.setLon(77);

        assertFalse(location.getLon() == original.getLon());

    }

}