package kamil.rodzik;

import android.app.ListFragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
 * Created by Kamil on 26.01.2016.
 *
 */
public class ModelListFragment extends ListFragment {
    // For logging.
    private static final String TAG = ModelListFragment.class.getSimpleName();
    private Logs log = new Logs(TAG);

    final static String BASE_SERVER_URL = "http://doom.comli.com/page_0.json";

    //ListView list_view;
    ArrayList<Model> modelList;
    ModelAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        modelList = new ArrayList<Model>();
        new JSONAsyncTask().execute(BASE_SERVER_URL);

        //ListView listView = (ListView)findViewById(R.id.list_view);
        adapter = new ModelAdapter(getActivity().getBaseContext(), R.layout.list_view, modelList);

        //listView.setAdapter(adapter);
        setListAdapter(adapter);

        return super.onCreateView(inflater, container, savedInstanceState);

        //View view = inflater.inflate(R.layout.list_fragment, container, false);
        //return view;
    }

    // params, progress, result
    public class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*
            dialog = new ProgressDialog();
            dialog.setMessage("Loading, pleas wait");
            dialog.setTitle("Connecting server");
            dialog.show();
            dialog.setCancelable(false);*/
        }


        @Override
        protected Boolean doInBackground(String... urls) {

            StringBuilder result = new StringBuilder();
            //HttpURLConnection urlConnection = null;

            try{

                //TODO CHECK IF WORKING
                URL url = new URL(urls[0]);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                //urlConnection = (HttpURLConnection) url.openConnection();
                ////urlConnection.connect();
                //InputStream in = new BufferedInputStream(urlConnection.getInputStream());
                BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                /*
                log.i("Po otwartym połaczeniu.");
                InputStream is = new BufferedInputStream(urlConnection.getInputStream());
                log.i("W przerwie.");
                BufferedReader in = new BufferedReader(new InputStreamReader(is));
                */


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

            //dialog.cancel();
            adapter.notifyDataSetChanged();
            if(result == false){
                //Toast.makeText(getApplicationContext(), "Unable to fetch data from server", Toast.LENGTH_LONG).show();
            }

            /* tego w tutku nie ma
            else {
                ModelAdapter adapter = new ModelAdapter(getApplicationContext(), R.layout.list_view , modelList);
                list_view.setAdapter(adapter);
            }
            */
        }
    }
}
