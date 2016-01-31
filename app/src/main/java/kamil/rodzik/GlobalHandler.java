package kamil.rodzik;

import android.os.Handler;

/**
 * Created by Kamil on 14.01.2016.
 * Singleton class. Here I storage globals variable.
 * By now using by splash screen for storing handler over multiple redraws
 * in i.e. screen orientation change.
 */
public class GlobalHandler {
    private static GlobalHandler instance;

    // Global variable
    private final Handler handler;

    // Restrict the constructor from being instantiated
    private GlobalHandler(){
        handler = new Handler();
    }

    /*
    public void setData(Handler d){
        this.handler=d;
    }
    */
    public Handler getHandler(){
        return this.handler;
    }

    public static synchronized GlobalHandler getInstance(){
        if(instance==null){
            instance=new GlobalHandler();
        }
        return instance;
    }
}
