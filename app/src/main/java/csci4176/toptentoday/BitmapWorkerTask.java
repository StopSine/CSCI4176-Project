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
 * Downloads a bitmap to the cache and can fetch it
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

    //set the image view to the downloaded bitmap
    @Override
    protected void onPostExecute(Bitmap b) {
        if (imageViewReference != null && b != null) {
            final ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                imageView.setImageBitmap(b);
            }
        }
    }

    //loads the bitmap from cache to the image view or executes the bitmap worker task to get it
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