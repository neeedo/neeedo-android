package neeedo.imimaprx.htw.de.neeedo.entities;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@Root(name = "location")
public class Location implements Serializable {

    @Element
    private long lat;

    @Element
    private long lon;

    public Location() {
    }

    public Location(long lat, long lon) {
        this.lat = lat;
        this.lon = lon;
    }

    public long getLat() {
        return lat;
    }

    public void setLat(long lat) {
        this.lat = lat;
    }

    public long getLon() {
        return lon;
    }

    public void setLon(long lon) {
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "Location{" +
                "lat=" + lat +
                ", lon=" + lon +
                '}';
    }
}
