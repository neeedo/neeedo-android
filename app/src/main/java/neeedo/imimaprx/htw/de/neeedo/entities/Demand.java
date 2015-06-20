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
    private ArrayList<String> mustTags;

    @Element
    private ArrayList<String> shouldTags;

    @Element
    private String name = "";

    @Element
    private Location location;

    @Element
    private int distance;

    @Element
    private Price price;

    @Element
    private Demand demand;

    @Element
    String userId = "";

    @Element
    private User user;

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

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getName() {
        return name;
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public void setName(String name) {
        this.name = name;
    }

    public void setDemand(Demand demand) {
        this.demand = demand;
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    private String tagsToString(ArrayList<String> tags) {
        String returnString = "";
        int counter = 0;

        for (String tag : tags) {
            if (counter == 0) {
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
        return "Demand{" +
                "id='" + id + '\'' +
                ", version=" + version +
                ", mustTags=" + mustTags +
                ", shouldTags=" + shouldTags +
                ", name='" + name + '\'' +
                ", location=" + location +
                ", distance=" + distance +
                ", price=" + price +
                ", demand=" + demand +
                ", userId='" + userId + '\'' +
                ", user=" + user +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Demand demand1 = (Demand) o;

        if (version != demand1.version) return false;
        if (distance != demand1.distance) return false;
        if (id != null ? !id.equals(demand1.id) : demand1.id != null) return false;
        if (mustTags != null ? !mustTags.equals(demand1.mustTags) : demand1.mustTags != null)
            return false;
        if (shouldTags != null ? !shouldTags.equals(demand1.shouldTags) : demand1.shouldTags != null)
            return false;
        if (name != null ? !name.equals(demand1.name) : demand1.name != null) return false;
        if (location != null ? !location.equals(demand1.location) : demand1.location != null)
            return false;
        if (price != null ? !price.equals(demand1.price) : demand1.price != null) return false;
        if (demand != null ? !demand.equals(demand1.demand) : demand1.demand != null) return false;
        if (userId != null ? !userId.equals(demand1.userId) : demand1.userId != null) return false;
        return !(user != null ? !user.equals(demand1.user) : demand1.user != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (int) (version ^ (version >>> 32));
        result = 31 * result + (mustTags != null ? mustTags.hashCode() : 0);
        result = 31 * result + (shouldTags != null ? shouldTags.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + distance;
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (demand != null ? demand.hashCode() : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        return result;
    }
}
