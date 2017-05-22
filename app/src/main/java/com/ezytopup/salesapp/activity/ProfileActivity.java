package com.ezytopup.salesapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.ezytopup.salesapp.Eztytopup;
import com.ezytopup.salesapp.R;
import com.ezytopup.salesapp.api.WalletbalanceResponse;
import com.ezytopup.salesapp.utility.Constant;
import com.ezytopup.salesapp.utility.Helper;
import com.ezytopup.salesapp.utility.PreferenceUtils;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends BaseActivity {

    private static final String TAG = "ProfileActivity";
    private EditText mSaldo, mName, mPhone, mEmail;
    private ImageView mImageprofile;
    private ConstraintLayout container_view;

    public static void start(Activity caller) {
        Intent intent = new Intent(caller, ProfileActivity.class);
        caller.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mSaldo = (EditText) findViewById(R.id.ed_profile_saldo);
        mName = (EditText) findViewById(R.id.ed_profile_name);
        mPhone = (EditText) findViewById(R.id.ed_profile_phone);
        mEmail = (EditText) findViewById(R.id.ed_profile_email);
        mImageprofile = (ImageView) findViewById(R.id.im_profile_image);
        container_view = (ConstraintLayout) findViewById(R.id.container_profileactivity);

        String userName = PreferenceUtils.getSinglePrefrenceString
                (ProfileActivity.this, R.string.settings_def_storefirst_name_key);
        String userPhone = PreferenceUtils.getSinglePrefrenceString
                (ProfileActivity.this, R.string.settings_def_storephone_number_key);
        String userMail = PreferenceUtils.getSinglePrefrenceString
                (ProfileActivity.this, R.string.settings_def_storeemail_key);
        String imageUrl = PreferenceUtils.getSinglePrefrenceString
                (ProfileActivity.this, R.string.settings_def_storeimage_user_key);

        mName.setText(userName);
        mPhone.setText(userPhone);
        mEmail.setText(userMail);

        if (imageUrl != null){
            Glide.with(ProfileActivity.this)
                    .load(imageUrl).centerCrop()
                    .error(R.drawable.ic_error_loadimage)
                    .into(mImageprofile);
        }

        String checkEmail = PreferenceUtils.
                getSinglePrefrenceString(ProfileActivity.this, R.string.settings_def_storeemail_key);
        if (!checkEmail.startsWith("autoemail") && !checkEmail.endsWith("@mail.com")
                || checkEmail.equals(Constant.PREF_NULL)){
            getBalance();
        }

        if (Eztytopup.getSunmiDevice()){
            Helper.setImmersivebyKeyboard(container_view);
        }
    }

    private void getBalance(){
        String token = PreferenceUtils.getSinglePrefrenceString(ProfileActivity.this,
                R.string.settings_def_storeaccess_token_key);
        Call<WalletbalanceResponse> getBalace = Eztytopup.getsAPIService().getWalletbalance(token);
        getBalace.enqueue(new Callback<WalletbalanceResponse>() {
            @Override
            public void onResponse(Call<WalletbalanceResponse> call,
                                   Response<WalletbalanceResponse> response) {
                if (response.isSuccessful() &&
                        response.body().status.getCode()
                                .equals(String.valueOf(HttpURLConnection.HTTP_CREATED))){
                      mSaldo.setText(response.body().getBalance().toString());
                }else{
                    Log.i(TAG, "onResponse: " + response.body().status.getMessage());
                }
            }

            @Override
            public void onFailure(Call<WalletbalanceResponse> call, Throwable t) {

            }
        });
    };

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
    public void initView() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_profile;
    }
}
