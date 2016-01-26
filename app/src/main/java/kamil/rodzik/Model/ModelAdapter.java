package kamil.rodzik.Model;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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

    ImageLoader imageLoader = new ImageLoader();


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

        log.i("Image loading ... ");
        //
        imageLoader.loadBitmap(modelList.get(position).getImage(), holder.imageView);

        //imageLoader.new DownloadImageTask(holder.imageView).execute(modelList.get(position).getImage());


        return v;
    }

    static class ViewHolder {
        public TextView title;
        public TextView desc;
        public ImageView imageView;
    }
}
