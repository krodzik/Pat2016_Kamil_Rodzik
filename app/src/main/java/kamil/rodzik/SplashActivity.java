package kamil.rodzik;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

/**
 * Created by Kamil Rodzik on 23.12.2015.
 * Splash screen presenting while app is starting. If BACK button is pressed when on this screen
 * app stop running and stay on it. Otherwise it will go to next Activity (LoginActivity) after
 * SPLASH_TIME_OUT and finish SplashActivity.
 */
public class SplashActivity extends Activity {

    private boolean ifBackPressed = false;  // For checking if BACK button were pressed.
    private boolean ifConfigurationChanged = false;
    private static final String TAG = "main";
    private static final boolean DEBUG = true;  // Set this to false to disable logs.

    private Handler handler = new Handler();
    private final Globals g = Globals.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (DEBUG) Log.i(TAG, "on create");
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (DEBUG) Log.i(TAG, "onResume()");

        // SharedPreferences variables and ifLogged to remember if user is logged
        final String MyPREFERENCES = "MyPrefs";
        final String Logged = "loggedKey";
        boolean ifLogged;   // For checking if user already been logged
        SharedPreferences sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        final int SPLASH_TIME_OUT = 5000;       /* Splash screen timer */

        if (DEBUG) Log.v("config-Resume", "" + ifConfigurationChanged);

        handler = g.getData();

        if (!ifConfigurationChanged) {
            ifLogged = sharedPreferences.getBoolean(Logged, false);
            if (ifLogged){
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                if (DEBUG) Log.i(TAG, "start MainActivity - already Logged");
                finish();
            }else {
                if (DEBUG) Log.i(TAG, "thread starting");
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (DEBUG)
                            Log.v("ifConfig in thread ", Boolean.toString(ifConfigurationChanged));
                        if (!ifBackPressed) {
                            Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                            startActivity(intent);
                            if (DEBUG) Log.i(TAG, "start MainActivity - time out");
                            finish();
                        }
                    }
                }, SPLASH_TIME_OUT);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        ifConfigurationChanged = isChangingConfigurations();
        if (DEBUG) Log.v("config-Paused", "" + ifConfigurationChanged);

        if(!ifConfigurationChanged) {
            if (DEBUG) Log.i(TAG, "delete thread. App goes in background");
            handler = g.getData();
            handler.removeCallbacksAndMessages(null);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean("configChanged", true);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        ifConfigurationChanged = savedInstanceState.getBoolean("configChanged");
    }

    @Override
    public void onBackPressed() {
        if (ifBackPressed) {
            super.onBackPressed();
        }
        ifBackPressed = true;
        if (DEBUG) Log.i(TAG, "back");
    }
}