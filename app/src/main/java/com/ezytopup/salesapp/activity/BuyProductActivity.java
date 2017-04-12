package com.ezytopup.salesapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ezytopup.salesapp.Eztytopup;
import com.ezytopup.salesapp.R;
import com.ezytopup.salesapp.api.PaymentResponse;
import com.ezytopup.salesapp.api.DetailProductResponse;
import com.ezytopup.salesapp.utility.Constant;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BuyProductActivity extends BaseActivity implements View.OnClickListener{

    private static final String PRODUCT_ID = "BuyProductActivity::productid";
    private static final String PRODUCT_NAME = "BuyProductActivity::productname";
    private static final String PRODUCT_IMAGE = "BuyProductActivity::productimage";
    private static final String PRODUCT_BG = "BuyProductActivity::productbackground";
    private static final String PRODUCT_PRICE = "BuyProductActivity::productprice";
    private ArrayList<PaymentResponse.PaymentMethod> paymentActive;
    private ArrayList<DetailProductResponse.Result> results;
    private TextView mSubtotal;
    private TextView mTotal;
    private static final String TAG = "BuyProductActivity";
    private String productId;

    public static void start(Activity caller, String id, String name, String image, String bg,
                             String price) {
        Intent intent = new Intent(caller, BuyProductActivity.class);
        intent.putExtra(PRODUCT_ID, id);
        intent.putExtra(PRODUCT_NAME, name);
        intent.putExtra(PRODUCT_IMAGE, image);
        intent.putExtra(PRODUCT_BG, bg);
        intent.putExtra(PRODUCT_PRICE, price);
        caller.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (getIntent().getStringExtra(BuyProductActivity.PRODUCT_ID) == null||
                getIntent().getStringExtra(BuyProductActivity.PRODUCT_NAME) == null||
                getIntent().getStringExtra(BuyProductActivity.PRODUCT_IMAGE) == null||
                getIntent().getStringExtra(BuyProductActivity.PRODUCT_BG) == null||
                getIntent().getStringExtra(BuyProductActivity.PRODUCT_PRICE) == null){
            finish();
            return;
        }

        paymentActive = new ArrayList<>();
        results = new ArrayList<>();
        ImageView mBackgroundProduct = (ImageView) findViewById(R.id.buy_bgimage);
        ImageView mProductImage = (ImageView) findViewById(R.id.buy_productimages);
        TextView mProductTitle = (TextView) findViewById(R.id.buy_producttitle);
        TextView mProductPrice = (TextView) findViewById(R.id.buy_productprice);
        mTotal = (TextView) findViewById(R.id.buy_total);
        mSubtotal = (TextView) findViewById(R.id.buy_subtotal);
        TextView mAdminFee = (TextView) findViewById(R.id.buy_adminfee);

        productId = getIntent().getStringExtra(BuyProductActivity.PRODUCT_ID);
        String productName = getIntent().getStringExtra(BuyProductActivity.PRODUCT_NAME);
        String productImage = getIntent().getStringExtra(BuyProductActivity.PRODUCT_IMAGE);
        String productBackground = getIntent().getStringExtra(BuyProductActivity.PRODUCT_BG);
        String productPrice = getIntent().getStringExtra(BuyProductActivity.PRODUCT_PRICE);

        mProductTitle.setText(productName);
        mProductPrice.setText(productPrice);

        Glide.with(BuyProductActivity.this)
                .load(productBackground).centerCrop()
                .crossFade(Constant.ITEM_CROSSFADEDURATION)
                .into(mBackgroundProduct);
        mBackgroundProduct.setImageAlpha(Constant.DEF_BGALPHA);

        Glide.with(BuyProductActivity.this)
                .load(productImage).centerCrop()
                .crossFade(Constant.ITEM_CROSSFADEDURATION)
                .into(mProductImage);
        mTotal.setText(productPrice);
        mSubtotal.setText(productPrice);

//        getDetailProduct();
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
                getDetailProduct(productId);
        product.enqueue(new Callback<DetailProductResponse>() {
            @Override
            public void onResponse(Call<DetailProductResponse> call,
                                   Response<DetailProductResponse> response) {
                if (response.isSuccessful()){
                    results.addAll(response.body().result);
                    DetailProductResponse.Result r = results.get(0);
                    mTotal.setText(r.getHargaToko());
                    mSubtotal.setText(r.getHargaToko());
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

    @Override
    public void onClick(View v) {

    }
}
