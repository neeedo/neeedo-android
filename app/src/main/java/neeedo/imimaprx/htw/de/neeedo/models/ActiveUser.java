package neeedo.imimaprx.htw.de.neeedo.models;

import android.content.Context;

import org.springframework.util.Base64Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

/**
 * Use the checkForExistingCredentials to check there is a file with user credentials. If it returns true there is and was already loaded.
 * Before setting the username and password it is needed to set the current Context of the application.
 * When user and password is set it will be base64 encoded and saved as a file in the internal storage.
 */


public class ActiveUser {

    private static Context context;

    private String username = "";
    private String userPassword = "";
    private String userID;


    private String userEncoded;
    private String userPasswordEncoded;
    private static ActiveUser activeUser;

    private ActiveUser() {
        activeUser = new ActiveUser();

    }

    public static ActiveUser getInstance() {

        return activeUser == null ? new ActiveUser() : activeUser;
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
        userEncoded = Base64Utils.encodeToString(username.getBytes());
        saveCredentials();
    }

    public String getUserPassword() {
        return userPassword;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public void setUserPassword(String userPassword) {
        if (context == null) {
            throw new IllegalStateException("No Context is set!");
        }

        this.userPassword = userPassword;
        userPasswordEncoded = Base64Utils.encodeToString(userPassword.getBytes());
        saveCredentials();
    }

    private void decodeCredentials() {

        username = Base64Utils.decode(username.getBytes()).toString();
        userPassword = Base64Utils.decode(userPassword.getBytes()).toString();

    }

    private void saveCredentials() {

        if (context == null) {
            throw new IllegalStateException("No Context is set!");
        }

        if (userEncoded == null | userPasswordEncoded == null) {
            return;
        }

        File file = new File(context.getApplicationContext().getFilesDir(), "image");
        if (file.exists()) {
            file.delete();
        }

        FileOutputStream outputStream;

        String toSave = userEncoded + "|" + userPasswordEncoded;

        try {
            outputStream = context.openFileOutput("image", Context.MODE_PRIVATE);
            outputStream.write(toSave.getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean checkForExistingCredentials() {

        if (context == null) {
            throw new IllegalStateException("No Context is set!");
        }

        File file = new File(context.getApplicationContext().getFilesDir(), "image");
        if (file.exists()) {
            InputStream in;
            try {

                in = new FileInputStream(file);

                StringBuffer fileContent = new StringBuffer("");

                byte[] buffer = new byte[1024];

                int n;

                while ((n = in.read(buffer)) != -1) {
                    fileContent.append(new String(buffer, 0, n));
                }

                String result = fileContent.toString();

                String[] temp = result.split("[|]+");

                userEncoded = temp[0];
                userPasswordEncoded = temp[1];

                decodeCredentials();

            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }

            return true;
        }
        return false;
    }

    public void clearAllCredentials(){
        userEncoded = null;
        userPasswordEncoded = null;
        username = "";
        userPassword = "";
        userID = "";

    }

}
