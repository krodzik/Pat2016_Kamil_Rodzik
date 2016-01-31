package kamil.rodzik.JSON;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;

import kamil.rodzik.Logs;

/**
 * Created by Kamil on 26.01.2016.
 *
 */
public class ImageLoader {
    // For logging.
    private static final String TAG = ImageLoader.class.getSimpleName();
    private static Logs log = new Logs(TAG);

    // Variables for temporary image.
    private static Resources resources;
    private static int image;
    private static String imageName;

    private final int IMAGE_HEIGHT = 70;
    private final int IMAGE_WIDTH = 70;

    private LruCache<String, Bitmap> mMemoryCache;


    public ImageLoader(Context context) {

        resources = context.getResources();
        image = context.getResources().getIdentifier("temporary_image", "drawable",
                context.getPackageName());
        imageName = resources.getString(image);

        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    public void loadBitmap(String imageKey, ImageView imageView) {

        Bitmap bitmap = getBitmapFromMemCache(imageKey);
        if (bitmap != null) {
            //log.i("Image already in cache.");
            imageView.setImageBitmap(bitmap);
        } else {
            //log.i("No such image in cache. Downloading...");
            new DownloadImageTask(imageView).execute(imageKey);
        }
    }

    public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ImageView bitmapImage;

        public DownloadImageTask(ImageView bitmapImage) {
            this.bitmapImage = bitmapImage;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String url = urls[0];
            Bitmap miniature = decodeSampledBitmapFromStream(url, IMAGE_WIDTH, IMAGE_HEIGHT);

            if (miniature != null) {
                addBitmapToMemoryCache(url, miniature);
            } else {
                //log.i("Temporary image should show.");
                miniature = getBitmapFromMemCache(imageName);
                if (miniature != null) {
                    //log.i("Temporary image already in cache.");
                } else {
                    miniature = decodeSampledBitmapFromResource(resources, image,
                            IMAGE_WIDTH, IMAGE_HEIGHT);
                    addBitmapToMemoryCache(imageName, miniature);
                }
            }
            return miniature;
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
            // TODO zmienic potem
            //e.printStackTrace();
        }

        //log.e("ERROR in decodeSampledBitmapFromStream");
        //log.e("i.e. missing image or wrong url");

        return BitmapFactory.decodeStream(null, null, null);
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources path, int id,
                                                     int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(path, id, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(path, id, options);
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
