package kamil.rodzik;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Kamil Rodzik on 11.01.2015.
 * Main screen.
 */

public class MainActivity extends Activity {
    // For logging.
    private static final String TAG = MainActivity.class.getSimpleName();
    private Logs log;

    private SharedPref sharedPref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Sending context to SharedPreferences.
        sharedPref = new SharedPref(this);
        log = new Logs(TAG);

        /* LOGOUT BUTTON LOGIC */
        Button mSignOutButton = (Button) findViewById(R.id.sign_out_button);
        mSignOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

    }

    private void logout(){
        log.i("logout()");
        sharedPref.changeIfLogged(false);
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
