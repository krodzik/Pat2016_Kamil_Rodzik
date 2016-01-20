package kamil.rodzik;

/* import android.support.v7.app.AppCompatActivity; */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import java.util.regex.Pattern;

/**
 * Created by Kamil Rodzik on 11.01.2015.
 * Login screen.
 */
public class LoginActivity extends Activity {

    private static final String TAG = LoginActivity.class.getSimpleName(); // For logging.
    private static final boolean DEBUG = true;  // Set this to false to disable logs.

    private EditText mEmailView;
    private EditText mPasswordView;

    private SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);

        Button mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DEBUG) Log.i(TAG, "onClick");
                attemptLogin();
            }
        });

        // Need to send context to SharedPreferences
        sharedPref = new SharedPref(this);
    }

    private void attemptLogin(){
        if (DEBUG) Log.i(TAG, "attemptLogin()");
        // SharedPreferences variables and ifLogged to remember if user is logged
        /*
        final String MyPREFERENCES = "MyPrefs";
        final String Logged = "loggedKey";
        boolean ifLogged;
        SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        */
        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password.
        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError(getString(R.string.error_field_required));
            focusView = mPasswordView;
            cancel = true;
        } else if (!isPasswordValid(password)) {
            //mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
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
            if (DEBUG) Log.i(TAG, "attemptLogin() - FAIL");
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            if (DEBUG) Log.i(TAG, "attemptLogin() - SUCCESS");
            // Saves logged state to SharedPreferences
            sharedPref.changeIfLogged(true);
            /*
            ifLogged = true;
            editor.putBoolean(Logged, ifLogged);
            editor.commit();
            */
            // Everything seems fine. Start next activity and finish this one.
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            if (DEBUG) Log.i(TAG, "start MainActivity");
            finish();
        }
    }

    private boolean isEmailValid(String email) {
        //return email.contains("@");
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordValid(String password) {
        boolean validate;
        // Set patterns.
        final Pattern hasUppercase = Pattern.compile("[A-Z]");
        final Pattern hasLowercase = Pattern.compile("[a-z]");
        final Pattern hasNumber = Pattern.compile("\\d");

        // Check if password is valid.
        if (password.length() >= 8){
            validate = true;
        } else {
            validate = false;
            mPasswordView.setError(getString(R.string.error_invalid_password_length));
        }
        if (validate && hasUppercase.matcher(password).find()){
            validate = true;
        } else if (validate){
            validate = false;
            mPasswordView.setError(getString(R.string.error_invalid_password_uppercase));
        }
        if (validate && hasLowercase.matcher(password).find()){
            validate = true;
        } else if (validate){
            validate = false;
            mPasswordView.setError(getString(R.string.error_invalid_password_lowercase));
        }
        if (validate && hasNumber.matcher(password).find()){
            validate = true;
        } else if (validate){
            validate = false;
            mPasswordView.setError(getString(R.string.error_invalid_password_digit));
        }

        return validate;
    }
}
