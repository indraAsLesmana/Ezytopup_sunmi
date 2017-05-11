package com.ezytopup.salesapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ezytopup.salesapp.R;

public class ForgotPasswordActivity extends BaseActivity implements View.OnClickListener {

    private EditText mEmailView, mPhoneView;
    private Button mGetYourPasswordButton, mBackButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mEmailView = (EditText) findViewById(R.id.emailAddress);
        mPhoneView = (EditText) findViewById(R.id.phone);

        mGetYourPasswordButton = (Button) findViewById(R.id.btnGetyourpassword);
        mBackButton = (Button) findViewById(R.id.btnBack);

        mGetYourPasswordButton.setOnClickListener(this);
        mBackButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnGetyourpassword:
                break;
            case R.id.btnBack:
                break;
        }
    }

    @Override
    public void initView() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_forgot_password;
    }
}
