package csci4176.toptentoday;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class MovieDetails extends AppCompatActivity implements JSONDownloadTask.OnDownloadCompleted{

    JSONObject json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String backdropPath = "";
        String title = "";
        String overview = "";
        String release = "";
        String votes = "";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            try {
                json = new JSONObject(extras.getString("json"));
                backdropPath = json.getString("backdrop_path");
                title = json.getString("title");
                overview = json.getString("overview");
                release = json.getString("release_date");
                votes = json.getDouble("vote_average") + "/10";
                new JSONDownloadTask(this).execute(new URL("http://www.omdbapi.com/?t="+ URLEncoder.encode(title, "UTF-8")+"&type=movie&tomatoes=true"));
            }
            catch (JSONException | MalformedURLException | UnsupportedEncodingException e){
                e.printStackTrace();
            }
        }

        setContentView(R.layout.movie_detail_view);
        Toolbar toolbar = (Toolbar) findViewById(R.id.movie_detail_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.movie_detail_collapsing_toolbar);
        collapsingToolbarLayout.setTitle(title);
        collapsingToolbarLayout.setTitleEnabled(true);

        ImageView imgView = (ImageView) findViewById(R.id.movie_detail_image);
        String backdrop = "https://image.tmdb.org/t/p/w780" + backdropPath;
        new BitmapWorkerTask().loadBitmap(backdrop, imgView);

        TextView overviewTextView = (TextView) findViewById(R.id.movie_overview);
        overviewTextView.setText(overview);
        TextView releaseTextView = (TextView) findViewById(R.id.movie_release);
        releaseTextView.setText(release);
        TextView votesTextView = (TextView) findViewById(R.id.votes);
        votesTextView.setText(votes);

    }

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
            TextView textView = (TextView) inflater.inflate(R.layout.genre_text, null);
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

    public void onClick(View v) {
        boolean PermissionFineLocation = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED;
        boolean PermissionCourseLocation = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED;
        if (PermissionCourseLocation && PermissionFineLocation) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},0);
        }
        else {
            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            String baseUrl = "http://www.google.ca/movies?near=" + loc.getLatitude() + "," + loc.getLongitude();
            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(baseUrl));
            startActivity(browserIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_open_new:
                String baseUrl = "https://www.themoviedb.org/movie/";
                int id = 0;
                try{
                    id = json.getInt("id");
                }
                catch (JSONException e){

                }
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(baseUrl+id));
                startActivity(browserIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
