package kamil.rodzik;

import android.util.Log;

/**
 * Created by Kamil on 20.01.2016.
 * Implementing Log class.
 * Using BuildConfig.DEBUG for checking if logs should be printed.
 */
public class Logs {

    private final String TAG;
    private final Boolean DEBUG = BuildConfig.DEBUG;

    public Logs(String tag){
        TAG = tag;
    }

    public void i(String msg) {
        if(DEBUG)
            Log.i(TAG, msg);
    }

    public void bool(String msg, Boolean tr) {
        if(DEBUG)
            Log.i(TAG, msg + " " + Boolean.toString(tr));
    }
}
