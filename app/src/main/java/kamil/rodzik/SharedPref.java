package kamil.rodzik;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Kamil on 20.01.2016.
 * Class implementing SharedPreferences so we can use it across
 * whole app without writing same stuff again and again.
 */
public class SharedPref {
    static final String MyPREFERENCES = "MyPrefs";
    static final String Logged = "loggedKey";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public SharedPref(Context context){
        sharedPreferences = context.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void changeIfLogged(Boolean ifLogged){
        editor.putBoolean(Logged, ifLogged);
        editor.apply();
    }

    public Boolean getIfLogged(){
        return sharedPreferences.getBoolean(Logged, false);
    }
}
