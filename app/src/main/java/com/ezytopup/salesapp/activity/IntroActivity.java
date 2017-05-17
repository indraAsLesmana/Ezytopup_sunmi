package com.ezytopup.salesapp.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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

public class IntroActivity extends AppCompatActivity{
    private static final String TAG = "IntroActivity";
    private View mProgressBar;
    private ConstraintLayout intro_container;
    private ConstraintLayout intro_errorcontainer;
    private Button btn_reload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        if (Eztytopup.getSunmiDevice()){
            PackageManager p = IntroActivity.this.getPackageManager();
            ComponentName cN = new ComponentName(IntroActivity.this, IntroActivityLaucher.class);
            p.setComponentEnabledSetting(cN, PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                    PackageManager.DONT_KILL_APP);
        }

        btn_reload = (Button) findViewById(R.id.btn_intro_reload);
        intro_container = (ConstraintLayout) findViewById(R.id.container_intro);
        intro_errorcontainer = (ConstraintLayout) findViewById(R.id.container_introerror);
        mProgressBar = findViewById(R.id.intro_progress_bar);
        final String lastToken = PreferenceUtils.getSinglePrefrenceString(IntroActivity.this,
                R.string.settings_def_storeaccess_token_key);

        tokenCheck(lastToken);
        btn_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tokenCheck(lastToken);
            }
        });
    }

    private void tokenCheck(String lastToken){
        intro_errorcontainer.setVisibility(View.GONE);
        if (!lastToken.equals(Constant.PREF_NULL)){
            tokenValidityCheck(lastToken);
        }else {
            PreferenceUtils.destroyUserSession(IntroActivity.this);
            Login.start(IntroActivity.this);
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
                    MainActivity.start(IntroActivity.this);
                    finish();
                } else {
                    Helper.log(TAG, String.format("onResponse: %s",
                            response.body().status.getMessage()), null);
                    mProgressBar.setVisibility(View.GONE);
                    PreferenceUtils.destroyUserSession(IntroActivity.this);
                    Login.start(IntroActivity.this);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<TokencheckResponse> call, Throwable t) {
                mProgressBar.setVisibility(View.GONE);
                intro_errorcontainer.setVisibility(View.VISIBLE);
            }
        });
    }

}
