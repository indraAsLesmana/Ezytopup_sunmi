package com.ezytopup.salesapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;

import com.ezytopup.salesapp.R;

/**
 * Created by indraaguslesmana on 5/17/17.
 */

public class BuyResellerActivity extends BaseActivity implements View.OnClickListener{

    private static final String PRODUCT_ID = "BuyResellerActivity::productid";
    private static final String PRODUCT_NAME = "BuyResellerActivity::productname";
    private static final String PRODUCT_IMAGE = "BuyResellerActivity::productimage";
    private static final String PRODUCT_BG = "BuyResellerActivity::productbackground";
    private static final String PRODUCT_PRICE = "BuyResellerActivity::productprice";

    public static void start(Activity caller, String id, String name, String image, String bg,
                             String price) {
        Intent intent = new Intent(caller, BuyResellerActivity.class);
        intent.putExtra(PRODUCT_ID, id);
        intent.putExtra(PRODUCT_NAME, name);
        intent.putExtra(PRODUCT_IMAGE, image);
        intent.putExtra(PRODUCT_BG, bg);
        intent.putExtra(PRODUCT_PRICE, price);
        caller.startActivity(intent);
    }
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (getIntent().getStringExtra(BuyResellerActivity.PRODUCT_ID) == null||
                getIntent().getStringExtra(BuyResellerActivity.PRODUCT_NAME) == null||
                getIntent().getStringExtra(BuyResellerActivity.PRODUCT_IMAGE) == null||
                getIntent().getStringExtra(BuyResellerActivity.PRODUCT_BG) == null||
                getIntent().getStringExtra(BuyResellerActivity.PRODUCT_PRICE) == null){

            finish();
            return;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        
    }

    @Override
    public void initView() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_buyreseller;
    }
}
