package neeedo.imimaprx.htw.de.neeedo.models;

import neeedo.imimaprx.htw.de.neeedo.entities.User;

public class UserModel {

    private static UserModel userModel;
    private User user;

    private UserModel() {
        userModel = new UserModel();
    }

    public static UserModel getInstance() {

        return userModel != null ? userModel : new UserModel();

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


}
