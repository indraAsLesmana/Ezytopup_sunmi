package com.ezytopup.salesapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.ezytopup.salesapp.Eztytopup;
import com.ezytopup.salesapp.R;
import com.ezytopup.salesapp.adapter.RecyclerList_FaqAdapter;
import com.ezytopup.salesapp.api.FaqResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FaqActivity extends BaseActivity {

    private RecyclerList_FaqAdapter adapter;
    private ArrayList<FaqResponse.Result> results;

    public static void start(Activity caller) {
        Intent intent = new Intent(caller, FaqActivity.class);
        caller.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar.setDisplayHomeAsUpEnabled(true);

        results = new ArrayList<>();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.faq_mainrecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        adapter = new RecyclerList_FaqAdapter(FaqActivity.this, results);
        recyclerView.setAdapter(adapter);
        getQuestion();
    }

    private void getQuestion(){
        Call<FaqResponse> faq = Eztytopup.getsAPIService().getFaq();
        faq.enqueue(new Callback<FaqResponse>() {
            @Override
            public void onResponse(Call<FaqResponse> call, Response<FaqResponse> response) {
                if (response.isSuccessful()){
                    results.addAll(response.body().result);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<FaqResponse> call, Throwable t) {
                Toast.makeText(FaqActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void initView() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_faq;
    }
}
