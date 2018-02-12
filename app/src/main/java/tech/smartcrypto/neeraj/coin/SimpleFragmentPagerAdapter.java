package tech.smartcrypto.neeraj.coin;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import tech.smartcrypto.neeraj.coin.fragments.AlertsFragment;
import tech.smartcrypto.neeraj.coin.fragments.NewsFragment;
import tech.smartcrypto.neeraj.coin.fragments.WatchlistFragment;

/**
 * Created by neeraj on 28/1/18.
 */

public class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

    private Context mContext;

    public SimpleFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    // This determines the fragment for each tab
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new WatchlistFragment();
        } else {//if (position == 1){
            return new AlertsFragment();
        }
//        else {
//            return new NewsFragment();
//        }
    }

    // This determines the number of tabs
    @Override
    public int getCount() {
        return 2;//3;
    }

    // This determines the title for each tab
    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        switch (position) {
            case 0:
                return mContext.getString(R.string.fragment_watchlist);
            case 1:
                return mContext.getString(R.string.fragment_alerts);
            //case 2:
             //   return mContext.getString(R.string.fragment_news);
            default:
                return null;
        }
    }

}
