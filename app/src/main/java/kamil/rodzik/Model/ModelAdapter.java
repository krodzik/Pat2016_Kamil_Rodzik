package kamil.rodzik.Model;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import kamil.rodzik.Logs;
import kamil.rodzik.R;


/**
 * Created by Kamil on 24.01.2016.
 *
 */
public class ModelAdapter extends ArrayAdapter<Model> {

    // For logging.
    private static final String TAG = ModelAdapter.class.getSimpleName();
    private static Logs log = new Logs(TAG);

    ArrayList<Model> modelList;
    int Resource;
    LayoutInflater vi;
    ViewHolder holder;

    public ModelAdapter(Context context, int resource, ArrayList<Model> objects) {
        super(context, resource, objects);

        modelList = objects;
        Resource = resource;

        vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //ViewHolder holder;
        View v = convertView;
        // first item
        // so we create this layout only once
        // recycling and optimization
        if (v == null) {
            holder = new ViewHolder();
            v = vi.inflate(Resource, null);
            //convertView = vi.inflate(Resource, null);

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
        // If for some reason image could not be loaded from server it will stay the same as above
        new DownloadImageTask(holder.imageView).execute(modelList.get(position).getImage());


        return v;
    }

    static class ViewHolder {
        public TextView title;
        public TextView desc;
        public ImageView imageView;
    }

    private class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bitmapImage;

        public DownloadImageTask(ImageView bitmapImage) {
            this.bitmapImage = bitmapImage;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap minature = decodeSampledBitmapFromStream(url, 50, 50);
            if(minature == null) {
                log.i("Miniatura rowna sie 0, obrazek tymczasowy nie powininen sie zmienic");
            }

            return minature;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            bitmapImage.setImageBitmap(result);
        }
    }

    public static Bitmap decodeSampledBitmapFromStream(String url,
                                                       int reqWidth, int reqHeight) {
        try {
            InputStream inputStream1 = new java.net.URL(url).openStream();
            // First decode with inJustDecodeBounds=true to check dimensions
            final BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStream1, null, options);

            // Calculate inSampleSize
            options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

            // Decode bitmap with inSampleSize set
            options.inJustDecodeBounds = false;

            InputStream inputStream2 = new java.net.URL(url).openStream();
            return BitmapFactory.decodeStream(inputStream2, null, options);
        } catch (IOException e) {
            e.printStackTrace();
        }

        log.e("ERROR in decodeSampledBitmapFromStream");
        log.i("i.e. missing image or wrong url");

        //Resources res = Resources.getSystem().getColor(R.color.colorAccent, );
        //int id = R.color.colorAccent;
        /*
        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        */
        // zobaczymy czy dla 0 pojdzie
        return BitmapFactory.decodeStream(null ,null, null);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }
}
