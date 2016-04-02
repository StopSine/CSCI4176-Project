package csci4176.toptentoday;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {
    int mNumOfTabs;

    ArticlesFragment articles;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    public ArticlesFragment getArticles(){
        return articles;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                articles = new ArticlesFragment();
                return articles;
            case 1:
                MoviesFragment movies = new MoviesFragment();
                return movies;
            case 2:
                ShowsFragment shows = new ShowsFragment();
                return shows;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
