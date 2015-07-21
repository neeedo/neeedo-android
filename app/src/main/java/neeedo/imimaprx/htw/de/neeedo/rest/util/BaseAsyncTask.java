package neeedo.imimaprx.htw.de.neeedo.rest.util;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import org.springframework.http.HttpBasicAuthentication;
import org.springframework.http.HttpHeaders;

import neeedo.imimaprx.htw.de.neeedo.R;
import neeedo.imimaprx.htw.de.neeedo.events.ServerResponseEvent;
import neeedo.imimaprx.htw.de.neeedo.models.ActiveUser;
import neeedo.imimaprx.htw.de.neeedo.models.ApplicationContextModel;
import neeedo.imimaprx.htw.de.neeedo.rest.util.returntype.RestResult;
import neeedo.imimaprx.htw.de.neeedo.service.EventService;

public abstract class BaseAsyncTask extends AsyncTask {

    public enum GetEntitiesMode {
        GET_BY_USER, GET_RANDOM
    }

    public enum SendMode {
        CREATE, UPDATE
    }

    public enum CompletionType {
        PHRASE, TAG
    }

    public enum EntityType {
        DEMAND, OFFER, USER, FAVORITE
    }

    protected EventService eventService = EventService.getInstance();

    @Override
    protected void onPostExecute(Object o) {
        eventService.post(new ServerResponseEvent());
    }

    protected void setAuthorisationHeaders(HttpHeaders requestHeaders) {
        final ActiveUser activeUser = ActiveUser.getInstance();
        HttpBasicAuthentication authentication = new HttpBasicAuthentication(activeUser.getUsername(), activeUser.getUserPassword());
        requestHeaders.setAuthorization(authentication);
    }


    protected boolean wasSuccessful(Object o) {
        if (o instanceof RestResult) {
            RestResult restResult = (RestResult) o;
            if (restResult.getResult() == RestResult.ReturnType.SUCCESS) {
                return true;
            }
        }
        return false;
    }

    public String getErrorMessage(String exception) {
        Context context = ApplicationContextModel.getInstance().getApplicationContext();
        String message = context.getString(R.string.exception_message_error_unkown);

        if (exception.contains("401")) {
            message = context.getString(R.string.authorization_error);
        }
        if (exception.contains("403")) {
            message = context.getString(R.string.login_error_text);
        }
        if (exception.contains("503")) {
            message = context.getString(R.string.exception_message_server_not_available);
        }
        if (exception.contains("409")) {
            message = context.getString(R.string.exception_message_email_already_in_use);
        }
        if (exception.contains("500")) {
            message = context.getString(R.string.exception_message_internal_server_error);
        }
        if (exception.contains("Unable to resolve host")) {
            message = context.getString(R.string.exception_message_no_internet);
        }
        return message;
    }

    public void showToast(final String message) {

        Handler mHandler = new Handler(Looper.getMainLooper());
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ApplicationContextModel.getInstance().getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }

}


