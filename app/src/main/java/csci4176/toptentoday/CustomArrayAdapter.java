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
 * Array adapter to store multiple values, since both text and subtext are needed
 */
public class CustomArrayAdapter extends ArrayAdapter<ListItem> {


    public CustomArrayAdapter(Context context, ArrayList<ListItem> listItems) {
        super(context, 0, listItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        //setup list item values and images
        ListItem item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        TextView title = (TextView) convertView.findViewById(R.id.list_title);
        TextView subTitle = (TextView) convertView.findViewById(R.id.list_subtitle);
        ImageView image = (ImageView) convertView.findViewById(R.id.list_image);;

        title.setText(item.title);
        subTitle.setText(item.subTitle);

        if (item.imgUrl != null) {
            if (!item.imgUrl.isEmpty()) {
               new BitmapWorkerTask().loadBitmap(item.imgUrl, image);
            }
        }
        else {
            image.setVisibility(View.GONE);
        }

        return convertView;
    }
}