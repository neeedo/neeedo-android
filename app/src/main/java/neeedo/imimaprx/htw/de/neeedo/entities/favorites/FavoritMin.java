package neeedo.imimaprx.htw.de.neeedo.entities.favorites;

import com.fasterxml.jackson.annotation.JsonInclude;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

import neeedo.imimaprx.htw.de.neeedo.entities.util.BaseEntity;

@Root(name = "user")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FavoritMin implements Serializable, BaseEntity {

    @Element
    private String userId;

    @Element
    private String offerId;

    public FavoritMin(String userId, String offerId) {
        this.userId = userId;
        this.offerId = offerId;
    }

    public FavoritMin() {

    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getOfferId() {
        return offerId;
    }

    public void setOfferId(String offerId) {
        this.offerId = offerId;
    }

    @Override
    public String toString() {
        return "FavoritMin{" +
                "userId='" + userId + '\'' +
                ", offerId='" + offerId + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FavoritMin that = (FavoritMin) o;

        if (userId != null ? !userId.equals(that.userId) : that.userId != null) return false;
        return !(offerId != null ? !offerId.equals(that.offerId) : that.offerId != null);

    }

    @Override
    public int hashCode() {
        int result = userId != null ? userId.hashCode() : 0;
        result = 31 * result + (offerId != null ? offerId.hashCode() : 0);
        return result;
    }
}
