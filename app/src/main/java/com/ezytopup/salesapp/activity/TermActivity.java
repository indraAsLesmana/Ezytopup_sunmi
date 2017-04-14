package com.ezytopup.salesapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ezytopup.salesapp.Eztytopup;
import com.ezytopup.salesapp.R;
import com.ezytopup.salesapp.adapter.RecyclerList_TermAdapter;
import com.ezytopup.salesapp.api.TermResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TermActivity extends BaseActivity {

    private RecyclerList_TermAdapter adapter;
    private ArrayList<TermResponse.Result> results;

    public static void start(Activity caller) {
        Intent intent = new Intent(caller, TermActivity.class);
        caller.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar.setDisplayHomeAsUpEnabled(true);

        TextView titleText = (TextView) findViewById(R.id.faq_titlequetion);
        titleText.setVisibility(View.GONE);

        results = new ArrayList<>();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.faq_mainrecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        adapter = new RecyclerList_TermAdapter(TermActivity.this, results);
        recyclerView.setAdapter(adapter);

        if (results.isEmpty() || results.size() == 0) getTerm(); //TODO not work, still load on internet
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

    private void getTerm() {
        Call<TermResponse> term = Eztytopup.getsAPIService().getTerm();
        term.enqueue(new Callback<TermResponse>() {
            @Override
            public void onResponse(Call<TermResponse> call, Response<TermResponse> response) {
                if (response.isSuccessful()) {
                    results.addAll(response.body().result);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<TermResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void initView() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_generallist;
    }
}
