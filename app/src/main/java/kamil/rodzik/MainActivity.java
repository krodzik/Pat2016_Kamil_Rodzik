package kamil.rodzik;

/* import android.support.v7.app.AppCompatActivity; */
import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Kamil Rodzik on 23.12.2015.
 * Main screen presenting simple "Hello World!" text.
 */
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
