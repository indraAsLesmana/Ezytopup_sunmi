package com.ezytopup.salesapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.ezytopup.salesapp.R;

public class ChangePasswordActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mOldPasswordView, mNewPasswordView, mConfirmNewPasswordView;
    private Button mChangePasswordButton, mCancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        mOldPasswordView = (EditText) findViewById(R.id.oldPassword);
        mNewPasswordView = (EditText) findViewById(R.id.newPassword);
        mConfirmNewPasswordView = (EditText) findViewById(R.id.confirmNewpassword);

        mChangePasswordButton = (Button) findViewById(R.id.change_password_button);
        mCancelButton = (Button) findViewById(R.id.btnCancel);

        mChangePasswordButton.setOnClickListener(this);
        mCancelButton.setOnClickListener(this);
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
}
