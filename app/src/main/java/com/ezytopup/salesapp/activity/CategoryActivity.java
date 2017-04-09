package com.ezytopup.salesapp.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ezytopup.salesapp.Eztytopup;
import com.ezytopup.salesapp.R;
import com.ezytopup.salesapp.adapter.RecyclerList_CategoryAdapter;
import com.ezytopup.salesapp.api.CategoryResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CategoryActivity extends BaseActivity {

    private static final String CATEGORY_NAME = "CategoryActivity::productName";
    private static final String CATEGORY_ID = "CategoryActivity::productId";
    private RecyclerList_CategoryAdapter adapter;
    private ArrayList<CategoryResponse.Product> results;
    private static final String TAG = "CategoryActivity";

    private String mCategoryId;


    public static void start(Activity caller, String categoryName, String categoryId) {
        Intent intent = new Intent(caller, CategoryActivity.class);
        intent.putExtra(CATEGORY_NAME, categoryName);
        intent.putExtra(CATEGORY_ID, categoryId);
        caller.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (getIntent().getStringExtra(CategoryActivity.CATEGORY_ID) == null){
            return;
        }
        String mCategoryName = getIntent().getStringExtra(CategoryActivity.CATEGORY_NAME);
        mCategoryId = getIntent().getStringExtra(CategoryActivity.CATEGORY_ID);

        TextView categoryTitle = (TextView) findViewById(R.id.faq_titlequetion);
        categoryTitle.setText(String.format("%s: %s", getString(R.string.category), mCategoryName));

        results = new ArrayList<>();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.faq_mainrecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        adapter = new RecyclerList_CategoryAdapter(CategoryActivity.this, results);
        recyclerView.setAdapter(adapter);

        if (results.isEmpty() || results.size() == 0) getCategory();
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

    private void getCategory() {
        Call<CategoryResponse> category = Eztytopup.getsAPIService().getCategery(mCategoryId);
        category.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (response.isSuccessful()){
                    results.addAll(response.body().products);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {

            }
        });
    }

    @Override
    public void initView() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_faq_andterm;
    }
}
