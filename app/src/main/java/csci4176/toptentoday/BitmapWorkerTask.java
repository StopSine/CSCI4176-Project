package csci4176.toptentoday;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.LruCache;
import android.widget.ImageView;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;

/**
 * Created by Blair on 4/3/2016.
 */

public class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap>
{
    private WeakReference<ImageView> imageViewReference;

    @Override
    protected Bitmap doInBackground(String... url) {
        Bitmap b = null;
        try {
            URL imgUrl = new URL(url[0]);
            b = BitmapFactory.decodeStream(imgUrl.openConnection().getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
        BitmapCache.getInstance().addBitmapToMemoryCache(url[0], b);
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

    public void loadBitmap(String url, ImageView image) {
        imageViewReference = new WeakReference<ImageView>(image);
        final Bitmap bitmap = BitmapCache.getInstance().getBitmapFromMemCache(url);
        if (bitmap != null) {
            image.setImageBitmap(bitmap);
        } else {
            execute(url);
        }
    }
}