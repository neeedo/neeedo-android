package neeedo.imimaprx.htw.de.neeedo.models;

import neeedo.imimaprx.htw.de.neeedo.entities.demand.Demand;
import neeedo.imimaprx.htw.de.neeedo.entities.favorites.Favorite;
import neeedo.imimaprx.htw.de.neeedo.entities.offer.Offer;
import neeedo.imimaprx.htw.de.neeedo.entities.user.User;
import neeedo.imimaprx.htw.de.neeedo.entities.util.BaseEntity;
import neeedo.imimaprx.htw.de.neeedo.rest.util.BaseAsyncTask.EntityType;

public class UserModel {

    private static UserModel userModel;
    private User user;

    private UserModel() {

    }

    public static UserModel getInstance() {

        if (userModel == null) {
            userModel = new UserModel();
        }
        return userModel;

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean userAvailable() {
        return user == null ? false : true;
    }

    public void putCurrentLoginInformationInActiveUser(User user) {
        ActiveUser activeUser = ActiveUser.getInstance();
        activeUser.setUserPassword(user.getPassword());
        activeUser.setUsername(user.getEmail());
        activeUser.setUserId(user.getId());
    }

    public boolean checkIfEntityOwnerIsActiveUser(BaseEntity entity) {
        String userId = ActiveUser.getInstance().getUserId();
        EntityType entityType = getType(entity);
        if (entityType != null) {

            switch (entityType) {
                case DEMAND: {
                    Demand demand = (Demand) entity;
                    if (demand.getUser().getId().equals(userId)) {
                        return true;
                    }
                }
                break;

                case OFFER: {
                    Offer offer = (Offer) entity;
                    if (offer.getUser().getId().equals(userId)) {
                        return true;
                    }
                }
                break;

                case FAVORITE: {
                    Favorite favorite = (Favorite) entity;
                    if (favorite.getUser().getId().equals(userId)) {
                        return true;
                    }
                }
                break;

                case USER: {
                    User user = (User) entity;
                    if (user.getId().equals(userId)) {
                        return true;
                    }
                }
                break;
            }
        }
        return false;
    }

    private EntityType getType(BaseEntity baseEntity) {
        if (baseEntity instanceof Demand) {
            return EntityType.DEMAND;
        } else if (baseEntity instanceof Offer) {
            return EntityType.OFFER;
        } else if (baseEntity instanceof Favorite) {
            return EntityType.FAVORITE;
        } else if (baseEntity instanceof User) {
            return EntityType.USER;
        }
        return null;
    }


}
