package kamil.rodzik;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Kamil on 20.01.2016.
 * Class implementing SharedPreferences so we can use it across
 * whole app without writing same stuff again and again.
 */
public class SharedPref {
    private static final String MyPREFERENCES = "MyPrefs";
    private static final String Logged = "loggedKey";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    public SharedPref(Context context) {
        sharedPreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    public void changeIfLogged(Boolean ifLogged) {
        editor.putBoolean(Logged, ifLogged);
        editor.apply();
    }

    public Boolean getIfLogged() {
        return sharedPreferences.getBoolean(Logged, false);
    }
}
