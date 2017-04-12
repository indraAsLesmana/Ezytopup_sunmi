package com.ezytopup.salesapp.adapter;

import android.content.Context;
import android.os.Bundle;
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
    private String productId;
    private static final int BUY = 0;
    private static final int DETAIL = 1;

    public BuyFragment_Adapter(FragmentManager fm, Context context, String id) {
        super(fm);
        mContext = context;
        productId = id;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case BUY:
                BuyproductFragment mBuyProductFragment = new BuyproductFragment();
                Bundle bundle = new Bundle();
                bundle.putString("a", productId);
                mBuyProductFragment.setArguments(bundle);
                return mBuyProductFragment;
            case DETAIL:
                DetailproductFragment mDetailProductfragment = new DetailproductFragment();
                Bundle bundle1 = new Bundle();
                bundle1.putString("b", productId);
                mDetailProductfragment.setArguments(bundle1);
                return mDetailProductfragment;
            default:
                return new BuyproductFragment();
        }
    }

    @Override
    public int getCount() {
        return PAGE_COUNT;
    }

}
