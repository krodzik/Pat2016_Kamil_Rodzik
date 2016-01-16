package kamil.rodzik;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Created by Kamil Rodzik on 11.01.2015.
 * Main screen.
 */

public class MainActivity extends Activity {

    private static final String TAG = "main";
    private static final boolean DEBUG = true;  // Set this to false to disable logs.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button mSignOutButton = (Button) findViewById(R.id.sign_out_button);
        mSignOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
    }

    private void logout(){
        if (DEBUG) Log.i(TAG, "logout()");
        // SharedPreferences variables and ifLogged to remember if user is logged
        // Changing ifLogged to false on Logout click
        final String MyPREFERENCES = "MyPrefs";
        final String Logged = "loggedKey";
        boolean ifLogged = false;
        SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean(Logged, ifLogged);
        editor.commit();

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
