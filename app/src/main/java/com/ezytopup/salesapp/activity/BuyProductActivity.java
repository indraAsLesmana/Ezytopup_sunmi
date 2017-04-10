package com.ezytopup.salesapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.ezytopup.salesapp.R;

public class BuyProductActivity extends BaseActivity {

    private static final String PRODUCT_ID = "BuyProductActivity::productid";

    public static void start(Activity caller, String productId) {
        Intent intent = new Intent(caller, BuyProductActivity.class);
        intent.putExtra(PRODUCT_ID, productId);
        caller.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyproduct);
    }
    private void getTransaction(){

    }

    @Override
    public void initView() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_buyproduct;
    }
}
