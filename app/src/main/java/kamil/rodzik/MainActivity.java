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
    private ProgressBar progressBar;
    private int progressBarStatus = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        log.i("onCreate");
        // Sending context to SharedPreferences.
        sharedPref = new SharedPref(this);

        ProgressStatus.getProgressStatusInstance().setListener(this);
        progressBarStatus = ProgressStatus.getProgressStatusInstance().getProgress();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        // Show progress bar for the first activity call.
        if(savedInstanceState == null) {
            progressBar.setVisibility(View.VISIBLE);
        }

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
    public void stateChanged() {
        // Get progress status from JSONListAdapter.
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
            progressBar.setVisibility(View.GONE);
            Toast.makeText(MainActivity.this,
                    "Unable to fetch data from server", Toast.LENGTH_LONG).show();
        }

    }
}
