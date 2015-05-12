package neeedo.imimaprx.htw.de.neeedo.models;

import android.util.Base64;

public class ActiveUser {

    private String username = "";
    private String userPassword = "";
    private static ActiveUser activeUser;


    private ActiveUser() {
        activeUser = new ActiveUser();
    }


    public static ActiveUser getInstance() {

        return activeUser == null ? new ActiveUser() : activeUser;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ActiveUser that = (ActiveUser) o;

        if (username != null ? !username.equals(that.username) : that.username != null)
            return false;
        return !(userPassword != null ? !userPassword.equals(that.userPassword) : that.userPassword != null);

    }

    @Override
    public int hashCode() {
        int result = username != null ? username.hashCode() : 0;
        result = 31 * result + (userPassword != null ? userPassword.hashCode() : 0);
        return result;
    }
}
