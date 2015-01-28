package neeedo.imimaprx.htw.de.neeedo.Entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

@JsonIgnoreProperties(ignoreUnknown = true)
@Root(name = "demand")
public class Demand implements Serializable {

    @Element
    private long id;

    @Element
    private long userId;

    @Element
    private String tags;

    @Element
    private Location location;

    @Element
    private int distance;

    @Element
    private Price price;

    public Demand() {

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Demand{" +
                "id=" + id +
                ", userId=" + userId +
                ", tags='" + tags + '\'' +
                ", location=" + location +
                ", distance=" + distance +
                ", price=" + price +
                '}';
    }
}
