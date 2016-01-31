package kamil.rodzik;

import android.app.Activity;
import android.os.Bundle;
import android.content.Intent;
import android.os.Handler;

/**
 * Created by Kamil Rodzik on 23.12.2015.
 * Splash screen presenting while app is starting. If BACK button is pressed when on this screen
 * app stop running and stay on it. Otherwise it will go to next Activity (LoginActivity) after
 * SPLASH_TIME_OUT and finish SplashActivity.
 */
public class SplashActivity extends Activity {
    // For logging.
    private static final String TAG = SplashActivity.class.getSimpleName();
    private final Logs log = new Logs(TAG);

    private static final int SPLASH_TIME_OUT = 5000;    // Splash screen timer.

    private SharedPref sharedPref;

    private boolean ifBackPressed = false;  // For checking if BACK button were pressed.
    private boolean ifConfigurationChanged = false;

    private Handler handler = new Handler();
    private final GlobalHandler globalHandler = GlobalHandler.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref = new SharedPref(this);

        log.i("onCreate");
    }

    @Override
    protected void onResume() {
        super.onResume();
        log.i("onResume");
        log.bool("ifConfigurationChanged : ", ifConfigurationChanged);

        handler = globalHandler.getHandler();

        if (!ifConfigurationChanged) {
            if (sharedPref.getIfLogged()) {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                log.i("Start MainActivity - already Logged.");
                finish();
            } else {
                log.i("Thread start.");
                handler.postDelayed(endSplashScreen, SPLASH_TIME_OUT);
            }
        }
    }

    private Runnable endSplashScreen = new Runnable() {
        @Override
        public void run() {
            if (!ifBackPressed) {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                log.i("Thread is over. Next activity starting.");
                finish();
            }
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        log.i("onPause");

        ifConfigurationChanged = isChangingConfigurations();
        log.bool("ifConfigurationChanged : ", ifConfigurationChanged);

        if (!ifConfigurationChanged) {
            log.i("Delete thread. App goes in background.");
            handler = globalHandler.getHandler();
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
        log.i("Back was pressed.");
    }
}