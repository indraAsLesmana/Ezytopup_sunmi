package com.ezytopup.salesapp.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.text.Spannable;
import android.text.SpannableString;

import com.ezytopup.salesapp.fragment.FavoriteFragment;
import com.ezytopup.salesapp.fragment.HomeFragment;


/**
 * Created by indraaguslesmana on 3/9/17.
 */

public class MainFragment_Adapter extends FragmentPagerAdapter {
    private static final int PAGE_COUNT = 4;
    private String tabTitles[] = new String[]{"Home", "Popular", "History", "Search"}; //TODO temporary hardcode
    private Context mContext;

    private static final int HOME = 0;
    private static final int POPULAR = 1;
    private static final int HISTORY = 2;
    private static final int SEARCH = 3;

    public MainFragment_Adapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case HOME:
                return new HomeFragment();
            case POPULAR:
                return new FavoriteFragment();
            case HISTORY:
                return new HomeFragment();
            case SEARCH:
                return new HomeFragment();
            default:
                return new HomeFragment();
        }
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        SpannableString sb = new SpannableString(tabTitles[position]);
        sb.setSpan(null, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }
}
