package com.ezytopup.salesapp.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.ezytopup.salesapp.Eztytopup;
import com.ezytopup.salesapp.R;
import com.ezytopup.salesapp.api.BuynowReseller;
import com.ezytopup.salesapp.api.DetailProductResponse;
import com.ezytopup.salesapp.api.VoucherprintResponse;
import com.ezytopup.salesapp.printhelper.ThreadPoolManager;
import com.ezytopup.salesapp.utility.Constant;
import com.ezytopup.salesapp.utility.Helper;
import com.ezytopup.salesapp.utility.PreferenceUtils;
import com.zj.btsdk.PrintPic;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by indraaguslesmana on 5/17/17.
 */

public class BuyResellerActivity extends BaseActivity implements View.OnClickListener{

    private static final String PRODUCT_ID = "BuyResellerActivity::productid";
    private static final String PRODUCT_NAME = "BuyResellerActivity::productname";
    private static final String PRODUCT_IMAGE = "BuyResellerActivity::productimage";
    private static final String PRODUCT_BG = "BuyResellerActivity::productbackground";
    private static final String PRODUCT_PRICE = "BuyResellerActivity::productprice";
    private static final String TAG = "BuyResellerActivity";
    private Button buynowButton, cancelButton;
    private String productId, productName, productImage, productBackground, productPrice;
    private TextView mSubtotal, mTotal, info1, info2, info3, buy_desc, textView4;
    private ArrayList<DetailProductResponse.Result> results;

    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private BluetoothDevice con_dev = null;

    public static void start(Activity caller, String id, String name, String image, String bg,
                             String price) {
        Intent intent = new Intent(caller, BuyResellerActivity.class);
        intent.putExtra(PRODUCT_ID, id);
        intent.putExtra(PRODUCT_NAME, name);
        intent.putExtra(PRODUCT_IMAGE, image);
        intent.putExtra(PRODUCT_BG, bg);
        intent.putExtra(PRODUCT_PRICE, price);
        caller.startActivity(intent);
    }
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        actionBar.setDisplayHomeAsUpEnabled(true);

        if (getIntent().getStringExtra(BuyResellerActivity.PRODUCT_ID) == null||
                getIntent().getStringExtra(BuyResellerActivity.PRODUCT_NAME) == null||
                getIntent().getStringExtra(BuyResellerActivity.PRODUCT_IMAGE) == null||
                getIntent().getStringExtra(BuyResellerActivity.PRODUCT_BG) == null||
                getIntent().getStringExtra(BuyResellerActivity.PRODUCT_PRICE) == null){

            finish();
            return;
        }else {
            productId = getIntent().getStringExtra(BuyResellerActivity.PRODUCT_ID);
            productName = getIntent().getStringExtra(BuyResellerActivity.PRODUCT_NAME);
            productImage = getIntent().getStringExtra(BuyResellerActivity.PRODUCT_IMAGE);
            productBackground = getIntent().getStringExtra(BuyResellerActivity.PRODUCT_BG);
            productPrice = getIntent().getStringExtra(BuyResellerActivity.PRODUCT_PRICE);
        }
        results = new ArrayList<>();

        buynowButton = (Button) findViewById(R.id.btnBuyNow);
        cancelButton = (Button) findViewById(R.id.btnCancel);
        ImageView mBackgroundProduct = (ImageView) findViewById(R.id.buy_bgimage);
        ImageView mProductImage = (ImageView) findViewById(R.id.buy_productimages);
        TextView mProductTitle = (TextView) findViewById(R.id.buy_producttitle);
        TextView mProductPrice = (TextView) findViewById(R.id.buy_productprice);
        textView4 = (TextView) findViewById(R.id.textView4);
        info1 = (TextView) findViewById(R.id.buy_info1);
        info2 = (TextView) findViewById(R.id.buy_info2);
        info3 = (TextView) findViewById(R.id.buy_info3);
        mTotal = (TextView) findViewById(R.id.buy_total);
        mSubtotal = (TextView) findViewById(R.id.buy_subtotal);
        buy_desc = (TextView) findViewById(R.id.buy_description);

        //disable information detail
        textView4.setVisibility(View.GONE);
        info1.setVisibility(View.GONE);
        info2.setVisibility(View.GONE);
        info3.setVisibility(View.GONE);

        buynowButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        mProductTitle.setText(productName);
        mProductPrice.setText(productPrice);
        Glide.with(BuyResellerActivity.this)
                .load(productBackground).centerCrop()
                .error(R.drawable.ic_error_loadimage)
                .crossFade(Constant.ITEM_CROSSFADEDURATION)
                .into(mBackgroundProduct);
        mBackgroundProduct.setImageAlpha(Constant.DEF_BGALPHA);

        Glide.with(BuyResellerActivity.this)
                .load(productImage).centerCrop()
                .error(R.drawable.ic_error_loadimage)
                .crossFade(Constant.ITEM_CROSSFADEDURATION)
                .into(mProductImage);
        mTotal.setText(productPrice);
        mSubtotal.setText(productPrice);

        getDetailProduct();
    }

    private void buyNowReseller(){
        String deviceId = PreferenceUtils.getSinglePrefrenceString(BuyResellerActivity.this,
                R.string.settings_def_storeidevice_key);
        String email = PreferenceUtils.getSinglePrefrenceString(BuyResellerActivity.this,
                R.string.settings_def_storeemail_key);
        String customerId = PreferenceUtils.getSinglePrefrenceString(BuyResellerActivity.this,
                R.string.settings_def_uid_key);
        String token = PreferenceUtils.getSinglePrefrenceString(BuyResellerActivity.this,
                R.string.settings_def_storeaccess_token_key);
        String sellerId = PreferenceUtils.getSinglePrefrenceString(BuyResellerActivity.this,
                R.string.settings_def_sellerid_key);
        String sellerShopName = PreferenceUtils.getSinglePrefrenceString(BuyResellerActivity.this,
                R.string.settings_def_sellershopname_key);
        String sellerKasirName = PreferenceUtils.getSinglePrefrenceString(BuyResellerActivity.this,
                R.string.settings_def_sellerkasirname_key);

        HashMap<String, String> data = new HashMap<>();
        data.put("device_id", deviceId);
        data.put("product_id", "3169");
        data.put("email", email);
        data.put("customerId", customerId);
        data.put("session_name", token);
        data.put("seller_id", sellerId);
        data.put("seller_password", "12341234");
        data.put("seller_shop_name", sellerShopName);
        data.put("seller_kasir_name", sellerKasirName);

        Call<VoucherprintResponse> buyProduct = Eztytopup.getsAPIService().getBuyreseller(data);
        buyProduct.enqueue(new Callback<VoucherprintResponse>() {
            @Override
            public void onResponse(Call<VoucherprintResponse> call, Response<VoucherprintResponse> response) {
                if (response.isSuccessful() && response.body().status.getCode()
                        .equals(String.valueOf(HttpURLConnection.HTTP_OK))){
                        bluetoothPrint(response);
                }else {
                    Toast.makeText(BuyResellerActivity.this, response.body().status.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<VoucherprintResponse> call, Throwable t) {
                Helper.log(TAG, t.getMessage(), null);
                Toast.makeText(BuyResellerActivity.this, t.getMessage(),
                        Toast.LENGTH_SHORT).show();
            }
        });
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
                    buy_desc.setText(r.getDescription());
                }else {
                    Toast.makeText(BuyResellerActivity.this, response.body().status.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<DetailProductResponse> call, Throwable t) {
                Helper.log(TAG, t.getMessage(), t);
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnBuyNow:
                if (!Eztytopup.getSunmiDevice()) {
                    if (!Eztytopup.getmBTprintService().isBTopen()) { // is blutooth Enable on that device?
                        Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                    } else if (!Eztytopup.getIsPrinterConnected()) {  // is bluetooth connected to printer?
                        Intent serverIntent = new Intent(BuyResellerActivity.this,
                                DeviceListActivity.class);
                        startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                    } else {
                        buyNowReseller();
                    }
                } else {
                    sunmiPrint();
                }
                break;
            case R.id.btnCancel:
                finish();
                break;
        }
        
    }
    
    // TODO : print will still printed, no Flag to dot print. i'll fix latter
    @SuppressLint("SdCardPath")
    private Boolean printImage() {
        File file = new File("/mnt/sdcard/Ezytopup/print_logo.jpg");
        if (!file.exists()) {
            Helper.downloadFile(this, PreferenceUtils.getSinglePrefrenceString(this,
                    R.string.settings_def_sellerprintlogo_key));
            Toast.makeText(this, R.string.please_wait_imageprint, Toast.LENGTH_SHORT).show();
            return Boolean.FALSE;
        }else {
            byte[] sendData = null;
            PrintPic pg = new PrintPic();
            pg.initCanvas(384);
            pg.initPaint();
            pg.drawImage(100, 0, "/mnt/sdcard/Ezytopup/print_logo.jpg");
            sendData = pg.printDraw();
            Eztytopup.getmBTprintService().write(sendData);
            return Boolean.TRUE;
        }
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(this, R.string.bluetooth_open, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, R.string.failed_open_bluetooth, Toast.LENGTH_LONG).show();
                }
                break;
            case  REQUEST_CONNECT_DEVICE:
                if (resultCode == Activity.RESULT_OK) {
                    String address = data.getExtras()
                            .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    con_dev = Eztytopup.getmBTprintService().getDevByMac(address);

                    Eztytopup.getmBTprintService().connect(con_dev);
                }
                break;
        }
    }

    @Override
    public void initView() {

    }

    @Override
    public int getLayout() {
        return R.layout.activity_buyreseller;
    }

    private void sunmiPrint(){
        ThreadPoolManager.getInstance().executeTask(new Runnable() {
            @Override
            public void run() {
                if( Eztytopup.getmBitmap() == null ){
                        /*Change store logo, here...*/
                    Eztytopup.setmBitmap(BitmapFactory.decodeResource(getResources(),
                            R.raw.ezy_for_print));
                }
                try {
                    String code = "JJ4A1 - L120O - 1IG6S - B0O6S";

                                /*logo*/
                    Eztytopup.getWoyouService().setAlignment(1, Eztytopup.getCallback());
                    Eztytopup.getWoyouService().printBitmap(Eztytopup.getmBitmap(),
                            Eztytopup.getCallback());
                                 /* make space*/
                    Eztytopup.getWoyouService().lineWrap(1, Eztytopup.getCallback());
                    Eztytopup.getWoyouService().setFontSize(24, Eztytopup.getCallback());
                    Eztytopup.getWoyouService().printText("Jl. Pangeran Jayakarta No. 129 \n"
                            + "Jakarta Pusat - 10730", Eztytopup.getCallback());
                                 /* make space*/
                    Eztytopup.getWoyouService().lineWrap(2, Eztytopup.getCallback());
                    Eztytopup.getWoyouService().setAlignment(0, Eztytopup.getCallback());
                    Eztytopup.getWoyouService().printOriginalText("  Lorem ipsum dolor sit amet, consectetur adipiscing elit" +
                                    "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.\n",
                            Eztytopup.getCallback());
                                 /* make space*/
                    Eztytopup.getWoyouService().lineWrap(1, Eztytopup.getCallback());
                    Eztytopup.getWoyouService().setAlignment(1, Eztytopup.getCallback());
                    Eztytopup.getWoyouService().printOriginalText("Your Voucher code is : \n",
                            Eztytopup.getCallback());
                                 /* make space*/
                    Eztytopup.getWoyouService().lineWrap(1, Eztytopup.getCallback());
                    Eztytopup.getWoyouService().printTextWithFont(code
                            ,"gh", 32, Eztytopup.getCallback());

                                /* make space*/
                    Eztytopup.getWoyouService().lineWrap(4, Eztytopup.getCallback());

                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private boolean validatePrint(String word){
        if (!word.isEmpty()){
            Eztytopup.getmBTprintService().sendMessage(word, "ENG");
            return true;
        }else {
            return false;
        }
    }
    // TODO unfinish method to change bluetoothprint method
    // if decide use this method, create arraylist string and add word one by one in response.
    private void blutoothPrint2(ArrayList<String> data){
        if (!printImage()) return;
        byte[] cmd = new byte[5];
        cmd[0] = 0x1b;
        cmd[1] = 0x21;

        for (int i = 0; i < data.size(); i++) {
            if (i == 0){ // Nama toko
                cmd[2] |= 0x10;
                Eztytopup.getmBTprintService().write(cmd);
                if (!validatePrint(data.get(i))) return;
            }

            if (i == 1){

            }
        }

    }

    private void bluetoothPrint(Response<VoucherprintResponse> response){
        // logo print
        if (!printImage()) return;
        byte[] cmd = new byte[5];
        cmd[0] = 0x1b;
        cmd[1] = 0x21;
        cmd[2] |= 0x10;
        Eztytopup.getmBTprintService().write(cmd);
        if (!validatePrint(response.body().result.baris01)) return;
        cmd[2] &= 0xEF;
        Eztytopup.getmBTprintService().write(cmd);
        if (!validatePrint(response.body().result.baris02)) return;
        if (!validatePrint(response.body().result.baris03)) return;
        if (!validatePrint(response.body().result.baris04)) return;
        cmd[2] |= 0x10;
        Eztytopup.getmBTprintService().write(cmd);
        if (!validatePrint(response.body().result.baris05)) return;
        cmd[2] &= 0xEF;
        Eztytopup.getmBTprintService().write(cmd);
        if (!validatePrint(response.body().result.baris06)) return;
        if (!validatePrint(response.body().result.baris07)) return;
        if (!validatePrint(response.body().result.baris08)) return;
        if (!validatePrint(response.body().result.baris09)) return;
        if (!validatePrint(response.body().result.baris10)) return;
        if (!validatePrint(response.body().result.baris11)) return;
        if (!validatePrint(response.body().result.baris12)) return;
        if (!validatePrint(response.body().result.baris13)) return;
        cmd[2] |= 0x10;
        Eztytopup.getmBTprintService().write(cmd);
        if (!validatePrint(response.body().result.baris14)) return;
        cmd[2] &= 0xEF;
        Eztytopup.getmBTprintService().write(cmd);
        if (!validatePrint(response.body().result.baris15)) return;
        if (!validatePrint(response.body().result.baris16)) return;
        if (!validatePrint(response.body().result.baris17)) return;
        if (!validatePrint(response.body().result.baris18)) return;
        if (!validatePrint(response.body().result.baris19)) return;
        if (!validatePrint(response.body().result.baris20)) return;
        if (!validatePrint(response.body().result.baris21)) return;
        if (!validatePrint(response.body().result.baris22)) return;
        if (!validatePrint(response.body().result.baris23)) return;
        if (!validatePrint(response.body().result.baris24)) return;
        if (!validatePrint(response.body().result.baris25)) return;
        if (!validatePrint(response.body().result.baris26)) return;
        if (!validatePrint(response.body().result.baris27)) return;
        if (!validatePrint(response.body().result.baris28)) return;
        if (!validatePrint(response.body().result.baris29)) return;
        if (!validatePrint(response.body().result.baris30)) return;
        if (!validatePrint(response.body().result.baris31)) return;
        if (!validatePrint(response.body().result.baris32)) return;
        if (!validatePrint(response.body().result.baris33)) return;
        if (!validatePrint(response.body().result.baris34)) return;
        if (!validatePrint(response.body().result.baris35)) ;
    }
}
