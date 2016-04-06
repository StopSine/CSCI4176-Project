package csci4176.toptentoday;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
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

/**
 * Details activity for articles
 */
public class ArticleDetails extends AppCompatActivity {

    JSONObject json;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_details);

        //grab json from saved bundle and load it into variables
        String title = "", url = "";
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

        //start task to get article content
        new scrapHtmlTask().execute(url);

        //setup toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(title);
    }

    //called after scrapHtmlTask, sets the text view to the scrapped text
    public void updateText(String s){
        TextView articleText = (TextView) findViewById(R.id.article_text);
        articleText.setText(s);
    }

    //setup back button and open in browser
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
                    e.printStackTrace();
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

    //uses the jSoup library to grab the content from the articles source
    class scrapHtmlTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {
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
