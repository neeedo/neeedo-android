package neeedo.imimaprx.htw.de.neeedo.models;

import android.content.Context;
import android.content.SharedPreferences;

import org.springframework.util.Base64Utils;

/**
 * Before setting the username and password it is needed to set the current Context of the application.
 */
public class ActiveUser {

    public static final String PREFS_FILE = "PrefsFile";
    private Context context;

    //Username is the Email here
    private boolean isStartup = false;
    private String username = "";
    private String userPassword = "";
    private String userId = "";

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
        saveValuesInPreferences();
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
        userId = "";
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
        editor.putString("userId", userId);
        editor.commit();
    }

    public boolean isAlreadyStarted() {
        return isStartup;
    }

    public void setAlreadyStarted(boolean isStartup) {
        this.isStartup = isStartup;
    }

    public void loadValuesFromPreferences() {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_FILE, context.MODE_PRIVATE);
        username = prefs.getString("name", null);
        userPassword = prefs.getString("password", null);
        userId = prefs.getString("userId", null);
        if (username == null | userPassword == null) {
            username = "";
            userPassword = "";
        }

    }

    public Context getContext() {
        return context;
    }

    public boolean userCredentialsAvailable() {
        if (userPassword.equals("") | username.equals("")) {
            return false;
        }
        return true;
    }

    public boolean hasActiveUser() {
        return (username != null) && (username != "");
    }

    public String getAuthentificationHash() {
        byte[] bytes = String.format("%s:%s", username, userPassword).getBytes();
        return String.format("Basic %s", Base64Utils.encodeToString(bytes));
    }

}

