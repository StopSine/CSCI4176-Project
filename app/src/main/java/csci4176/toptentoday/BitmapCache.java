package csci4176.toptentoday;

import android.graphics.Bitmap;
import android.util.LruCache;

/**
 * Cache used to store images
 * Inspiration largely from: http://developer.android.com/training/displaying-bitmaps/cache-bitmap.html
 */
public class BitmapCache
{
    private static BitmapCache _instance;

    //allows for class to be a singleton
    public synchronized static BitmapCache getInstance()
    {
        if (_instance == null)
        {
            _instance = new BitmapCache();
        }
        return _instance;
    }

    private static LruCache<String, Bitmap> mMemoryCache;

    //initializes the bitmap cache and sets the size
    private BitmapCache() {

        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getByteCount() / 1024;
            }
        };
    }

    //takes in key, value pair to store in cache
    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    //returns bitmap from cache
    public static Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }
}