package com.ezytopup.salesapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ezytopup.salesapp.R;

public class ChangePasswordActivity extends BaseActivity implements View.OnClickListener {

    private EditText mOldPasswordView, mNewPasswordView, mConfirmNewPasswordView;
    private Button mChangePasswordButton, mCancelButton;

    public static void start(Activity caller) {
        Intent intent = new Intent(caller, ChangePasswordActivity.class);
        caller.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mOldPasswordView = (EditText) findViewById(R.id.oldPassword);
        mNewPasswordView = (EditText) findViewById(R.id.newPassword);
        mConfirmNewPasswordView = (EditText) findViewById(R.id.confirmNewpassword);

        mChangePasswordButton = (Button) findViewById(R.id.change_password_button);
        mCancelButton = (Button) findViewById(R.id.btnCancel);

        mChangePasswordButton.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);
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
        switch (v.getId()){
            case R.id.change_password_button:
                break;
            case R.id.btnCancel:
                break;
        }
    }

    @Override
    public void initView() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_change_password;
    }

}
