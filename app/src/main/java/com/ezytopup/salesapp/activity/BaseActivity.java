package com.ezytopup.salesapp.activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.ezytopup.salesapp.R;
import com.ezytopup.salesapp.base.ActivityInterface;
import com.ezytopup.salesapp.utility.PreferenceUtils;

/**
 * Created by indraaguslesmana on 3/31/17.
 */

public abstract class BaseActivity extends AppCompatActivity implements ActivityInterface {

    ImageView toolbar_centerImage;
    Toolbar toolbar;
    ActionBar actionBar;
    SharedPreferences sharedPreferences;
    CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinatorlayout);
        configureToolbar();
        initSharedPreference();
    }

    private void configureToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            actionBar = getSupportActionBar();
            if (actionBar != null){
                actionBar.setDisplayShowTitleEnabled(false);

                toolbar_centerImage = (ImageView) findViewById(R.id.toolbar_centered_logo);
                Glide.with(this)
                        .load(PreferenceUtils.getSinglePrefrenceString(this,
                                R.string.settings_def_storelogo_key))
                        .crossFade()
                        .into(toolbar_centerImage);
            }
        }


    }

    private void initSharedPreference() {
        sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(this);

    }

}
