package neeedo.imimaprx.htw.de.neeedo.models;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Before setting the username and password it is needed to set the current Context of the application.
 */
public class ActiveUser {

    public static final String PREFS_FILE = "PrefsFile";
    private static Context context;

    //Username is the Email here
    private String username = "";
    private String userPassword = "";

    private static ActiveUser activeUser;

    private ActiveUser() {
    }

    public static ActiveUser getInstance() {
        if (activeUser == null) {
            activeUser = new ActiveUser();
        }
        return activeUser;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        if (context == null) {
            throw new IllegalStateException("No Context is set!");
        }

        this.username = username;
        saveValuesInPreferences();
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        if (context == null) {
            throw new IllegalStateException("No Context is set!");
        }
        this.userPassword = userPassword;
        saveValuesInPreferences();
    }

    public void clearUserInformation() {
        username = "";
        userPassword = "";
        saveValuesInPreferences();
        UserModel.getInstance().setUser(null);
    }

    private void saveValuesInPreferences() {
        if (context == null) {
            throw new IllegalStateException("No Context is set!");
        }

        if (username == null | userPassword == null) {
            return;
        }

        SharedPreferences.Editor editor = context.getSharedPreferences(PREFS_FILE, context.MODE_PRIVATE).edit();
        editor.putString("name", username);
        editor.putString("password", userPassword);
        editor.commit();
    }

    public void loadValuesFromPreferences() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_FILE, context.MODE_PRIVATE);
        username = prefs.getString("name", null);
        userPassword = prefs.getString("password", null);
        if (username == null & userPassword == null) {
            username = "";
            userPassword = "";
        }
    }

    public boolean userCredentialsAvailable() {
        if (userPassword.equals("") | username.equals("")) {
            return false;
        }
        return true;
    }

    public boolean hasActiveUser() {
        return ( username != null ) && (username != "");
    }
}

