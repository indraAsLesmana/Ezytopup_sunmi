package com.ezytopup.salesapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ezytopup.salesapp.R;

public class DetailProductActivity extends AppCompatActivity {
    private static final String PRODUCTID = "DetailProductActivity::productid";

    public static void start(Activity caller, String productid) {
        Intent intent = new Intent(caller, DetailProductActivity.class);
        intent.putExtra(PRODUCTID, productid);
        caller.startActivity(intent);
        caller.finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);
    }
}
