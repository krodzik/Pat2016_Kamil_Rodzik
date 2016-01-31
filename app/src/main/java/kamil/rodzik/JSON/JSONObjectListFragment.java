package kamil.rodzik.JSON;

import android.app.ListFragment;
import android.os.Bundle;

import java.util.ArrayList;

import kamil.rodzik.Logs;
import kamil.rodzik.R;

/**
 * Created by Kamil on 26.01.2016.
 * ListFragment.
 */
public class JSONObjectListFragment extends ListFragment {
    // For logging.
    private static final String TAG = JSONObjectListFragment.class.getSimpleName();
    private Logs log = new Logs(TAG);

    JSONListAdapter adapter;
    ArrayList<ObjectJSON> JSONObjectsList;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        JSONObjectsList = new ArrayList<>();
        adapter = new JSONListAdapter(getActivity().getBaseContext(), R.layout.list_view, JSONObjectsList);
        setListAdapter(adapter);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Handling orientation changes with this function. Allows to use same adapter.
        // onCreate() method will be call only once.
        setRetainInstance(true);
    }



}
