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

    ModelAdapter adapter;
    ArrayList<Model> modelList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        log.i("onCreate");

        modelList = new ArrayList<>();
        adapter = new ModelAdapter(getActivity().getBaseContext(), R.layout.list_view, modelList);
        setListAdapter(adapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        log.i("onActivityCreated");

        setRetainInstance(true);
    }



}
