package neeedo.imimaprx.htw.de.neeedo.entities.demand;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.ArrayList;

import neeedo.imimaprx.htw.de.neeedo.entities.offer.Offer;
import neeedo.imimaprx.htw.de.neeedo.entities.user.User;
import neeedo.imimaprx.htw.de.neeedo.entities.util.BaseEntity;
import neeedo.imimaprx.htw.de.neeedo.entities.util.Location;
import neeedo.imimaprx.htw.de.neeedo.entities.util.Price;
import neeedo.imimaprx.htw.de.neeedo.utils.ProductUtils;


@Root(name = "demand")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Demand implements Serializable, BaseEntity {

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
    private String userId = "";

    @Element
    private User user;

    @Element
    private ArrayList<Offer> matchingOfferList = new ArrayList<>();

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

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public String getMustTagsString() {
        return ProductUtils.tagsToString(mustTags);
    }

    public void setMustTags(ArrayList<String> mustTags) {
        this.mustTags = mustTags;
    }

    public ArrayList<String> getShouldTags() {
        return shouldTags;
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public String getShouldTagsString() {
        return ProductUtils.tagsToString(shouldTags);
    }

    public void setShouldTags(ArrayList<String> shouldTags) {
        this.shouldTags = shouldTags;
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public String getUserId() {
        return userId;
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public ArrayList<Offer> getMatchingOfferList() {
        return matchingOfferList;
    }

    public void setMatchingOfferList(ArrayList<Offer> matchingOfferList) {
        this.matchingOfferList = matchingOfferList;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "Demand{" +
                "id='" + id + '\'' +
                ", version=" + version +
                ", mustTags=" + mustTags +
                ", shouldTags=" + shouldTags +
                ", location=" + location +
                ", distance=" + distance +
                ", price=" + price +
                ", userId='" + userId + '\'' +
                ", user=" + user +
                ", matchingOfferList=" + matchingOfferList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Demand demand = (Demand) o;

        if (version != demand.version) return false;
        if (distance != demand.distance) return false;
        if (id != null ? !id.equals(demand.id) : demand.id != null) return false;
        if (mustTags != null ? !mustTags.equals(demand.mustTags) : demand.mustTags != null)
            return false;
        if (shouldTags != null ? !shouldTags.equals(demand.shouldTags) : demand.shouldTags != null)
            return false;
        if (location != null ? !location.equals(demand.location) : demand.location != null)
            return false;
        if (price != null ? !price.equals(demand.price) : demand.price != null) return false;
        if (userId != null ? !userId.equals(demand.userId) : demand.userId != null) return false;
        if (user != null ? !user.equals(demand.user) : demand.user != null) return false;
        return !(matchingOfferList != null ? !matchingOfferList.equals(demand.matchingOfferList) : demand.matchingOfferList != null);

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
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (matchingOfferList != null ? matchingOfferList.hashCode() : 0);
        return result;
    }
}
