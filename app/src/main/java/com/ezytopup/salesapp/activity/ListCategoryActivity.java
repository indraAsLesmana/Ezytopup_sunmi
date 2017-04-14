package com.ezytopup.salesapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.ezytopup.salesapp.Eztytopup;
import com.ezytopup.salesapp.R;
import com.ezytopup.salesapp.adapter.RecyclerList_ListCategoryAdapter;
import com.ezytopup.salesapp.api.ListCategoryResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ListCategoryActivity extends BaseActivity implements
        RecyclerList_ListCategoryAdapter.RecyclerList_ListCategoryAdapterlistener{

    private ArrayList<ListCategoryResponse.Category> allCategoryProduct;
    private RecyclerList_ListCategoryAdapter adapter;
    private static final String TAG = "HomeFragment";

    public static void start(Activity caller) {
        Intent intent = new Intent(caller, ListCategoryActivity.class);
        caller.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar.setDisplayHomeAsUpEnabled(true);

        allCategoryProduct = new ArrayList<>();
        RecyclerView my_recycler_view = (RecyclerView) findViewById(R.id.faq_mainrecycler);
        my_recycler_view.setHasFixedSize(true);
        my_recycler_view.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));
        adapter = new RecyclerList_ListCategoryAdapter(allCategoryProduct, this, this);
        my_recycler_view.setAdapter(adapter);
        getlistCategory();
    }

    private void getlistCategory() {
        Call<ListCategoryResponse> listcategory = Eztytopup.getsAPIService().getListCategory();
        listcategory.enqueue(new Callback<ListCategoryResponse>() {
            @Override
            public void onResponse(Call<ListCategoryResponse> call, Response<ListCategoryResponse> response) {
                if (response.isSuccessful()){
                    allCategoryProduct.addAll(response.body()._0.categories);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ListCategoryResponse> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t.getMessage());
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
    public void onCardClick(ListCategoryResponse.Category singleItem) {
        Toast.makeText(this, singleItem.getName(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void initView() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_generallist;
    }
}
