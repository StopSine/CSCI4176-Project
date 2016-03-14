package csci4176.toptentoday;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.tv.TvSeries;

public class ArticlesFragment extends ListFragment {

    private static final String TAG = "ArticlesFrag";
    CustomArrayAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //TODO: Fix list refreshing when page switches
        //fetch api data
        new ArticlesLookupTask().execute();

        //set adapter up with empty placeholder list
        if (adapter == null) {
            adapter = new CustomArrayAdapter(this.getContext(), new ArrayList<ListItem>(Arrays.asList(new ListItem("No Data Loaded", ""))));
        }
        setListAdapter(adapter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Log.d(TAG, "id=" + id);
    }

    //called when API data returns
    public void updateList(JSONObject result){
        ArrayList<ListItem> list = new ArrayList<ListItem>();
        try {
            JSONArray resultsArray = result.getJSONArray("results");
            for (int i = 0; i < resultsArray.length(); i++){
                list.add(new ListItem(resultsArray.getJSONObject(i).getString("title"), resultsArray.getJSONObject(i).getString("abstract")));
            }
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        adapter = new CustomArrayAdapter(this.getContext(), list);
        setListAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    class ArticlesLookupTask extends AsyncTask<Void, Void, JSONObject> {

        protected JSONObject doInBackground(Void... nothing) {
            URL url = null;
            HttpURLConnection conn = null;
            JSONObject js = new JSONObject();
            try {
                url = new URL("http://api.nytimes.com/svc/mostpopular/v2/mostviewed/all-sections/1.json?api-key=16802a58ff1e9b1758caf24c9c93f90c:7:74374990");
                conn = (HttpURLConnection) url.openConnection();
                if (conn.getResponseCode() != 200) {
                    throw new IOException(conn.getResponseMessage());
                }
                // Buffer the result into a string
                BufferedReader rd = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = rd.readLine()) != null) {
                    sb.append(line);
                }
                rd.close();

                conn.disconnect();
                js = new JSONObject(sb.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            catch (JSONException e) {
            e.printStackTrace();
            }
            return js;
        }

        protected void onPostExecute(JSONObject result) {
            updateList(result);
        }
    }
}