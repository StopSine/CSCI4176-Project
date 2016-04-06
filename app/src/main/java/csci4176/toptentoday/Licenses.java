package csci4176.toptentoday;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * Simple list to show api and licenses of libraries used
 */
public class Licenses extends AppCompatActivity {

    ListView listView ;

    //create and populate list
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_licenses);
        listView = (ListView) findViewById(R.id.licenses_list);
        String[] values = new String[] { "jsoup MIT license - http://jsoup.org/license",
                "New York Times: The Most Popular API - http://developer.nytimes.com/Api_terms_of_use",
                "The Movie Database API - https://www.themoviedb.org/documentation/api/terms-of-use",
                "The Open Movie Database API - Licenced under Creative Commons - http://creativecommons.org/licenses/by/4.0/"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);
        listView.setAdapter(adapter);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.licenses));
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
}