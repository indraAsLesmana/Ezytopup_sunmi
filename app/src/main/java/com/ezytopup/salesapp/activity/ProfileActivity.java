package com.ezytopup.salesapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;

import com.ezytopup.salesapp.Eztytopup;
import com.ezytopup.salesapp.R;
import com.ezytopup.salesapp.api.EzytopupAPI;
import com.ezytopup.salesapp.api.WalletbalanceResponse;
import com.ezytopup.salesapp.utility.Constant;
import com.ezytopup.salesapp.utility.PreferenceUtils;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends BaseActivity {

    private static final String TAG = "ProfileActivity";
    private EditText mSaldo;

    public static void start(Activity caller) {
        Intent intent = new Intent(caller, ProfileActivity.class);
        caller.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mSaldo = (EditText) findViewById(R.id.profileSaldo);

        String checkEmail = PreferenceUtils.
                getSinglePrefrenceString(ProfileActivity.this, R.string.settings_def_storeemail_key);
        if (!checkEmail.startsWith("autoemail") && !checkEmail.endsWith("@mail.com")
                || checkEmail.equals(Constant.PREF_NULL)){
            getBalance();
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
