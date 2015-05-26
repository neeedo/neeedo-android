package neeedo.imimaprx.htw.de.neeedo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import neeedo.imimaprx.htw.de.neeedo.models.ActiveUser;
import neeedo.imimaprx.htw.de.neeedo.rest.HttpGetUserAsyncTask;


public class LoginActivity extends Activity {

    private AutoCompleteTextView mEmailView;
    public EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private View mLoginError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        mLoginFormView = findViewById(R.id.login_login);
        mProgressView = findViewById(R.id.login_progress);
        mLoginError = findViewById(R.id.login_error);

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });



        Button registerBtn = (Button) findViewById(R.id.btn_register);
        registerBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegisterForm();
            }
        });
    }

    private void clearErrorMessages() {

    }

    public void attemptLogin() {
        mEmailView.setError(null);
        mPasswordView.setError(null);

        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            focusView.requestFocus();
        } else {
            hideKeyboard();
            showProgress(true);

            new HttpGetUserAsyncTask(email, password, this).execute();
        }
    }

    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(mPasswordView.getWindowToken(), 0);
        inputMethodManager.hideSoftInputFromWindow(mEmailView.getWindowToken(), 0);
    }

    private void openRegisterForm() {
    }

    private boolean isEmailValid(String email) {
        //TODO remove "true" for release
        return true || email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 4;
    }

    public void showProgress(final boolean show) {
        mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void setWrongCredentials() {
        showProgress(false);
        mLoginError.setVisibility(View.VISIBLE);
        mPasswordView.requestFocus();
    }
}

