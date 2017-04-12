package com.ezytopup.salesapp.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.ezytopup.salesapp.fragment.BuyproductFragment;
import com.ezytopup.salesapp.fragment.DetailproductFragment;


/**
 * Created by indraaguslesmana on 3/9/17.
 */

public class BuyFragment_Adapter extends FragmentPagerAdapter {
    private static final int PAGE_COUNT = 2;
    private Context mContext;

    private static final int BUY = 0;
    private static final int DETAIL = 1;

    public BuyFragment_Adapter(FragmentManager fm, Context context) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case BUY:
                return new BuyproductFragment();
            case DETAIL:
                return new DetailproductFragment();
            default:
                return new BuyproductFragment();
        }
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

}
