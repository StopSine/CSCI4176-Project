package csci4176.toptentoday;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;


public class ArticlesFragment extends ListFragment implements JSONDownloadTask.OnDownloadCompleted {

    interface OnDownloadCompleted{
        void updateList(JSONObject result);
    }

    private static final String TAG = "ArticlesFrag";
    CustomArrayAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //fetch api data
        try {
            new JSONDownloadTask(this).execute(new URL("http://api.nytimes.com/svc/mostpopular/v2/mostviewed/all-sections/1.json?api-key=16802a58ff1e9b1758caf24c9c93f90c:7:74374990"));
        }
        catch (MalformedURLException e){

        }

        //set adapter up with empty placeholder list
        if (adapter == null) {
            adapter = new CustomArrayAdapter(this.getContext(), new ArrayList<ListItem>(Arrays.asList(new ListItem("No Data Loaded", "", "", "" , null))));
        }
        setListAdapter(adapter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(this.getActivity(), ArticleDetails.class);
        intent.putExtra("json", adapter.getItem(position).json.toString());
        startActivity(intent);
    }

    //called when API data returns
    public void updateList(JSONObject result){
        ArrayList<ListItem> list = new ArrayList<ListItem>();
        try {
            JSONArray resultsArray = result.getJSONArray("results");
            for (int i = 0; i < 10; i++){
                String imgUrl = null;
                if(!resultsArray.getJSONObject(i).getString("media").isEmpty()){
                    JSONArray mediaMetaDataArray = resultsArray.getJSONObject(i).getJSONArray("media").getJSONObject(0).getJSONArray("media-metadata");
                    for (int j = 0; j < mediaMetaDataArray.length(); j++){
                        if (mediaMetaDataArray.getJSONObject(j).getString("format").equals("Large Thumbnail")){
                            imgUrl = mediaMetaDataArray.getJSONObject(j).getString("url");
                            break;
                        }
                    }
                }
                String title = resultsArray.getJSONObject(i).getString("title");
                String overview =  resultsArray.getJSONObject(i).getString("abstract");
                String url =  resultsArray.getJSONObject(i).getString("url");
                list.add(new ListItem(title, overview, imgUrl, url, resultsArray.getJSONObject(i)));
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        adapter = new CustomArrayAdapter(this.getContext(), list);
        setListAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}