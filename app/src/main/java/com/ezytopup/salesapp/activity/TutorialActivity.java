package com.ezytopup.salesapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.constraint.ConstraintLayout;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ezytopup.salesapp.Eztytopup;
import com.ezytopup.salesapp.R;
import com.ezytopup.salesapp.adapter.RecyclerList_TutorialAdapter;
import com.ezytopup.salesapp.api.FaqResponse;
import com.ezytopup.salesapp.api.TutorialStepResponse;
import com.ezytopup.salesapp.utility.Helper;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TutorialActivity extends BaseActivity {

    private RecyclerList_TutorialAdapter adapter;
    private ArrayList<TutorialStepResponse.Result> results;
    private static final String TAG = "TutorialActivity";
    private ConstraintLayout container_layout;

    public static void start(Activity caller) {
        Intent intent = new Intent(caller, TutorialActivity.class);
        caller.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView titleText = (TextView) findViewById(R.id.tutorial_titlequetion);
        titleText.setVisibility(View.VISIBLE);
        container_layout = (ConstraintLayout) findViewById(R.id.container_tutorial_layout);
        results = new ArrayList<>();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.activity_tutorialrecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        adapter = new RecyclerList_TutorialAdapter(TutorialActivity.this, results);
        recyclerView.setAdapter(adapter);

        if (results.isEmpty() || results.size() == 0) getTutorial();

    }

    private void getTutorial() {
        Call<TutorialStepResponse> tutorial = Eztytopup.getsAPIService().getTutorialStep();
        tutorial.enqueue(new Callback<TutorialStepResponse>() {
            @Override
            public void onResponse(Call<TutorialStepResponse> call, Response<TutorialStepResponse> response) {
                if (response.isSuccessful() &&
                        response.body().status.getCode()
                                .equals(String.valueOf(HttpURLConnection.HTTP_OK))){
                    results.addAll(response.body().result);
                    adapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(TutorialActivity.this, response.body().status.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TutorialStepResponse> call, Throwable t) {
                Helper.apiSnacbarError(TutorialActivity.this, t, container_layout);
            }
        });
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
    public void initView() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_tutorial;
    }


}
