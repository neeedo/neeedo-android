package neeedo.imimaprx.htw.de.neeedo.entities.favorites;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.ArrayList;

import neeedo.imimaprx.htw.de.neeedo.entities.user.User;
import neeedo.imimaprx.htw.de.neeedo.entities.util.BaseEntity;
import neeedo.imimaprx.htw.de.neeedo.entities.util.Location;


@Root(name = "favorit")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Favorit implements Serializable, BaseEntity {

    @Element
    private String id;

    @Element
    private long version = 0;

    @ElementList(name = "tags")
    private ArrayList<String> tags;

    @Element
    private Location location;

    @Element
    private int distance;

    @Element
    private double price;

    @Element
    private User user;

    public Favorit() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Favorit{" +
                "id='" + id + '\'' +
                ", version=" + version +
                ", tags=" + tags +
                ", location=" + location +
                ", distance=" + distance +
                ", price=" + price +
                ", user=" + user +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Favorit favorit = (Favorit) o;

        if (version != favorit.version) return false;
        if (distance != favorit.distance) return false;
        if (Double.compare(favorit.price, price) != 0) return false;
        if (id != null ? !id.equals(favorit.id) : favorit.id != null) return false;
        if (tags != null ? !tags.equals(favorit.tags) : favorit.tags != null) return false;
        if (location != null ? !location.equals(favorit.location) : favorit.location != null)
            return false;
        return !(user != null ? !user.equals(favorit.user) : favorit.user != null);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id != null ? id.hashCode() : 0;
        result = 31 * result + (int) (version ^ (version >>> 32));
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + distance;
        temp = Double.doubleToLongBits(price);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (user != null ? user.hashCode() : 0);
        return result;
    }
}
