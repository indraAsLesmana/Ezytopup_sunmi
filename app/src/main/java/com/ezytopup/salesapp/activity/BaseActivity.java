package com.ezytopup.salesapp.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
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

public abstract class BaseActivity extends AppCompatActivity implements ActivityInterface{

    private ImageView toolbar_centerImage;
    Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        configureToolbar();
    }

    private void configureToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null){
                getSupportActionBar().setDisplayShowTitleEnabled(false);

                toolbar_centerImage = (ImageView) findViewById(R.id.toolbar_centered_logo);
                Glide.with(this)
                        .load(PreferenceUtils.getSinglePrefrence(this,
                                R.string.settings_def_storelogo_key))
                        .crossFade()
                        .into(toolbar_centerImage);
            }
        }
    }



}
