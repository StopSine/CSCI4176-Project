package csci4176.toptentoday;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private DrawerLayout mDrawer;
    private NavigationView nView;
    private ActionBarDrawerToggle mDrawerToggle;

    //setup toolbar, tabs, and pager
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        nView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerToggle = new ActionBarDrawerToggle(
                this,  mDrawer, mToolbar,
                R.string.drawer_open, R.string.drawer_close
        );

        mDrawer.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Articles"));
        tabLayout.addTab(tabLayout.newTab().setText("Movies"));
        tabLayout.addTab(tabLayout.newTab().setText("Shows"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        viewPager.setOffscreenPageLimit(2);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });

        nView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        onNavItemSelected(menuItem);
                        return true;
                    }
                });

    }

    public void onNavItemSelected(MenuItem menuItem) {

        switch(menuItem.getItemId()) {
            case R.id.nav_first_fragment:
                break;
            case R.id.nav_second_fragment:
                break;
            default:
        }
        mDrawer.closeDrawers();
    }

    public void showLicenses(){
        //TODO:
        //tmp paste of licenses to add
        //NYT API
        //https://www.themoviedb.org/documentation/api
    }

    public void showSettings(){
        //nothing yet
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }
}
