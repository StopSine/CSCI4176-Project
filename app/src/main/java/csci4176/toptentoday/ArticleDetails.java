package csci4176.toptentoday;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        JSONObject json;
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
        }
        return super.onOptionsItemSelected(item);
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
