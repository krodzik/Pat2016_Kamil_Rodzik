package kamil.rodzik;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;

/**
 * Created by Kamil Rodzik on 23.12.2015.
 * Splash screen presenting while app is starting. If BACK button is pressed when on this screen
 * app stop running and stay on it. Otherwise it will go to next Activity (MainActivity) after
 * SPLASH_TIME_OUT and finish SplashActivity.
 */
public class SplashActivity extends Activity {

    private boolean ifBack = false;                 /* For checking if BACK button were pressed */


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final int SPLASH_TIME_OUT = 5000;       /* Splash screen timer */

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!ifBack) {
                    Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        }, SPLASH_TIME_OUT);
    }

    @Override
    public void onBackPressed(){
        ifBack = true;
    }

    @Override
    protected void onPause(){
        super.onPause();
        finish();
    }
}