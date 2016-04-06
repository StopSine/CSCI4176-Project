package csci4176.toptentoday;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/*
 * Main app screen with viewpager and Articles/Movies/Shows fragments
 */
public class MainActivity extends AppCompatActivity {

    private ViewPager mViewPager;
    private DrawerLayout mDrawer;
    PagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setup toolbar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        //setup drawer and hamburger icon
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle mDrawerToggle = new ActionBarDrawerToggle(
                this,  mDrawer, mToolbar,
                R.string.drawer_open, R.string.drawer_close
        );
        mDrawer.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        //create viewpager tabs
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Articles"));
        tabLayout.addTab(tabLayout.newTab().setText("Movies"));
        tabLayout.addTab(tabLayout.newTab().setText("Shows"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        //add viewpager, set paging limit and attach custom adapter
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        mViewPager.setOffscreenPageLimit(2);
        adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        mViewPager.setAdapter(adapter);
        //attach tabs to viewpager
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        //setup drawer menu onItemSelected
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
            new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    menuItem.setChecked(true);
                    onNavItemSelected(menuItem);
                    return true;
                }
            }
        );
    }

    //inflate menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main_view, menu);
        return true;
    }

    //handle refresh action
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                int currItem = mViewPager.getCurrentItem();
                switch(currItem){
                    case 0:
                        adapter.getArticles().refresh();
                        break;
                    case 1:
                        adapter.getMovies().refresh();
                        break;
                    case 2:
                        adapter.getShows().refresh();
                        break;
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    //handle filtering from selecting drawer items
    public void onNavItemSelected(MenuItem menuItem) {
        switch(menuItem.getGroupId()) {
            case R.id.pref_filter_group:
                String stringToStore = menuItem.getTitle().toString().toLowerCase();
                SharedPreferences prefs = this.getSharedPreferences("prefs", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = prefs.edit();
                Set<String> filterSet = prefs.getStringSet("filter-list", new HashSet<String>(Arrays.asList("all-sections")));
                filterSet.add(stringToStore);
                if (menuItem.getItemId() == R.id.pref_filter_all){
                    filterSet = new HashSet<String>(Arrays.asList("all-sections"));
                }
                edit.putStringSet("filter-list", filterSet);
                edit.commit();
                adapter.getArticles().refresh();
                return;
            default:
        }
        switch(menuItem.getItemId()){
            case R.id.nav_licenses:
                Intent intent = new Intent(this, Licenses.class);
                startActivity(intent);
                break;
        }
        mDrawer.closeDrawers();
    }
}
