package neeedo.imimaprx.htw.de.neeedo.models;

import android.util.Base64;

public class ActiveUser {

    private String username;
    private String userPassword;
    private String userToken;
    private static ActiveUser activeUser;


    private ActiveUser() {
        activeUser = new ActiveUser();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }


    public static ActiveUser getInstance() {

        return activeUser == null ? new ActiveUser() : activeUser;
    }

    public String getUserToken() {
        return userToken;
    }

    public void generateUserToken() throws IllegalArgumentException {

        if (username == null) {
            throw new IllegalArgumentException("No Username is set.");
        }

        if (userPassword == null) {
            throw new IllegalArgumentException("No UserPassword is set.");
        }

        String temp = username + userPassword;

        byte[] data = temp.getBytes();
        userToken = Base64.encodeToString(data, Base64.DEFAULT);
        userToken += ":";

    }

}
