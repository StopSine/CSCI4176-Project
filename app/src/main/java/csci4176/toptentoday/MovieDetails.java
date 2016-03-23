package csci4176.toptentoday;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
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
            }
            catch (JSONException e){

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
