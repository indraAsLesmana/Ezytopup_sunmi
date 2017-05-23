package com.ezytopup.salesapp.activity;

import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ezytopup.salesapp.BuildConfig;
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
    private ConstraintLayout intro_container;
    private ConstraintLayout intro_errorcontainer;
    private Button btn_reload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        btn_reload = (Button) findViewById(R.id.btn_intro_reload);
        intro_container = (ConstraintLayout) findViewById(R.id.container_intro);
        intro_errorcontainer = (ConstraintLayout) findViewById(R.id.container_introerror);
        mProgressBar = findViewById(R.id.intro_progress_bar);
        TextView version = (TextView)findViewById(R.id.version_code);
        final String lastToken = PreferenceUtils.getSinglePrefrenceString(IntroActivityLaucher.this,
                R.string.settings_def_storeaccess_token_key);

        tokenCheck(lastToken);
        btn_reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tokenCheck(lastToken);
            }
        });

        if(version != null) {
            String versionStr = Integer.toString(BuildConfig.VERSION_CODE);
            versionStr += "-" + BuildConfig.BUILD_TYPE;
            version.setText(String.format("%s: %s", getString(R.string.code_version), versionStr));
        }
    }

    private void tokenCheck(String lastToken){
        intro_errorcontainer.setVisibility(View.GONE);
        if (!lastToken.equals(Constant.PREF_NULL)){
            tokenValidityCheck(lastToken);
        }else {
            PreferenceUtils.destroyUserSession(IntroActivityLaucher.this);
            Login.start(IntroActivityLaucher.this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Eztytopup.setIsUserReseller(PreferenceUtils.getSinglePrefrenceString(this,
                R.string.settings_def_sellerid_key).equals(Constant.PREF_NULL)
                ? Boolean.FALSE : Boolean.TRUE);
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
                intro_errorcontainer.setVisibility(View.VISIBLE);
            }
        });
    }

}
