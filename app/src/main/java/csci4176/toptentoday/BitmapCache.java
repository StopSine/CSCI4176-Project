package csci4176.toptentoday;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Created by Blair on 4/3/2016.
 */
public class BitmapCache
{
    private static BitmapCache _instance;

    public synchronized static BitmapCache getInstance()
    {
        if (_instance == null)
        {
            _instance = new BitmapCache();
        }
        return _instance;
    }

    private static LruCache<String, Bitmap> mMemoryCache;

    private BitmapCache() {

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    public static Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }
}