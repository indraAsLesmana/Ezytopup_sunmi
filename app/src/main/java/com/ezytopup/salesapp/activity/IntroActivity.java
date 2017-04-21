package com.ezytopup.salesapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import com.ezytopup.salesapp.Eztytopup;
import com.ezytopup.salesapp.R;
import com.ezytopup.salesapp.api.TokencheckResponse;
import com.ezytopup.salesapp.utility.PreferenceUtils;
import java.net.HttpURLConnection;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IntroActivity extends AppCompatActivity{
    private static final String TAG = "IntroActivity";
    private View mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        mProgressBar = findViewById(R.id.intro_progress_bar);
        mProgressBar.setVisibility(View.VISIBLE);
        String lastToken = PreferenceUtils.getSinglePrefrenceString(IntroActivity.this,
                R.string.settings_def_storeaccess_token_key);
        if (!lastToken.equals("-")){
            tokenValidityCheck(lastToken);
        }else {
            PreferenceUtils.destroyUserSession(IntroActivity.this);
            Login.start(IntroActivity.this);
        }
    }

    private void tokenValidityCheck(String token){
        Call<TokencheckResponse> tokenResponse = Eztytopup.getsAPIService().checkToken(token);
        tokenResponse.enqueue(new Callback<TokencheckResponse>() {
            @Override
            public void onResponse(Call<TokencheckResponse> call, Response<TokencheckResponse> response) {
                if (response.isSuccessful()
                        && response.body().status.getCode()
                                .equals(String.valueOf(HttpURLConnection.HTTP_OK))
                        && response.body().tokenValidity == Boolean.TRUE) {
                    MainActivity.start(IntroActivity.this);
                    finish();
                } else {
                    Log.i(TAG, String.format("onResponse: %s", response.body().status.getMessage()));
                    PreferenceUtils.destroyUserSession(IntroActivity.this);
                    Login.start(IntroActivity.this);
                    finish();
                }
            }

            @Override
            public void onFailure(Call<TokencheckResponse> call, Throwable t) {

            }
        });
    }

}
