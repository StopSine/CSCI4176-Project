package csci4176.toptentoday;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Details activity for shows
 */
public class ShowDetails extends AppCompatActivity implements JSONDownloadTask.OnDownloadCompleted{

    JSONObject json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);

        //grab json from saved bundle and load it into variables
        String backdropPath = "", title = "", overview = "", release = "", votes = "";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            try {
                json = new JSONObject(extras.getString("json"));
                backdropPath = json.getString("backdrop_path");
                title = json.getString("name");
                overview = json.getString("overview");
                release = json.getString("first_air_date");
                votes = json.getDouble("vote_average") + "/10";

                //download additional information (genres/ratings) from omdbapi
                new JSONDownloadTask(this).execute(new URL("http://www.omdbapi.com/?t="+URLEncoder.encode(title, "UTF-8")+"&type=series&tomatoes=true"));
            }
            catch (JSONException | MalformedURLException | UnsupportedEncodingException e){
                e.printStackTrace();
            }
        }

        //setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.movie_detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.movie_detail_collapsing_toolbar);
        collapsingToolbarLayout.setTitle(title);
        collapsingToolbarLayout.setTitleEnabled(true);

        //download and add backdrop image
        ImageView imgView = (ImageView) findViewById(R.id.movie_detail_image);
        String backdrop = "https://image.tmdb.org/t/p/w780" + backdropPath;
        new BitmapWorkerTask().loadBitmap(backdrop, imgView);

        //setup text and hide showtimes button
        TextView overviewTextView = (TextView) findViewById(R.id.movie_overview);
        overviewTextView.setText(overview);
        TextView releaseTextView = (TextView) findViewById(R.id.movie_release);
        releaseTextView.setText(release);
        TextView votesTextView = (TextView) findViewById(R.id.votes);
        votesTextView.setText(votes);
        Button showTimesButton = (Button) findViewById(R.id.showtimesButton);
        showTimesButton.setVisibility(View.GONE);


    }

    //called after omdb download finishes, sets additional ratings and genres
    public void updateList(JSONObject result){
        String imdbVotes = "";
        String tomatoesVotes = "";
        String[] genres = {};
        try {
            imdbVotes = result.getString("imdbRating") + "/10";
            tomatoesVotes = result.getString("tomatoRating") + "/10";
            genres = result.getString("Genre").split(",\\s+");
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        LinearLayout tableLayout = (LinearLayout) findViewById(R.id.genre_table);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        for (int i = 0; i < 3; i++) {
            TextView textView = (TextView) inflater.inflate(R.layout.genre_text_view, null);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f);
            params.setMargins(10, 0, 10, 0);
            textView.setLayoutParams(params);
            textView.setWidth(0);
            if (i < genres.length) {
                textView.setBackgroundResource(R.drawable.genre_bg);
                textView.setText(genres[i]);
            }
            tableLayout.addView(textView);
        }
        TextView imdbVotesTextView = (TextView) findViewById(R.id.imdb_votes);
        imdbVotesTextView.setText(imdbVotes);
        TextView tomatoesVotesTextView = (TextView) findViewById(R.id.tomatoes_votes);
        tomatoesVotesTextView.setText(tomatoesVotes);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail_view, menu);
        return true;
    }

    //setup back button and open in browser
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_open_new:
                String baseUrl = "https://www.themoviedb.org/tv/";
                int id = 0;
                try{
                    id = json.getInt("id");
                }
                catch (JSONException e){
                    e.printStackTrace();
                }
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(baseUrl + id));
                startActivity(browserIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
