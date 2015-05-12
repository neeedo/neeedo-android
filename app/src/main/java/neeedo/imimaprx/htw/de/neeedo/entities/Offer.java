package neeedo.imimaprx.htw.de.neeedo.entities;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.ArrayList;

import neeedo.imimaprx.htw.de.neeedo.entities.Location;


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
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Offer offer = (Offer) o;

        if (version != offer.version) return false;
        if (id != null ? !id.equals(offer.id) : offer.id != null) return false;
        if (location != null ? !location.equals(offer.location) : offer.location != null)
            return false;
        if (tags != null ? !tags.equals(offer.tags) : offer.tags != null)
            return false;
        if (price != null ? !price.equals(offer.price) : offer.price != null) return false;
        if (userId != null ? !userId.equals(offer.userId) : offer.userId != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (int) (version ^ (version >>> 32));
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (tags != null ? tags.hashCode() : 0);
        result = 31 * result + (location != null ? location.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        return result;
    }
}
