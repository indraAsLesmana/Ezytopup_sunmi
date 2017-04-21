package com.ezytopup.salesapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.ezytopup.salesapp.Eztytopup;
import com.ezytopup.salesapp.R;
import com.ezytopup.salesapp.adapter.RecyclerList_CategoryAdapter;
import com.ezytopup.salesapp.api.CategoryResponse;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CategoryActivity extends BaseActivity implements
        RecyclerList_CategoryAdapter.RecyclerList_CategoryAdapterlistener{

    private static final String CATEGORY_NAME = "CategoryActivity::productName";
    private static final String CATEGORY_ID = "CategoryActivity::productId";
    private RecyclerList_CategoryAdapter adapter;
    private ArrayList<CategoryResponse.Product> results;
    private static final String TAG = "CategoryActivity";
    private String mCategoryId;
    private TextView mGeneral_list;

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
        mGeneral_list = (TextView) findViewById(R.id.general_emptylist);
        TextView categoryTitle = (TextView) findViewById(R.id.faq_titlequetion);
        categoryTitle.setText(String.format("%s: %s", getString(R.string.category), mCategoryName));

        results = new ArrayList<>();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.activity_generalmainrecycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        recyclerView.setHasFixedSize(true);
        adapter = new RecyclerList_CategoryAdapter(CategoryActivity.this, results, this);
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
        Call<CategoryResponse> category = Eztytopup.getsAPIService().getCategory(mCategoryId);
        category.enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if (response.isSuccessful() &&
                        response.body().status.getCode()
                                .equals(String.valueOf(HttpURLConnection.HTTP_OK))){
                    results.addAll(response.body().products);
                    if (results.size() == 0) mGeneral_list.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(CategoryActivity.this, response.body().status.getMessage(),
                            Toast.LENGTH_SHORT).show();
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
        return R.layout.activity_generallist;
    }

    @Override
    public void onCardClick(CategoryResponse.Product product) {
        BuyProductActivity.start(this,
                product.getProductId(),
                product.getProductName(),
                product.getReviewUrl(),
                product.getBackgroundImageUrl(),
                product.getPrice());
    }
}
