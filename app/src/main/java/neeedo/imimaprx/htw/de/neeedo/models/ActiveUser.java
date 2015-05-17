package neeedo.imimaprx.htw.de.neeedo.models;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Use the checkForExistingCredentials to check there is a file with user credentials. If it returns true there is and was already loaded.
 * Before setting the username and password it is needed to set the current Context of the application.
 * When user and password is set it will be base64 encoded and saved as a file in the internal storage.
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


    public Context getContext() {
        return context;
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

    public void clearUserinformation() {
        username = "";
        userPassword = "";

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
        if (username != null & userPassword != null) {
            username = "";
            userPassword = "";
        }
    }

    public boolean userinformationLoaded() {
        if (userPassword.equals("") | username.equals("")) {
            return false;
        }
        return true;
    }

}

