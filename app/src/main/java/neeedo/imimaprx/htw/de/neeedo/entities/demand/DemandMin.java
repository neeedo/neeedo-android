package neeedo.imimaprx.htw.de.neeedo.entities.demand;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.ArrayList;

import neeedo.imimaprx.htw.de.neeedo.entities.user.UserMin;
import neeedo.imimaprx.htw.de.neeedo.entities.util.BaseEntity;
import neeedo.imimaprx.htw.de.neeedo.entities.util.Location;
import neeedo.imimaprx.htw.de.neeedo.entities.util.Price;


@Root(name = "demand")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DemandMin implements Serializable, BaseEntity {

    @Element
    private String id;

    @Element
    private long version = 0;

    @ElementList(name = "mustTags")
    private ArrayList<String> mustTags;

    @ElementList(name = "shouldTags")
    private ArrayList<String> shouldTags;

    @Element
    private Location location;

    @Element
    private int distance;

    @Element
    private Price price;

    @Element
    private UserMin user;

    public DemandMin() {

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

    public ArrayList<String> getMustTags() {
        return mustTags;
    }

    public void setMustTags(ArrayList<String> mustTags) {
        this.mustTags = mustTags;
    }

    public ArrayList<String> getShouldTags() {
        return shouldTags;
    }

    public void setShouldTags(ArrayList<String> shouldTags) {
        this.shouldTags = shouldTags;
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

    public UserMin getUser() {
        return user;
    }

    public void setUser(UserMin user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "DemandMin{" +
                "id='" + id + '\'' +
                ", version=" + version +
                ", mustTags=" + mustTags +
                ", shouldTags=" + shouldTags +
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

        DemandMin demandMin = (DemandMin) o;

        if (version != demandMin.version) return false;
        if (distance != demandMin.distance) return false;
        if (id != null ? !id.equals(demandMin.id) : demandMin.id != null) return false;
        if (mustTags != null ? !mustTags.equals(demandMin.mustTags) : demandMin.mustTags != null)
            return false;
        if (shouldTags != null ? !shouldTags.equals(demandMin.shouldTags) : demandMin.shouldTags != null)
            return false;
        if (location != null ? !location.equals(demandMin.location) : demandMin.location != null)
            return false;
        if (price != null ? !price.equals(demandMin.price) : demandMin.price != null) return false;
        return !(user != null ? !user.equals(demandMin.user) : demandMin.user != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (int) (version ^ (version >>> 32));
        result = 31 * result + (mustTags != null ? mustTags.hashCode() : 0);
        result = 31 * result + (shouldTags != null ? shouldTags.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + distance;
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        return result;
    }
}
