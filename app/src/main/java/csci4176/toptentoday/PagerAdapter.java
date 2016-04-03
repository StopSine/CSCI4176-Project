package csci4176.toptentoday;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentPagerAdapter {
    int mNumOfTabs;

    ArticlesFragment articles;
    MoviesFragment movies;
    ShowsFragment shows;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    public ArticlesFragment getArticles(){
        return articles;
    }

    public MoviesFragment getMovies(){
        return movies;
    }

    public ShowsFragment getShows(){
        return shows;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if (articles == null){
                    articles = new ArticlesFragment();
                }
                return articles;
            case 1:
                if (movies == null){
                    movies = new MoviesFragment();
                }
                return movies;
            case 2:
                if (shows == null){
                    shows = new ShowsFragment();
                }
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
