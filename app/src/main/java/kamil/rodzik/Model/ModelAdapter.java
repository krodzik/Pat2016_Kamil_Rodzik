package kamil.rodzik.Model;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
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
import java.util.List;

import kamil.rodzik.Logs;
import kamil.rodzik.ProgressStatus;
import kamil.rodzik.R;

/**
 * Created by Kamil on 24.01.2016.
 * Adapter for JSON object
 */
public class ModelAdapter extends ArrayAdapter<Model>  {
    // For logging.
    private static final String TAG = ModelAdapter.class.getSimpleName();
    private static Logs log = new Logs(TAG);

    ArrayList<Model> modelList;
    int Resource;
    LayoutInflater vi;
    ViewHolder holder;

    ImageLoader imageLoader;

    final static String BASE_SERVER_URL = "http://doom.comli.com/";
    final int objectsPerJSONFile = 10;
    List<Integer> alreadyWelcomePositions = new ArrayList<>();


    public ModelAdapter(Context context, int resource, ArrayList<Model> objects) {
        super(context, resource, objects);

        Resource = resource;
        modelList = objects;

        imageLoader = new ImageLoader(context);

        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        alreadyWelcomePositions.add(0);
        new JSONParser().execute();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        log.i("Pozycja : " + Integer.toString(position));
        if (((position % 9) == 0) && !(alreadyWelcomePositions.contains(position))) {
            log.e("Zaladowanie nowej listy");
            new JSONParser().execute();
            alreadyWelcomePositions.add(position);
        }

        View v = convertView;
        if (v == null) {
            holder = new ViewHolder();
            v = vi.inflate(Resource, null);

            holder.title = (TextView) v.findViewById(R.id.title);
            holder.desc = (TextView) v.findViewById(R.id.desc);
            holder.imageView = (ImageView) v.findViewById(R.id.imageView);

            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        holder.title.setText(modelList.get(position).getTitle());
        holder.desc.setText(modelList.get(position).getDesc());
        holder.imageView.setImageResource(R.mipmap.ic_launcher);

        log.i("Image loading ... ");
        imageLoader.loadBitmap(modelList.get(position).getImage(), holder.imageView);

        return v;
    }

    static class ViewHolder {
        public TextView title;
        public TextView desc;
        public ImageView imageView;
    }

    // TODO export to another class
    public class JSONParser extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Send "0" to initiate progress bar.

            ProgressStatus.getProgressStatusInstance().changeProgress(0);
            log.i("Sending 0");

        }

        @Override
        protected Boolean doInBackground(Void... unused) {

            StringBuilder result = new StringBuilder();

            int fileNumber = 0;
            int modelListLength = modelList.size();
            log.i("Liczba elementow w modelList = " + Integer.toString(modelListLength));

            while((fileNumber*10) < modelListLength){
                fileNumber++;
            }

            log.i(" strona : " + Integer.toString(fileNumber));
            String urlString = BASE_SERVER_URL + "page_" + Integer.toString(fileNumber) + ".json";

            try {
                // here it takes pagination
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
            log.i("Notify change.");
            notifyDataSetChanged();

            // Error when trying fetch data from server. Using ProgressStatus to notify MainActivity
            if (!result) {
                ProgressStatus.getProgressStatusInstance().changeProgress(-1);
            }

        }
    }

}
