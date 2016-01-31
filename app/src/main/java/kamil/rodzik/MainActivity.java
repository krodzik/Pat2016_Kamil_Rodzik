package kamil.rodzik;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

/**
 * Created by Kamil Rodzik on 11.01.2015.
 * Main screen.
 */

public class MainActivity extends Activity implements ProgressStatus.OnProgressBarStatusListener {
    // For logging.
    private static final String TAG = MainActivity.class.getSimpleName();
    private Logs log = new Logs(TAG);

    private SharedPref sharedPref;

    // Set two progress bars. One for showing progress of loading file from server. Second for
    // informing user if next list page is being loaded.
    private ProgressBar progressBar;
    private ProgressBar progressBarUnder;
    private int progressBarStatus = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        log.i("onCreate");
        // Sending context to SharedPreferences.
        sharedPref = new SharedPref(this);

        // Set listener for incoming progress change from Adapter.
        ProgressStatus.getProgressStatusInstance().setListener(this);
        progressBarStatus = ProgressStatus.getProgressStatusInstance().getProgress();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        // Show progress bar for the first activity call.
        if(savedInstanceState == null) {
            progressBar.setVisibility(View.VISIBLE);
        }
        progressBarUnder = (ProgressBar) findViewById(R.id.progressBarUnder);

        // Logout button logic.
        Button mSignOutButton = (Button) findViewById(R.id.sign_out_button);
        mSignOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
    }

    private void logout() {
        log.i("logout()");
        sharedPref.changeIfLogged(false);
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void progressChanged() {
        // Get progress status from JSONListAdapter.
        // For "0" to "10" changing progress in top progress bar.
        // "-1" means that there's error and JSON file can't be read.
        // "100" is for changing visibility of bottom progress bar denoting new list page is
        // loading or being already loaded
        progressBarStatus = ProgressStatus.getProgressStatusInstance().getProgress();
        if (progressBarStatus == 0){
            progressBar.setVisibility(View.VISIBLE);
        }
        progressBar.setProgress(progressBarStatus);
        if (progressBarStatus == 10) {
            progressBar.setVisibility(View.GONE);
        }
        // Show message to user when JSON file can't be downloaded.
        if(progressBarStatus == -1) {
            if(progressBar.getVisibility() == View.VISIBLE)
                progressBar.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this,
                    "Unable to fetch data from server", Toast.LENGTH_LONG).show();
        }
        // Set visibility for bottom progress bar when new list page is loading.
        if(progressBarStatus == 100){
            if(progressBarUnder.getVisibility() == View.GONE){
                progressBarUnder.setVisibility(View.VISIBLE);
            }
        }
        if(progressBarStatus == 101){
            if(progressBarUnder.getVisibility() == View.VISIBLE){
                progressBarUnder.setVisibility(View.GONE);
            }
        }
    }

    private void changeVisibility(ProgressBar progressBarToChangeVisibility){
        if(progressBarToChangeVisibility.getVisibility() == View.GONE){
            progressBarToChangeVisibility.setVisibility(View.VISIBLE);
        } else {
            progressBarToChangeVisibility.setVisibility(View.GONE);
        }
    }
}
