package kamil.rodzik;

import android.app.ListFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
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
 * ListFragment
 */
public class ModelListFragment extends ListFragment {
    // For logging.
    private static final String TAG = ModelListFragment.class.getSimpleName();
    private Logs log = new Logs(TAG);

    final static String BASE_SERVER_URL = "http://doom.comli.com/";

    ArrayList<Model> modelList;
    ModelAdapter adapter;

    private ProgressBar progressBar;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        log.e("onCreate");
        //setRetainInstance(true);

        modelList = new ArrayList<>();
        new JSONAsyncTask().execute(BASE_SERVER_URL);

        // context, resource, ...
        adapter = new ModelAdapter(getActivity().getBaseContext(), R.layout.list_view, modelList);
        setListAdapter(adapter);
    }

    /*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        log.i("onCreateView");
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    */


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        log.i("onActivityCreated");

        setRetainInstance(true);

    }


    // TODO export to another class
    public class JSONAsyncTask extends AsyncTask<String, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Send "0" to initiate progress bar.

            ProgressStatus.getProgressStatusInstance().changeProgress(0);
            log.e("Sending 0");

        }

        @Override
        protected Boolean doInBackground(String... urls) {

            StringBuilder result = new StringBuilder();

            try {
                // here it takes pagination
                int fileNumber = 0;
                String urlString = urls[0] + "page_" + Integer.toString(fileNumber) + ".json";
                log.i(urlString);
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(urlConnection.getInputStream()));

                String line;
                while ((line = in.readLine()) != null) {
                    result.append(line);
                }

                JSONObject jsonObject = new JSONObject(result.toString());
                JSONArray jsonArray = jsonObject.getJSONArray("array");

                for (int i = 0; i < jsonArray.length(); i++) {
                    Model model = new Model();

                    JSONObject jsonRealObject = jsonArray.getJSONObject(i);

                    model.setTitle(jsonRealObject.getString("title"));
                    model.setDesc(jsonRealObject.getString("desc"));
                    model.setImage(jsonRealObject.getString("url"));

                    modelList.add(model);
                    //log.i("Progress update : " + Integer.toString(i + 1));
                    publishProgress(i + 1);
                    Thread.sleep(250);
                }
                log.i("Success parsing JSON!");
                return true;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
                // dla sleepa
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            log.i("Fail parsing JSON.");
            return false;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            //progressBar.setProgress(progress[0]);
            ProgressStatus.getProgressStatusInstance().changeProgress(progress[0]);
            //progressBar.setProgress(progress[0]);
            //log.i("Set progress : " + Integer.toString(progress[0]));
            //adapter.notifyDataSetChanged();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            //progressBar.setVisibility(View.GONE);
            adapter.notifyDataSetChanged();

            if (!result) {
                Toast.makeText(getActivity().getBaseContext(),
                        "Unable to fetch data from server", Toast.LENGTH_LONG).show();
            }
        }
    }
}
