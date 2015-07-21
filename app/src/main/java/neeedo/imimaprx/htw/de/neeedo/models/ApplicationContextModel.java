package neeedo.imimaprx.htw.de.neeedo.models;


import android.content.Context;

public class ApplicationContextModel {

    private static ApplicationContextModel applicationContextModel;

    private Context applicationContext;

    private ApplicationContextModel() {

    }

    public static ApplicationContextModel getInstance() {
        if (applicationContextModel == null) {
            applicationContextModel = new ApplicationContextModel();
        }
        return applicationContextModel;
    }

    public Context getApplicationContext() {
        return applicationContext;
    }

    public void setApplicationContext(Context applicationContext) {
        this.applicationContext = applicationContext;
    }
}
