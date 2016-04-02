package csci4176.toptentoday;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

public class ArticleDetails extends AppCompatActivity {

    JSONObject json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String title = "";
        String url = "";
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            try {
                json = new JSONObject(extras.getString("json"));
                title = json.getString("title");
                url = json.getString("url");

            }
            catch (JSONException e){

            }
        }
        new scrapHtmlTask().execute(url);
        setContentView(R.layout.article_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);
    }

    public void updateText(String s){
        TextView articleText = (TextView) findViewById(R.id.article_text);
        articleText.setText(s);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_open_new:
                String url = "";
                try{
                    url = json.getString("url");
                }
                catch (JSONException e){

                }
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detail_view, menu);
        return true;
    }

    class scrapHtmlTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {
            System.out.println(url[0]);
            String fullStory = "\t";
            try {
                Document doc = Jsoup.connect(url[0]).get();
                Elements elements = doc.body().select("p.p-block");
                for (Element e : elements) {
                    fullStory += e.text() + "\n\n\t";
                }
            }
            catch (IOException e){
                e.printStackTrace();
            }
            return fullStory;
        }

        @Override
        protected void onPostExecute(String s) {
            updateText(s);
        }
    }

}
