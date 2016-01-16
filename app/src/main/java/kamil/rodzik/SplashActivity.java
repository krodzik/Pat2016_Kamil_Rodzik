package kamil.rodzik;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.provider.Settings;
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

    Handler handler = new Handler();
    Globals g = Globals.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (DEBUG) Log.i(TAG, "on create");

        /*
        if(savedInstanceState != null){
            ifConfigurationChanged = savedInstanceState.getBoolean("configChanged");
            //Log.v("wartosc po powrocie : ", Boolean.toString(ifConfigurationChanged));
        }
        */
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (DEBUG) Log.i(TAG, "onresume");

        final int SPLASH_TIME_OUT = 5000;       /* Splash screen timer */

        if (DEBUG) Log.v("config-Resume", "" + ifConfigurationChanged);

        handler = g.getData();

        if (!ifConfigurationChanged) {
            if (DEBUG) Log.i(TAG, "odpala sie watek");

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.v("ifConfigcha in thread ", Boolean.toString(ifConfigurationChanged));
                    if (!ifBackPressed) {
                        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                        startActivity(intent);
                        if (DEBUG) Log.i(TAG, "odpala MAIN");
                        finish();
                    }
                }
            }, SPLASH_TIME_OUT);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        ifConfigurationChanged = isChangingConfigurations();
        if (DEBUG) Log.v("config-Paused", "" + ifConfigurationChanged);

        if(!ifConfigurationChanged) {
            if (DEBUG) Log.i(TAG, "kasujemy watek. Apka przechodzi do background");
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