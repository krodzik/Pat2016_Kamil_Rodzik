package kamil.rodzik.Model;

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
    private static Logs log;

    final static String URL_OF_TEMP_IMAGE = "http://doom.comli.com/ic_launcher.png";
    private final int IMAGE_HEIGHT = 100;
    private final int IMAGE_WIDTH = 100;

    private LruCache<String, Bitmap> mMemoryCache;

    public ImageLoader() {
        log = new Logs(TAG);

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
        log.i("wartosc klucza przy dodaniu : " + key);
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        log.i("wartosc klucza przy pobieraniu : " + key);
        return mMemoryCache.get(key);
    }

    public void loadBitmap(String resId, ImageView imageView) {
        final String imageKey = resId;

        final Bitmap bitmap = getBitmapFromMemCache(imageKey);
        log.i("wartosc klucza przy sprawdzaniu: " + imageKey);
        if (bitmap != null) {
            log.i("Image already in cache.");
            imageView.setImageBitmap(bitmap);
        } else {
            log.i("No such image in cache. Downloading...");
            //imageView.setImageResource(R.mipmap.ic_launcher);
            new DownloadImageTask(imageView).execute(resId);
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

            if(miniature == null) {
                log.i("Temporary image should show.");
                url = URL_OF_TEMP_IMAGE;
                final Bitmap bitmap = getBitmapFromMemCache(url);
                if (bitmap != null) {
                    //imageView.setImageBitmap(bitmap);
                    log.i("Obraz tymczasowy juz wczesniej wczytany. Nie wczytuje jeszcze raz.");
                    miniature = bitmap;
                } else {
                    miniature = decodeSampledBitmapFromStream(url, IMAGE_WIDTH, IMAGE_HEIGHT);
                }
            }

            addBitmapToMemoryCache(url, miniature);

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
            e.printStackTrace();
        }

        log.e("ERROR in decodeSampledBitmapFromStream");
        log.e("i.e. missing image or wrong url");

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
