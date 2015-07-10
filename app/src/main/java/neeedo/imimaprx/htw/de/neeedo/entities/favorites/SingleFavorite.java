package neeedo.imimaprx.htw.de.neeedo.entities.favorites;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;

import neeedo.imimaprx.htw.de.neeedo.entities.demand.Demand;
import neeedo.imimaprx.htw.de.neeedo.entities.util.BaseEntity;

@Root(name = "favorite")
public class SingleFavorite implements Serializable, BaseEntity {

    @Element
    private Favorite favorite;

    public Favorite getFavorite() {
        return favorite;
    }

    public void setFavorite(Favorite favorite) {
        this.favorite = favorite;
    }

    @Override
    public String toString() {
        return "SingleFavorite{" +
                "favorite=" + favorite +
                '}';
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SingleFavorite that = (SingleFavorite) o;

        return !(favorite != null ? !favorite.equals(that.favorite) : that.favorite != null);

    }

    @Override
    public int hashCode() {
        return favorite != null ? favorite.hashCode() : 0;
    }
}
