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


@Root(name = "favorite")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Favorite implements Serializable, BaseEntity {

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
    private User user = null;

    @Element
    private String userId = "";

    @Element
    private String offerId = "";

    @Element
    private ArrayList<String> images;

    public Favorite() {

    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public long getVersion() {
        return version;
    }

    public void setVersion(long version) {
        this.version = version;
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public ArrayList<String> getImages() {
        return images;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public String getTagsString() {
        return tagsToString(tags);
    }

    @Override
    public String toString() {

        String temp = "";

        for (String s : tags) {
            temp += s + ",";
        }
        temp = temp.substring(0, temp.length() - 1);

        temp += "\nfrom " + user.getName();
        return temp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Favorite favorite = (Favorite) o;

        if (version != favorite.version) return false;
        if (distance != favorite.distance) return false;
        if (Double.compare(favorite.price, price) != 0) return false;
        if (id != null ? !id.equals(favorite.id) : favorite.id != null) return false;
        if (tags != null ? !tags.equals(favorite.tags) : favorite.tags != null) return false;
        if (location != null ? !location.equals(favorite.location) : favorite.location != null)
            return false;
        if (user != null ? !user.equals(favorite.user) : favorite.user != null) return false;
        if (userId != null ? !userId.equals(favorite.userId) : favorite.userId != null)
            return false;
        if (offerId != null ? !offerId.equals(favorite.offerId) : favorite.offerId != null)
            return false;
        return !(images != null ? !images.equals(favorite.images) : favorite.images != null);

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
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (offerId != null ? offerId.hashCode() : 0);
        result = 31 * result + (images != null ? images.hashCode() : 0);
        return result;
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
}
