package csci4176.toptentoday;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Blair on 3/13/2016.
 */
public class CustomArrayAdapter extends ArrayAdapter<ListItem> {
    public CustomArrayAdapter(Context context, ArrayList<ListItem> listItems) {
        super(context, 0, listItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ListItem item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        TextView title = (TextView) convertView.findViewById(R.id.listTitle);
        TextView subTitle = (TextView) convertView.findViewById(R.id.listSubTitle);
        title.setText(item.title);
        subTitle.setText(item.subTitle);

        return convertView;
    }
}