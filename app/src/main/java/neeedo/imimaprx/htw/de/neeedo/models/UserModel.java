package neeedo.imimaprx.htw.de.neeedo.models;

import neeedo.imimaprx.htw.de.neeedo.entities.user.User;

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

    public void putCurrentLoginInformationInActiveUser() {
        ActiveUser activeUser = ActiveUser.getInstance();
        activeUser.setUserPassword(user.getPassword());
        activeUser.setUsername(user.getEmail());
    }


}
