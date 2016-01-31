package kamil.rodzik.JSON;

import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import kamil.rodzik.Logs;
import kamil.rodzik.ProgressStatus;
import kamil.rodzik.R;

/**
 * Created by Kamil on 24.01.2016.
 * Adapter for JSON object list.
 */
public class JSONListAdapter extends ArrayAdapter<ObjectJSON> {
    // For logging.
    private static final String TAG = JSONListAdapter.class.getSimpleName();
    private static Logs log = new Logs(TAG);

    final static String BASE_SERVER_URL = "http://doom.comli.com/";

    ArrayList<ObjectJSON> JSONObjectsList;
    int Resource;
    LayoutInflater vi;
    ViewHolder holder;

    ImageLoader imageLoader;

    final int objectsPerJSONFile = 10;
    // Array that stole already visited last elements of JSONObjectList.
    // Used with pagination.
    List<Integer> alreadyVisitedLastPositions;


    public JSONListAdapter(Context context, int resource, ArrayList<ObjectJSON> objects) {
        super(context, resource, objects);

        Resource = resource;
        JSONObjectsList = objects;
        alreadyVisitedLastPositions = new ArrayList<>();

        imageLoader = new ImageLoader(context);

        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        new JSONParser().execute();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

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

        holder.title.setText(JSONObjectsList.get(position).getTitle());
        holder.desc.setText(JSONObjectsList.get(position).getDesc());
        holder.imageView.setImageResource(R.mipmap.ic_launcher);

        //log.i("Image loading ... ");
        imageLoader.loadBitmap(JSONObjectsList.get(position).getImage(), holder.imageView);

        // Checking if scrolled to end of list. If yes then trying to get next list elements from
        // server.
        if (checkIfListEnd(position)) {
            //log.i("Parsing next JSON file.");
            new JSONParser().execute();
            alreadyVisitedLastPositions.add(position);
        }

        return v;
    }

    private boolean checkIfListEnd(int position) {
        // Checking if end of the list.
        if (!((position + 1) % objectsPerJSONFile == 0))
            return false;
        // Checking if already viewed.
        return !(alreadyVisitedLastPositions.contains(position));
    }

    static class ViewHolder {
        public TextView title;
        public TextView desc;
        public ImageView imageView;
    }


    public class JSONParser extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Send "100" to set progress bar visibility to VISIBLE.
            ProgressStatus.getProgressStatusInstance().changeProgress(100);
        }

        @Override
        protected Boolean doInBackground(Void... unused) {

            int fileNumberToParse = findFileNumberToParse();
            String combinedURL = combineIntoURL(fileNumberToParse);

            try {
                URL url = new URL(combinedURL);

                if (isURLReachable(url)) {
                    log.i("URL reachable. Start parsing.");
                } else {    // There's no more JSON file to download.
                    log.e("URL unreachable. There's no more JSON file to download.");
                    return true;
                }

                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(urlConnection.getInputStream()));

                String stringDataFromURL = getStringDataFromURL(bufferedReader);

                urlConnection.disconnect();

                JSONObject jsonObject = new JSONObject(stringDataFromURL);
                JSONArray jsonArray = jsonObject.getJSONArray("array");

                getObjectsFromJSONArray(jsonArray);

                log.i("Success parsing JSON!");
                return true;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (IOException e) {
                log.e("URL unreachable. Exception throw.");
                e.printStackTrace();
            } catch (InterruptedException e) {  // TODO For sleep
                e.printStackTrace();
            }

            log.e("Fail parsing JSON.");
            return false;
        }

        private int findFileNumberToParse() {
            int fileNumber = 0;
            int JSONObjectListLength = JSONObjectsList.size();
            log.I("Number of elements in JSONObjectsList =", JSONObjectListLength);

            while ((fileNumber * 10) < JSONObjectListLength) {
                fileNumber++;
            }
            return fileNumber;
        }

        private String combineIntoURL(int fileNumber) {
            String combinedURL = BASE_SERVER_URL + "page_" + Integer.toString(fileNumber) + ".json";
            log.i("URL : " + combinedURL);
            return combinedURL;
        }

        private boolean isURLReachable(URL url) throws IOException {
            HttpURLConnection.setFollowRedirects(false);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("HEAD");
            int responseCode = connection.getResponseCode();
            log.I("getResponseCode =", responseCode);
            HttpURLConnection.setFollowRedirects(true);
            connection.disconnect();

            return (responseCode == HttpURLConnection.HTTP_OK);
        }

        private String getStringDataFromURL(BufferedReader bufferedReader) throws IOException {
            StringBuilder dataFromURL = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                dataFromURL.append(line);
            }

            return dataFromURL.toString();
        }

        private void getObjectsFromJSONArray(JSONArray jsonArray) throws JSONException, InterruptedException {
            for (int i = 0; i < jsonArray.length(); i++) {
                ObjectJSON objectJSON = new ObjectJSON();

                JSONObject jsonRealObject = jsonArray.getJSONObject(i);

                objectJSON.setTitle(jsonRealObject.getString("title"));
                objectJSON.setDesc(jsonRealObject.getString("desc"));
                objectJSON.setImage(jsonRealObject.getString("url"));

                JSONObjectsList.add(objectJSON);
                publishProgress(i + 1);
                // TODO delete sleep
                Thread.sleep(250);
            }
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Sending progress to progress bar in MainActivity.
            ProgressStatus.getProgressStatusInstance().changeProgress(progress[0]);
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);

            notifyDataSetChanged();

            // There's no more file to download from server.
            // Using ProgressStatus to hide progress bar in MainActivity.
            ProgressStatus.getProgressStatusInstance().changeProgress(101);

            // Error when trying fetch data from server.
            // Using ProgressStatus to notify MainActivity so proper message can be shown.
            if (!result) {
                ProgressStatus.getProgressStatusInstance().changeProgress(-1);
            }
        }
    }

}
