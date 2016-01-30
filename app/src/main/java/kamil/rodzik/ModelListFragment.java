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
        //new JSONParser().execute(BASE_SERVER_URL);

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



}
