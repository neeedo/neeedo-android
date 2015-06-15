package neeedo.imimaprx.htw.de.neeedo.entities;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.ArrayList;


@Root(name = "demand")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Demand implements Serializable, BaseEntity {

    @Element
    private String id;

    @Element
    private long version = 0;

    @Element
    private String userId;

    @Element
    private ArrayList<String> mustTags;

    @Element
    private ArrayList<String> shouldTags;

    @Element
    private Location location;

    @Element
    private int distance;

    @Element
    private Price price;

    @Element
    private Demand demand;

    public Demand() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    public ArrayList<String> getMustTags() {
        return mustTags;
    }

    public String getMustTagsString() {
        return tagsToString(mustTags);
    }

    public void setMustTags(ArrayList<String> mustTags) {
        this.mustTags = mustTags;
    }

    public ArrayList<String> getShouldTags() {
        return shouldTags;
    }

    public String getShouldTagsString() {
        return tagsToString(shouldTags);
    }

    public void setShouldTags(ArrayList<String> shouldTags) {
        this.shouldTags = shouldTags;
    }

    public Demand getDemand() {
        return this.demand;
    }

    public void setDemand(Demand demand) {
        this.demand = demand;
    }

    private String tagsToString(ArrayList<String> tags) {
        String returnString = "";
        int counter = 0;

        for(String tag : tags) {
            if(counter == 0) {
                returnString = tag;
            } else {
                returnString = returnString + ", " + tag;
            }
            counter++;
        }

        return returnString;
    }

    @Override
    public String toString() {
//        return "Demand{" +
//                "id='" + id + '\'' +
//                ", version=" + version +
//                ", userId='" + userId + '\'' +
//                ", mustTags=" + mustTags +
//                ", shouldTags=" + shouldTags +
//                ", location=" + location +
//                ", distance=" + distance +
//                ", price=" + price +
//                '}';

        return "Must have:\n" + mustTags +
                "\nShould have:\n" + shouldTags +
                "\n\nPrice: " + price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Demand demand = (Demand) o;

        if (distance != demand.distance) return false;
        if (version != demand.version) return false;
        if (id != null ? !id.equals(demand.id) : demand.id != null) return false;
        if (location != null ? !location.equals(demand.location) : demand.location != null)
            return false;
        if (mustTags != null ? !mustTags.equals(demand.mustTags) : demand.mustTags != null)
            return false;
        if (price != null ? !price.equals(demand.price) : demand.price != null) return false;
        if (shouldTags != null ? !shouldTags.equals(demand.shouldTags) : demand.shouldTags != null)
            return false;
        if (userId != null ? !userId.equals(demand.userId) : demand.userId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (int) (version ^ (version >>> 32));
        result = 41 * result + (userId != null ? userId.hashCode() : 0);
        result = 47 * result + (mustTags != null ? mustTags.hashCode() : 0);
        result = 73 * result + (shouldTags != null ? shouldTags.hashCode() : 0);
        result = 113 * result + (location != null ? location.hashCode() : 0);
        result = 137 * result + distance;
        result = 149 * result + (price != null ? price.hashCode() : 0);
        return result;
    }
}
