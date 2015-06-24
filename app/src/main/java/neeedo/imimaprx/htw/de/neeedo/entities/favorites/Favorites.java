package neeedo.imimaprx.htw.de.neeedo.entities.favorites;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import neeedo.imimaprx.htw.de.neeedo.entities.util.BaseEntity;

@Root(name = "favorites")
public class Favorites implements Serializable, BaseEntity {

    @Element
    private ArrayList<Favorit> favorites;

    public Favorites() {
    }

    public List<Favorit> getFavorites() {
        return favorites;
    }

    public void setFavorites(ArrayList<Favorit> favorites) {
        this.favorites = favorites;
    }

    public boolean isEmpty() {
        if (favorites == null || favorites.size() == 0) {
            return true;
        }
        return false;
    }

    public void addSingleFavorit(Favorit favorit) {
        if (favorites == null) {
            favorites = new ArrayList<>();
        }
        favorites.add(favorit);
    }

    @Override
    public String toString() {
        return "Favorites{" +
                "favorites=" + favorites +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Favorites favorites = (Favorites) o;

        return !(this.favorites != null ? !this.favorites.equals(favorites.favorites) : favorites.favorites != null);

    }

    @Override
    public int hashCode() {
        return favorites != null ? favorites.hashCode() : 0;
    }
}
