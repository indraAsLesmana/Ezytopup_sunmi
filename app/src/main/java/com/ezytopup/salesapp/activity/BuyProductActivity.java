package com.ezytopup.salesapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ezytopup.salesapp.Eztytopup;
import com.ezytopup.salesapp.R;
import com.ezytopup.salesapp.api.DetailProductResponse;
import com.ezytopup.salesapp.api.ProductResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyProductActivity extends BaseActivity {

    private static final String PRODUCT = "BuyProductActivity::productid";
    private ProductResponse.Product mProduct;
    private ArrayList<DetailProductResponse.Result> results;
    private TextView mSubtotal, mAdminFee, mTotal;
    private ImageView mBackgroundProduct;
    private static final String TAG = "BuyProductActivity";

    public static void start(Activity caller, ProductResponse.Product product) {
        Intent intent = new Intent(caller, BuyProductActivity.class);
        intent.putExtra(PRODUCT, product);
        caller.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (getIntent().getSerializableExtra(BuyProductActivity.PRODUCT) == null){
            finish();
            return;
        }
        results = new ArrayList<>();
        mBackgroundProduct = (ImageView) findViewById(R.id.buy_bgproduct);
        mTotal = (TextView) findViewById(R.id.buy_total);
        mSubtotal = (TextView) findViewById(R.id.buy_subtotal);
        mAdminFee = (TextView) findViewById(R.id.buy_adminfee);

        mProduct = (ProductResponse.Product)getIntent().
                getSerializableExtra(BuyProductActivity.PRODUCT);
        getDetailProduct();
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

    private void getDetailProduct(){
        Call<DetailProductResponse> product = Eztytopup.getsAPIService().
                getDetailProduct(mProduct.getProductId());
        product.enqueue(new Callback<DetailProductResponse>() {
            @Override
            public void onResponse(Call<DetailProductResponse> call,
                                   Response<DetailProductResponse> response) {
                if (response.isSuccessful()){
                    results.addAll(response.body().result);
                    DetailProductResponse.Result r = results.get(0);
                    // note API harusnya bentuknya langsung Object,
                    // tidak ada Array. toh yg dikirim satu object
                    mTotal.setText(r.getHargaToko());
                    mSubtotal.setText(r.getHargaToko());

                    Glide.with(BuyProductActivity.this)
                            .load(mProduct.getBackgroundImageUrl()).centerCrop()
                            .crossFade()
                            .into(mBackgroundProduct);

                }
            }
            @Override
            public void onFailure(Call<DetailProductResponse> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

    @Override
    public void initView() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_buyproduct;
    }
}
