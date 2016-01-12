package kamil.rodzik;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;
import android.widget.Toast;
import android.util.Log;

/**
 * Created by Kamil Rodzik on 23.12.2015.
 * Splash screen presenting while app is starting. If BACK button is pressed when on this screen
 * app stop running and stay on it. Otherwise it will go to next Activity (MainActivity) after
 * SPLASH_TIME_OUT and finish SplashActivity.
 */
public class SplashActivity extends Activity {

    private boolean ifBackPressed = false;                 /* For checking if BACK button were pressed */
    private boolean ifConfigurationChanged = false;
    private static final String TAG = "main";
    private boolean ifClosed = false;

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.i(TAG, "on create");

        if(savedInstanceState != null){
            ifConfigurationChanged = savedInstanceState.getBoolean("configChanged");
            //Log.v("wartosc po powrocie : ", Boolean.toString(ifConfigurationChanged));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //Log.i(TAG, "onstart");
    }

    @Override
    protected void onResume(){
        super.onResume();
        //Log.i(TAG, "onresume");
        ifClosed = false;
        //Log.v("ifClosed before  ", Boolean.toString(ifClosed));

        final int SPLASH_TIME_OUT = 5000;       /* Splash screen timer */

        Log.v("configChanged-Resume", "" + ifConfigurationChanged);

        if(!ifConfigurationChanged) {
            Log.i(TAG, "odpala sie watek");

            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.v("ifConfigcha in thread ", Boolean.toString(ifConfigurationChanged));
                    if (!ifBackPressed) {               // TODO dolozyc warunek
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(intent);
                        Log.i(TAG, "odpala MAIN");
                        finish();
                    }
                }
            }, SPLASH_TIME_OUT);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        ifClosed = true;
        //Log.v("ifClosed after  ", Boolean.toString(ifClosed));

        ifConfigurationChanged = isChangingConfigurations();
        Log.v("configChanged-Paused", "" + ifConfigurationChanged);

        if(!ifConfigurationChanged) {
            Log.i(TAG, "kasujemy watek. Apka przechodzi do background");
            handler.removeCallbacksAndMessages(null);
            ifClosed = true;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        //Log.i(TAG, "onstop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        savedInstanceState.putBoolean("configChanged", true);
    }

    @Override
    public void onBackPressed(){
        if(ifBackPressed) {
            super.onBackPressed();
        }
        ifBackPressed = true;
        Log.i(TAG, "back");
    }
}