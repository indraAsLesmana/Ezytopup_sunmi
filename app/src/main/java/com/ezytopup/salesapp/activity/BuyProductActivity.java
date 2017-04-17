package com.ezytopup.salesapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ezytopup.salesapp.Eztytopup;
import com.ezytopup.salesapp.R;
import com.ezytopup.salesapp.adapter.Grid_PaymentAdapter;
import com.ezytopup.salesapp.api.PaymentResponse;
import com.ezytopup.salesapp.api.DetailProductResponse;
import com.ezytopup.salesapp.utility.Constant;

import java.net.HttpURLConnection;
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
    private ArrayList<DetailProductResponse.Result> results;
    private TextView mSubtotal;
    private TextView mTotal;
    private static final String TAG = "BuyProductActivity";
    private String productId;
    private TextView bt_Detailproduct;
    private View view_desc;
    private ConstraintLayout view_detailbuy;
    private TextView info1, info2, info3, buy_desc;
    private RelativeLayout e_payment, bank_transfer, credit_card, ezy_wallet;
    private ImageView e_paymentStatus, bank_transferStatus, credit_cardStatus, ezy_walletStatus;
    private TextView e_paymentTv, bank_transferTv, credit_cardTv, ezy_walletTv;
    private GridView e_paymentGrid, bank_transferGrid, credit_cardGrid, ezy_walletGrid;

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

        results = new ArrayList<>();
        info1 = (TextView) findViewById(R.id.buy_info1);
        info2 = (TextView) findViewById(R.id.buy_info2);
        info3 = (TextView) findViewById(R.id.buy_info3);
        buy_desc = (TextView) findViewById(R.id.buy_description);
        view_desc = findViewById(R.id.buy_descview);
        view_detailbuy = (ConstraintLayout) findViewById(R.id.buy_detailview);
        bt_Detailproduct = (TextView) findViewById(R.id.tvDetailProduct);
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

        e_payment = (RelativeLayout) findViewById(R.id.rlPayment);
        bank_transfer = (RelativeLayout) findViewById(R.id.rlBanktransfer);
        credit_card = (RelativeLayout) findViewById(R.id.rlCreditcard);
        ezy_wallet = (RelativeLayout) findViewById(R.id.rlWallet);
        e_paymentStatus = (ImageView) findViewById(R.id.rivPayment);
        bank_transferStatus = (ImageView) findViewById(R.id.rivBanktransfer);
        credit_cardStatus = (ImageView) findViewById(R.id.rivCreditcard);
        ezy_walletStatus = (ImageView) findViewById(R.id.rivWallet);
        e_paymentTv = (TextView) findViewById(R.id.tvPayment);
        bank_transferTv = (TextView) findViewById(R.id.tvBanktransfer);
        credit_cardTv = (TextView) findViewById(R.id.tvCreditcard);
        ezy_walletTv = (TextView) findViewById(R.id.tvWallet);
        e_paymentGrid = (GridView) findViewById(R.id.gridePayment);
        bank_transferGrid = (GridView) findViewById(R.id.gridBanktransfer);
        credit_cardGrid = (GridView) findViewById(R.id.gridCreditcard);
        ezy_walletGrid = (GridView) findViewById(R.id.gridWallet);

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

        bt_Detailproduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (view_detailbuy.isShown()){
                    bt_Detailproduct.setText(R.string.buy);
                    view_detailbuy.setVisibility(View.GONE);
                    view_desc.setVisibility(View.VISIBLE);
                }else {
                    bt_Detailproduct.setText(R.string.detail_product);
                    view_detailbuy.setVisibility(View.VISIBLE);
                    view_desc.setVisibility(View.GONE);
                }
            }
        });

        getDetailProduct();
        setActivePayment();
    }

    private void setActivePayment() {
        ArrayList<PaymentResponse.PaymentMethod> paymentActive = Eztytopup.getPaymentActive();
        for (int i = 0; i < paymentActive.size(); i++) {
            String paymentid = paymentActive.get(i).getId();
            switch (paymentid){
                case Constant.INTERNET_BANK:
                    e_paymentTv.setText(paymentActive.get(i).getPaymentMethod());
                    getImage(paymentActive.get(i).getPaymentLogo(), e_paymentStatus);
                    ArrayList<PaymentResponse.PaymentMethod> epaymentData = Eztytopup.getPaymentInternet();
                    Grid_PaymentAdapter paymentAdapter = new
                            Grid_PaymentAdapter(this, epaymentData);
                    e_paymentGrid.setAdapter(paymentAdapter);
                    e_payment.setVisibility(View.VISIBLE);
                    e_paymentGrid.setVisibility(View.VISIBLE);
                    break;
                case Constant.BANK_TRANSFER:
                    bank_transferTv.setText(paymentActive.get(i).getPaymentMethod());
                    getImage(paymentActive.get(i).getPaymentLogo(), bank_transferStatus);
                    ArrayList<PaymentResponse.PaymentMethod> transferData = Eztytopup.getPaymentTransfer();
                    Grid_PaymentAdapter transferAdapter = new
                            Grid_PaymentAdapter(this, transferData);
                    bank_transferGrid.setAdapter(transferAdapter);
                    bank_transfer.setVisibility(View.VISIBLE);
                    bank_transferGrid.setVisibility(View.VISIBLE);
                    break;
                case Constant.CREADIT_CARD:
                    credit_cardTv.setText(paymentActive.get(i).getPaymentMethod());
                    getImage(paymentActive.get(i).getPaymentLogo(), credit_cardStatus);
                    ArrayList<PaymentResponse.PaymentMethod> creditData = Eztytopup.getPaymentCredit();
                    Grid_PaymentAdapter creditAdapter = new
                            Grid_PaymentAdapter(this, creditData);
                    credit_cardGrid.setAdapter(creditAdapter);
                    credit_card.setVisibility(View.VISIBLE);
                    credit_cardGrid.setVisibility(View.VISIBLE);
                    break;
                case Constant.EZYTOPUP_WALLET:
                    ezy_walletTv.setText(paymentActive.get(i).getPaymentMethod());
                    getImage(paymentActive.get(i).getPaymentLogo(), ezy_walletStatus);
                    ArrayList<PaymentResponse.PaymentMethod> ezywalletData = Eztytopup.getPaymentWallet();
                    Grid_PaymentAdapter walletAdapter = new
                            Grid_PaymentAdapter(this, ezywalletData);
                    ezy_walletGrid.setAdapter(walletAdapter);
                    ezy_wallet.setVisibility(View.VISIBLE);
                    ezy_walletGrid.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }

    private void getImage(String url, ImageView imagePlace){
        if (url == null){
            return;
        }
        Glide.with(BuyProductActivity.this)
                .load(url).centerCrop()
                .error(R.drawable.com_facebook_profile_picture_blank_square)
                .crossFade(Constant.ITEM_CROSSFADEDURATION)
                .into(imagePlace);
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
                if (response.isSuccessful() &&
                        response.body().status.getCode()
                                .equals(String.valueOf(HttpURLConnection.HTTP_OK))){
                    results.addAll(response.body().result);
                    DetailProductResponse.Result r = results.get(0);
                    info1.setText(r.getInfo1());
                    info2.setText(r.getInfo2());
                    info3.setText(r.getInfo3());
                    buy_desc.setText(r.getDescription());
                }else {
                    Toast.makeText(BuyProductActivity.this, response.body().status.getMessage(),
                            Toast.LENGTH_SHORT).show();
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
