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

public class RegisterFragment_Adapter extends FragmentPagerAdapter {
    private static final int PAGE_COUNT = 4;
    private String tabTitles[] = new String[]{"Home", "Favorite", "History", "Search"};
    private Context mContext;

    private static final int SIGN = 0;
    private static final int REGISTER = 1;
    private static final int A = 2;
    private static final int B = 3;

    public RegisterFragment_Adapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case SIGN:
                return new HomeFragment();
            case REGISTER:
                return new FavoriteFragment();
            case A:
                return new FavoriteFragment();
            case B:
                return new FavoriteFragment();
            default:
                return new FavoriteFragment();
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
