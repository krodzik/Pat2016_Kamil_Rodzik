package kamil.rodzik;

import android.os.Handler;

/**
 * Created by Kamil on 14.01.2016.
 */
public class Globals {
    private static Globals instance;

    // Global variable
    private Handler handler;

    // Restrict the constructor from being instantiated
    private Globals(){
        handler = new Handler();
    }

    public void setData(Handler d){
        this.handler=d;
    }
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
