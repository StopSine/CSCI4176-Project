package csci4176.toptentoday;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URL;

public class MovieDetails extends AppCompatActivity {

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
            } catch (JSONException e) {

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
        new BitmapWorkerTask(imgView).execute(backdrop);

        TextView overviewTextView = (TextView) findViewById(R.id.movie_overview);
        overviewTextView.setText(overview);
        TextView releaseTextView = (TextView) findViewById(R.id.movie_release);
        releaseTextView.setText(release);
        TextView votesTextView = (TextView) findViewById(R.id.movie_votes);
        votesTextView.setText(votes);

    }

    class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
        private final WeakReference<ImageView> imageViewReference;

        public BitmapWorkerTask(ImageView image) {
            imageViewReference = new WeakReference<ImageView>(image);
        }

        @Override
        protected Bitmap doInBackground(String... url) {
            Bitmap b = null;
            try {
                URL imgUrl = new URL(url[0]);
                b = BitmapFactory.decodeStream(imgUrl.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return b;
        }

        @Override
        protected void onPostExecute(Bitmap b) {
            if (imageViewReference != null && b != null) {
                final ImageView imageView = imageViewReference.get();
                if (imageView != null) {
                    imageView.setImageBitmap(b);
                }
            }
        }
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
