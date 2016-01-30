package kamil.rodzik;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

/**
 * Created by Kamil Rodzik on 11.01.2015.
 * Main screen.
 */

public class MainActivity extends Activity implements ProgressStatus.OnProgressBarStatusListener {
    // For logging.
    private static final String TAG = MainActivity.class.getSimpleName();
    private Logs log = new Logs(TAG);

    private SharedPref sharedPref;
    private ProgressBar progressBar;
    private int modelState = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        log.i("onCreate");
        // Sending context to SharedPreferences.
        sharedPref = new SharedPref(this);
        //log = new Logs(TAG);


        ProgressStatus.getProgressStatusInstance().setListener(this);
        modelState = ProgressStatus.getProgressStatusInstance().getProgress();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        if(savedInstanceState == null) {
            log.e("rowna sie null");
            progressBar.setVisibility(View.VISIBLE);
        }


        /* LOGOUT BUTTON LOGIC */
        Button mSignOutButton = (Button) findViewById(R.id.sign_out_button);
        mSignOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
    }


    @Override
    protected void onStart(){
        super.onStart();
        log.i("onStart");
    }


    private void logout() {
        log.i("logout()");
        sharedPref.changeIfLogged(false);
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }


    @Override
    public void stateChanged() {
        modelState = ProgressStatus.getProgressStatusInstance().getProgress();
        //log.i(" says: Model state changed : " + Integer.toString(modelState));
        // TODO to sie moze lub nie moze przydac do paginacji
        if (modelState == 0){
            log.e("modelState equal ZERO");
            progressBar.setVisibility(View.VISIBLE);
        }
        progressBar.setProgress(modelState);
        if (modelState == 10) {
            progressBar.setVisibility(View.GONE);
        }
    }


}
