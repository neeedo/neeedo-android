package neeedo.imimaprx.htw.de.neeedo.entities;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.ArrayList;


@Root(name = "offer")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Offer implements Serializable {

    @Element
    private String id;

    @Element
    private long version = 0;

    @Element
    private String userId;

    @Element
    private ArrayList<String> tags = new ArrayList<>();

    @Element
    private Location location;

    @Element
    private Double price = 0d; // TODO price entity without min and max?

    @Element
    private Offer offer;

    @Element
    private ArrayList<String> images = new ArrayList<>();

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


    @Override
    public String toString() {
        return "Offer{" +
                "id='" + id + '\'' +
                ", version=" + version +
                ", userId='" + userId + '\'' +
                ", tags=" + tags +
                ", location=" + location +
                ", price=" + price +
                ", offer=" + offer +
                ", images=" + images +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Offer offer1 = (Offer) o;

        if (version != offer1.version) return false;
        if (id != null ? !id.equals(offer1.id) : offer1.id != null) return false;
        if (userId != null ? !userId.equals(offer1.userId) : offer1.userId != null) return false;
        if (tags != null ? !tags.equals(offer1.tags) : offer1.tags != null) return false;
        if (location != null ? !location.equals(offer1.location) : offer1.location != null)
            return false;
        if (price != null ? !price.equals(offer1.price) : offer1.price != null) return false;
        if (offer != null ? !offer.equals(offer1.offer) : offer1.offer != null) return false;
        return !(images != null ? !images.equals(offer1.images) : offer1.images != null);

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (int) (version ^ (version >>> 32));
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (offer != null ? offer.hashCode() : 0);
        result = 31 * result + (images != null ? images.hashCode() : 0);
        return result;
    }
}
