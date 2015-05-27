package neeedo.imimaprx.htw.de.neeedo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import neeedo.imimaprx.htw.de.neeedo.entities.User;
import neeedo.imimaprx.htw.de.neeedo.models.UserModel;
import neeedo.imimaprx.htw.de.neeedo.rest.HttpGetRefreshUserAsyncTask;
import neeedo.imimaprx.htw.de.neeedo.rest.HttpPostUserAsyncTask;


public class LoginActivity extends Activity {

    private EditText mEmailView;
    public EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private View mLoginError;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);

        mLoginFormView = findViewById(R.id.login_login);
        mProgressView = findViewById(R.id.login_progress);
        mLoginError = findViewById(R.id.login_error);

        Button mSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        Button registerButton = (Button) findViewById(R.id.btn_register);
        registerButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegisterForm();
            }
        });

        Button sendRegisterButton = (Button) findViewById(R.id.login_register_send);
        sendRegisterButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRegister();
            }
        });

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            finish();
        }
        handleFormDataChanged();
        return super.onKeyDown(keyCode, event);
    }

    private void handleFormDataChanged() {
        mLoginError.setVisibility(View.GONE);
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

            new HttpGetRefreshUserAsyncTask(email, password, this).execute();
        }
    }

    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(mPasswordView.getWindowToken(), 0);
        inputMethodManager.hideSoftInputFromWindow(mEmailView.getWindowToken(), 0);
    }

    private void openRegisterForm() {
        LinearLayout loginLayout = (LinearLayout) findViewById(R.id.login_login);
        LinearLayout loginRegister = (LinearLayout) findViewById(R.id.login_register);

        loginLayout.setVisibility(View.GONE);
        loginRegister.setVisibility(View.VISIBLE);

    }

    private void sendRegister() {

        TextView passwordError = (TextView) findViewById(R.id.login_password_error_text);
        TextView fieldsError = (TextView) findViewById(R.id.login_error_fields_empty_text);
        TextView emailError = (TextView) findViewById(R.id.login_email_error_text);

        passwordError.setVisibility(View.GONE);
        fieldsError.setVisibility(View.GONE);
        emailError.setVisibility(View.GONE);

        String name;
        String email;
        String password;
        String passwordConfirm;

        TextView temp = (TextView) findViewById(R.id.login_name_text);
        name = temp.getText().toString();
        temp = (TextView) findViewById(R.id.login_register_email);
        email = temp.getText().toString();
        temp = (TextView) findViewById(R.id.login_register_password);
        password = temp.getText().toString();
        temp = (TextView) findViewById(R.id.login_register_password_confirm);
        passwordConfirm = temp.getText().toString();


        if (name.isEmpty() | email.isEmpty() | password.isEmpty() | passwordConfirm.isEmpty()) {
            fieldsError.setVisibility(View.VISIBLE);
            return;
        }

        if (!(password.equals(passwordConfirm))) {
            passwordError.setVisibility(View.VISIBLE);
            return;
        }

        User user = new User();
        user.setUsername(name);
        user.setEmail(email);
        user.setPassword(password);

        UserModel.getInstance().setUser(user);

        new HttpPostUserAsyncTask().execute();

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

