package kamil.rodzik;

import android.app.ListFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    final static String BASE_SERVER_URL = "http://doom.comli.com/page_0.json";

    ArrayList<Model> modelList;
    ModelAdapter adapter;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        modelList = new ArrayList<>();
        new JSONAsyncTask().execute(BASE_SERVER_URL);

        adapter = new ModelAdapter(getActivity().getBaseContext(), R.layout.list_view, modelList);
        setListAdapter(adapter);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public class JSONAsyncTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            StringBuilder result = new StringBuilder();

            try {
                URL url = new URL(urls[0]);
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
                }
                log.i("Success parsing JSON!");
                return true;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            log.i("Fail parsing JSON.");
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            adapter.notifyDataSetChanged();

            if (!result) {
                Toast.makeText(getActivity().getBaseContext(),
                        "Unable to fetch data from server", Toast.LENGTH_LONG).show();
            }
        }
    }
}
