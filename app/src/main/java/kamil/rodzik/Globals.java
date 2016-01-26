package kamil.rodzik;

import android.os.Handler;

import kamil.rodzik.Model.ImageLoader;

/**
 * Created by Kamil on 14.01.2016.
 * Singleton class. Here I storage globals variable.
 * By now using by splash screen for storing handler over multiple redraws
 * in i.e. screen orientation change.
 */
public class Globals {
    private static Globals instance;

    // Global variable
    private final Handler handler;

    // Restrict the constructor from being instantiated
    private Globals(){
        handler = new Handler();
    }

    /*
    public void setData(Handler d){
        this.handler=d;
    }
    */
    public Handler getData(){
        return this.handler;
    }

    public static synchronized Globals getInstance(){
        if(instance==null){
            instance=new Globals();
        }
        return instance;
    }
}
