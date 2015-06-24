package neeedo.imimaprx.htw.de.neeedo.models;


import java.util.ArrayList;

import neeedo.imimaprx.htw.de.neeedo.entities.favorites.Favorit;
import neeedo.imimaprx.htw.de.neeedo.entities.favorites.Favorites;

public class FavoritesModel {

    private Favorites favorites;


    public static FavoritesModel getInstance() {
        if (favoritesModel == null)
            favoritesModel = new FavoritesModel();
        return favoritesModel;
    }

    private FavoritesModel() {
    }

    private static FavoritesModel favoritesModel;

    public Favorites getFavorites() {
        if (favorites == null) {
            favorites = new Favorites();
            favorites.setFavorites(new ArrayList<Favorit>());
        }
        return favorites;
    }

    public void setFavorites(Favorites favorites) {
        this.favorites = favorites;
    }
}
