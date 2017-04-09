package com.ezytopup.salesapp.activity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ezytopup.salesapp.Eztytopup;
import com.ezytopup.salesapp.R;
import com.ezytopup.salesapp.utility.PreferenceUtils;

public class IntroActivity extends AppCompatActivity{
    private View mProgressBar;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        mProgressBar = findViewById(R.id.intro_progress_bar);
        sharedPreferences = Eztytopup.getsPreferences();

        new PreferenceCheck().execute(sharedPreferences);
        mProgressBar.setVisibility(View.VISIBLE);

    }

    private class PreferenceCheck extends AsyncTask<SharedPreferences, Void, SharedPreferences>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected SharedPreferences doInBackground(SharedPreferences... params) {
            SharedPreferences preferences = params[0];
            return preferences;
        }

        @Override
        protected void onPostExecute(SharedPreferences sharedPreferences) {
            super.onPostExecute(sharedPreferences);
            mProgressBar.setVisibility(View.GONE);

            if (sharedPreferences.getAll() != null){
                MainActivity.start(IntroActivity.this);
            }else {
                PreferenceUtils.destroyUserSession(IntroActivity.this);
                Login.start(IntroActivity.this);
            }

        }
    }

}
