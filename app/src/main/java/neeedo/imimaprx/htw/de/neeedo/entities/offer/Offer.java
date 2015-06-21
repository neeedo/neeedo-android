package neeedo.imimaprx.htw.de.neeedo.entities.offer;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.ArrayList;

import neeedo.imimaprx.htw.de.neeedo.entities.util.BaseEntity;
import neeedo.imimaprx.htw.de.neeedo.entities.util.Location;
import neeedo.imimaprx.htw.de.neeedo.entities.user.User;


@Root(name = "offer")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Offer implements Serializable, BaseEntity {

    @Element
    private String id;

    @Element
    private long version = 0;

    @Element
    private String name = "";

    @Element
    private ArrayList<String> tags = new ArrayList<>();

    @Element
    private Location location;

    @Element
    private Double price = 0d; // TODO price entity without min and max?

    @Element
    private Offer offer;

    @Element
    private User user;

    @Element
    private ArrayList<String> images = new ArrayList<>();

    @Element
    private String userId = "";

    public Offer() {

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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
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

    public Offer getOffer() {
        return this.offer;
    }

    public void setOffer(Offer offer) {
        this.offer = offer;
    }

    public void setImages(ArrayList<String> images) {
        this.images = images;
    }

    public ArrayList<String> getImages() {
        return images;
    }

    public void addSingleImageURL(String url) {
        images.add(url);
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public String getName() {
        return name;
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
