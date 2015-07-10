package neeedo.imimaprx.htw.de.neeedo.models;


import java.util.ArrayList;

import neeedo.imimaprx.htw.de.neeedo.entities.favorites.Favorite;
import neeedo.imimaprx.htw.de.neeedo.entities.favorites.Favorites;
import neeedo.imimaprx.htw.de.neeedo.entities.offer.Offers;

public class FavoritesModel {

    private Favorites favorites;
    private Offers offersToFavorites;


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
            favorites.setFavorites(new ArrayList<Favorite>());
        }
        return favorites;
    }

    public void setFavorites(Favorites favorites) {
        this.favorites = favorites;
    }
}
