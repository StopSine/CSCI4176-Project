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
import android.os.AsyncTask;

import java.util.Arrays;
import java.util.List;
import info.movito.themoviedbapi.*;
import info.movito.themoviedbapi.model.MovieDb;

import java.util.ArrayList;

public class MoviesFragment extends ListFragment {

    private static final String TAG = "MoviesFrag";
    CustomArrayAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //fetch api data
        new MovieLookupTask().execute();

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

    //called when API data returns
    public void updateList(List<MovieDb> result){
        ArrayList<ListItem> list = new ArrayList<ListItem>();
        String baseImgUrl = "http://image.tmdb.org/t/p/w130";
        String baseUrl = "https://www.themoviedb.org/movie/";
        for (int i = 0; i < 10; i++){
            list.add(new ListItem(result.get(i).getTitle(), result.get(i).getOverview(), baseImgUrl + result.get(i).getPosterPath(), baseUrl + result.get(i).getId()));
        }
        adapter = new CustomArrayAdapter(this.getContext(), list);
        setListAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    class MovieLookupTask extends AsyncTask<Void, Void, List<MovieDb>> {

        protected List<MovieDb> doInBackground(Void... nothing) {
            //TODO: set parameters properly
            return new TmdbApi("c0a48133bf57722a3829e6456f01b24f").getDiscover().getDiscover(
                    1,
                    "en",
                    "popularity.desc",
                    true,
                    2016,
                    2016,
                    0,
                    0,
                    "",
                    "2016-02-01",
                    "2016-03-01",
                    "",
                    "",
                    ""
            ).getResults();
        }

        protected void onPostExecute(List<MovieDb> result) {
            updateList(result);
        }
    }
}