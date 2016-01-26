package kamil.rodzik;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import kamil.rodzik.Model.Model;
import kamil.rodzik.Model.ModelAdapter;

/**
 * Created by Kamil Rodzik on 11.01.2015.
 * Main screen.
 */

public class MainActivity extends Activity {
    // For logging.
    private static final String TAG = MainActivity.class.getSimpleName();
    private Logs log;

    private SharedPref sharedPref;

    /*
    final static String BASE_SERVER_URL = "http://doom.comli.com/page_0.json";

    //ListView list_view;
    ArrayList<Model> modelList;
    ModelAdapter adapter;
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Sending context to SharedPreferences.
        sharedPref = new SharedPref(this);
        log = new Logs(TAG);

        /*
        modelList = new ArrayList<Model>();
        new JSONAsyncTask().execute(BASE_SERVER_URL);

        ListView listView = (ListView)findViewById(R.id.list);
        adapter = new ModelAdapter(getApplicationContext(), R.layout.list_view, modelList);

        listView.setAdapter(adapter);
*/

        /* LOGOUT BUTTON LOGIC */
        Button mSignOutButton = (Button) findViewById(R.id.sign_out_button);
        mSignOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

    }

    /*
    // params, progress, result
    public class JSONAsyncTask extends AsyncTask<String, Void, Boolean>{

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(MainActivity.this);
            dialog.setMessage("Loading, pleas wait");
            dialog.setTitle("Connecting server");
            dialog.show();
            dialog.setCancelable(false);
        }


        @Override
        protected Boolean doInBackground(String... urls) {

            StringBuilder result = new StringBuilder();
            //HttpURLConnection urlConnection = null;

            try{

                //TODO CHECK IF WORKING
                URL url = new URL(urls[0]);
                log.i("Otwiera połaczenie.");
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));


                String line = "";
                log.i("Wchodzi do while.");
                while ((line = in.readLine()) != null) {
                    result.append(line);
                }
                log.i("Jestesmy w while. Po warunku");

                JSONObject jsonObject = new JSONObject(result.toString());
                JSONArray jsonArray = jsonObject.getJSONArray("array");

                //JSONArray jsonArray = new JSONArray(line);

                for (int i = 0; i < jsonArray.length(); i++) {
                    Model model = new Model();

                    JSONObject jsonRealObject = jsonArray.getJSONObject(i);

                    model.setTitle(jsonRealObject.getString("title"));
                    model.setDesc(jsonRealObject.getString("desc"));
                    model.setImage(jsonRealObject.getString("url"));

                    modelList.add(model);
                }

                //urlConnection.disconnect();
                log.i("udalo sie!");
                return true;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            log.i("Nie udało się.");
            //urlConnection.disconnect();
            return false;
        }

        // nie ma override w tutku
        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            dialog.cancel();
            adapter.notifyDataSetChanged();
            if(result == false){
                Toast.makeText(getApplicationContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();
            }
        }
    }
*/
    private void logout(){
        log.i("logout()");
        sharedPref.changeIfLogged(false);
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
