package com.ezytopup.salesapp.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.ezytopup.salesapp.Eztytopup;
import com.ezytopup.salesapp.R;
import com.ezytopup.salesapp.api.TokencheckResponse;
import com.ezytopup.salesapp.utility.Constant;
import com.ezytopup.salesapp.utility.Helper;
import com.ezytopup.salesapp.utility.PreferenceUtils;

import java.net.HttpURLConnection;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IntroActivityLaucher extends AppCompatActivity{
    private static final String TAG = "IntroActivityLaucher";
    private View mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        mProgressBar = findViewById(R.id.intro_progress_bar);
        String lastToken = PreferenceUtils.getSinglePrefrenceString(IntroActivityLaucher.this,
                R.string.settings_def_storeaccess_token_key);

        if (!lastToken.equals(Constant.PREF_NULL)){
            tokenValidityCheck(lastToken);
        }else {
            PreferenceUtils.destroyUserSession(IntroActivityLaucher.this);
            Login.start(IntroActivityLaucher.this);
        }
    }

    private void tokenValidityCheck(String token){
        mProgressBar.setVisibility(View.VISIBLE);

        Call<TokencheckResponse> tokenResponse = Eztytopup.getsAPIService().checkToken(token);
        tokenResponse.enqueue(new Callback<TokencheckResponse>() {
            @Override
            public void onResponse(Call<TokencheckResponse> call,
                                   Response<TokencheckResponse> response) {
                if (response.isSuccessful()
                        && response.body().status.getCode()
                                .equals(String.valueOf(HttpURLConnection.HTTP_OK))
                        && response.body().tokenValidity == Boolean.TRUE) {
                    mProgressBar.setVisibility(View.GONE);
                    MainActivity.start(IntroActivityLaucher.this);
                    finish();
                } else {
                    Helper.log(TAG, String.format("onResponse: %s",
                            response.body().status.getMessage()), null);
                    mProgressBar.setVisibility(View.GONE);
                    PreferenceUtils.destroyUserSession(IntroActivityLaucher.this);
                    Login.start(IntroActivityLaucher.this);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<TokencheckResponse> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
            }
        });
    }

}
