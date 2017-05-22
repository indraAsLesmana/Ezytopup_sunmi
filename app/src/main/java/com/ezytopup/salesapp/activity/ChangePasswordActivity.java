package com.ezytopup.salesapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ezytopup.salesapp.Eztytopup;
import com.ezytopup.salesapp.R;
import com.ezytopup.salesapp.api.ChangepasswordResponse;
import com.ezytopup.salesapp.utility.Helper;
import com.ezytopup.salesapp.utility.PreferenceUtils;

import java.net.HttpURLConnection;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends BaseActivity implements View.OnClickListener {

    private EditText mOldPasswordView, mNewPasswordView, mConfirmNewPasswordView;
    private Button mChangePasswordButton, mCancelButton;
    private static final String TAG = "ChangePasswordActivity";
    private String token, newPassword, confirmPassword, oldPassword;
    private ConstraintLayout container_view;

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
        container_view = (ConstraintLayout) findViewById(R.id.container_changepassword);

        mCancelButton.setOnClickListener(this);
        mChangePasswordButton.setOnClickListener(this);

        if (Eztytopup.getSunmiDevice()){
            Helper.setImmersivebyKeyboard(container_view);
        }
    }

    private void setChangepassword(String token, String newPassword, String oldPassword,
                                   String confirmPassword){
        HashMap<String, String> data = new HashMap<>();
        data.put("pass_lama", oldPassword);
        data.put("pass_baru1", newPassword);
        data.put("pass_baru2", confirmPassword);
        data.put("token", token);

        Call<ChangepasswordResponse> changePassword = Eztytopup.getsAPIService()
                .setChangePassword(data);
        changePassword.enqueue(new Callback<ChangepasswordResponse>() {
            @Override
            public void onResponse(Call<ChangepasswordResponse> call,
                                   Response<ChangepasswordResponse> response) {
                if (response.isSuccessful() &&
                        response.body().status.getCode()
                                .equals(String.valueOf(HttpURLConnection.HTTP_OK))){
                    Toast.makeText(ChangePasswordActivity.this, response.body().status.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(ChangePasswordActivity.this, response.body().status.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ChangepasswordResponse> call, Throwable t) {

            }
        });
    }
    private void changePassword(){
        token = PreferenceUtils.getSinglePrefrenceString(ChangePasswordActivity.this,
                R.string.settings_def_storeaccess_token_key);
        oldPassword = mOldPasswordView.getText().toString();
        newPassword = mNewPasswordView.getText().toString();
        confirmPassword = mConfirmNewPasswordView.getText().toString();

        Helper.log(TAG, "change passwordToken " + token, null);

        if(oldPassword.isEmpty()){
            Toast.makeText(this, R.string.please_fill_password, Toast.LENGTH_SHORT).show();
        } else if (newPassword.length() < 8 || confirmPassword.length() < 8 ||
                oldPassword.length() < 8){
            Toast.makeText(this, R.string.password_toshort, Toast.LENGTH_SHORT).show();
        }else if (!newPassword.equals(confirmPassword)){
            Toast.makeText(this, R.string.password_notmatch, Toast.LENGTH_SHORT).show();
        }else{
            setChangepassword(token, newPassword, mOldPasswordView.getText().toString(), token);
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
        switch (v.getId()){
            case R.id.change_password_button:
                changePassword();
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
