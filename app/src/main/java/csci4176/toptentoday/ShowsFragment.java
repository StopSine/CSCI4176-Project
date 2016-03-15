package csci4176.toptentoday;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ArrayAdapter;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import info.movito.themoviedbapi.TmdbApi;
import info.movito.themoviedbapi.model.MovieDb;
import info.movito.themoviedbapi.model.tv.TvSeries;

public class ShowsFragment extends ListFragment {

    private static final String TAG = "ShowsFrag";
    CustomArrayAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //TODO: Fix list refreshing when page switches and stop using the api wrapper (its lazy)
        //fetch api data
        new ShowsLookupTask().execute();

        //set adapter up with empty placeholder list
        if (adapter == null) {
            adapter = new CustomArrayAdapter(this.getContext(), new ArrayList<ListItem>(Arrays.asList(new ListItem("No Data Loaded", "", "", ""))));
        }
        setListAdapter(adapter);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Uri uri = Uri.parse(adapter.getItem((int)id).url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void updateList(List<TvSeries> result){
        ArrayList<ListItem> list = new ArrayList<ListItem>();
        String baseImgUrl = "http://image.tmdb.org/t/p/w130";
        String baseUrl = "https://www.themoviedb.org/tv/";
        for (int i = 0; i < 10; i++){
            list.add(new ListItem(result.get(i).getName(), result.get(i).getOverview(), baseImgUrl + result.get(i).getPosterPath(), baseUrl + result.get(i).getId()));
        }
        adapter = new CustomArrayAdapter(this.getContext(), list);
        setListAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    class ShowsLookupTask extends AsyncTask<Void, Void, List<TvSeries>> {

        protected List<TvSeries> doInBackground(Void... nothing) {
            //TODO: set parameters properly
            return new TmdbApi("c0a48133bf57722a3829e6456f01b24f").getTvSeries().getPopular("en",1).getResults();
        }

        protected void onPostExecute(List<TvSeries> result) {
            updateList(result);
        }
    }
}