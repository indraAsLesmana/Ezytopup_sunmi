package com.ezytopup.salesapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.ezytopup.salesapp.R;

public class SignUpActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar.setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public void initView() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_sign_up;
    }
}
