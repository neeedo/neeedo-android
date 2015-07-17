package neeedo.imimaprx.htw.de.neeedo.models;


import java.util.ArrayList;

import neeedo.imimaprx.htw.de.neeedo.entities.favorites.Favorite;
import neeedo.imimaprx.htw.de.neeedo.entities.favorites.Favorites;
import neeedo.imimaprx.htw.de.neeedo.entities.favorites.SingleFavorite;

public class FavoritesModel {

    private Favorites favorites;
    private SingleFavorite singleFavorite;

    public static FavoritesModel getInstance() {
        if (favoritesModel == null)
            favoritesModel = new FavoritesModel();
        return favoritesModel;
    }

    private FavoritesModel() {
    }

    private static FavoritesModel favoritesModel;

    public ArrayList<Favorite> getFavorites() {
        if (favorites == null) {
            favorites = new Favorites();
            favorites.setFavorites(new ArrayList<Favorite>());
        }
        return favorites.getFavorites();
    }

    public void setFavorites(Favorites favorites) {
        this.favorites = favorites;
    }

    public Favorite getSingleFavorite() {
        if (singleFavorite == null) {
            return null;
        }
        return singleFavorite.getFavorite();
    }

    public void setSingleFavorite(SingleFavorite singleFavorite) {
        this.singleFavorite = singleFavorite;
    }
}
