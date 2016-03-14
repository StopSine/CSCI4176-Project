package csci4176.toptentoday;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Blair on 3/13/2016.
 */
public class CustomArrayAdapter extends ArrayAdapter<ListItem> {

    private static final String TAG = "CustomArray";
    private LruCache<String, Bitmap> mMemoryCache;

    public CustomArrayAdapter(Context context, ArrayList<ListItem> listItems) {
        super(context, 0, listItems);
        // Get memory class of this device, exceeding this amount will throw an
        // OutOfMemory exception.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {

            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in bytes rather than number
                // of items.
                return bitmap.getByteCount() / 1024;
            }

        };
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ListItem item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        TextView title = (TextView) convertView.findViewById(R.id.listTitle);
        TextView subTitle = (TextView) convertView.findViewById(R.id.listSubTitle);
        ImageView image = (ImageView) convertView.findViewById(R.id.listImage);;

        title.setText(item.title);
        subTitle.setText(item.subTitle);

        if (item.imgUrl != null && !item.imgUrl.isEmpty()) {
            loadBitmap(item.imgUrl, image);
            image.invalidate();
        }
        return convertView;
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    public void loadBitmap(String url, ImageView image) {
        final String imageKey = String.valueOf(url);

        final Bitmap bitmap = getBitmapFromMemCache(imageKey);
        if (bitmap != null) {
            image.setImageBitmap(bitmap);
        } else {
            BitmapWorkerTask task = new BitmapWorkerTask(image);
            task.execute(url);
        }
    }

    class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;

        public BitmapWorkerTask(ImageView image) {
            imageViewReference = new WeakReference<ImageView>(image);
        }
        @Override
        protected Bitmap doInBackground(String... url) {
            Bitmap b = null;
            try {
                URL imgUrl = new URL(url[0]);
                b = BitmapFactory.decodeStream(imgUrl.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            addBitmapToMemoryCache(url[0], b);
            return b;
        }

        @Override
        protected void onPostExecute(Bitmap b) {
            if (imageViewReference != null && b != null) {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(b);
                }
            }
        }
    }
}