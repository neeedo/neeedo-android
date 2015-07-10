package neeedo.imimaprx.htw.de.neeedo.entities.favorites;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.ArrayList;

import neeedo.imimaprx.htw.de.neeedo.entities.util.BaseEntity;

@Root(name = "favorites")
public class Favorites implements Serializable, BaseEntity {

    @Element
    private ArrayList<Favorite> favorites = new ArrayList<>();

    public Favorites() {
    }

    public ArrayList<Favorite> getFavorites() {
        return favorites;
    }

    public void setFavorites(ArrayList<Favorite> favorites) {
        this.favorites = favorites;
    }

    public boolean isEmpty() {
        if (favorites == null || favorites.size() == 0) {
            return true;
        }
        return false;
    }

    public void addSingleFavorit(Favorite favorite) {
        if (favorites == null) {
            favorites = new ArrayList<>();
        }
        favorites.add(favorite);
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
