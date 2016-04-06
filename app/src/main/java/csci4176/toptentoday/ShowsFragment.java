package csci4176.toptentoday;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

/**
 * Handles fetching the json for shows and filling in the list
 */
public class ShowsFragment extends ListFragment implements JSONDownloadTask.OnDownloadCompleted {

    CustomArrayAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        refresh();

        //set adapter up with empty placeholder list
        if (adapter == null) {
            adapter = new CustomArrayAdapter(this.getContext(), new ArrayList<ListItem>(Collections.singletonList(new ListItem("No Data Loaded", "", "", "", null))));
        }
        setListAdapter(adapter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    //grab current date and use it make call to fetch the json for shows
    public void refresh(){
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        Date newDate = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.CANADA);
        try {
            new JSONDownloadTask(this).execute(new URL("http://api.themoviedb.org/3/discover/tv?api_key=c0a48133bf57722a3829e6456f01b24f&page=1&air_date.gte="+dateFormat.format(newDate)+"&sort_by=popularity.desc"));
        }
        catch (MalformedURLException e){
            e.printStackTrace();
        }
    }

    //open details
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Intent intent = new Intent(this.getActivity(), ShowDetails.class);
        intent.putExtra("json", adapter.getItem(position).json.toString());
        startActivity(intent);
    }

    //called when API data returns
    public void updateList(JSONObject result){
        ArrayList<ListItem> list = new ArrayList<ListItem>();
        //error list
        if (result == null){
            list.add(new ListItem("Something Went Wrong!", "Check your internet connection, and try to refresh", null, null, null));
        }
        else {
            //fill in values
            String baseImgUrl = "http://image.tmdb.org/t/p/w130";
            String baseUrl = "https://www.themoviedb.org/tv/";
            try {
                JSONArray resultsArray = result.getJSONArray("results");
                for (int i = 0; i < 10; i++) {
                    String title = resultsArray.getJSONObject(i).getString("name");
                    String overview = resultsArray.getJSONObject(i).getString("overview");
                    String imgUrl = baseImgUrl + resultsArray.getJSONObject(i).getString("poster_path");
                    String url = baseUrl + resultsArray.getJSONObject(i).getString("id");
                    list.add(new ListItem(title, overview, imgUrl, url, resultsArray.getJSONObject(i)));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        adapter = new CustomArrayAdapter(this.getContext(), list);
        setListAdapter(adapter);
        adapter.notifyDataSetChanged();
    }
}