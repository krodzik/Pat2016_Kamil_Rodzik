package kamil.rodzik;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
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
    // For logging.
    private static final String TAG = LoginActivity.class.getSimpleName();
    private Logs log;

    private SharedPref sharedPref;

    private EditText mEmailView;
    private EditText mPasswordView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Sending context to SharedPreferences.
        sharedPref = new SharedPref(this);
        log = new Logs(TAG);

        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);

        Button mSignInButton = (Button) findViewById(R.id.sign_in_button);
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                log.i("onClick");
                attemptLogin();
            }
        });
    }

    private void attemptLogin(){
        log.i("attemptLogin()");
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
            log.i("attemptLogin() - FAIL");
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            log.i("attemptLogin() - SUCCESS");
            // Saves logged state to SharedPreferences
            sharedPref.changeIfLogged(true);
            // Everything seems fine. Start next activity and finish this one.
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            log.i("start MainActivity");
            finish();
        }
    }

    private boolean isEmailValid(String email) {
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
