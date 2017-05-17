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
import com.ezytopup.salesapp.api.DetailProductResponse;
import com.ezytopup.salesapp.printhelper.ThreadPoolManager;
import com.ezytopup.salesapp.utility.Constant;
import com.ezytopup.salesapp.utility.Helper;
import com.ezytopup.salesapp.utility.PreferenceUtils;
import com.zj.btsdk.PrintPic;

import java.io.File;
import java.net.HttpURLConnection;
import java.util.ArrayList;

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
    private TextView mSubtotal, mTotal, mQty, info1, info2, info3, buy_desc;
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
        info1 = (TextView) findViewById(R.id.buy_info1);
        info2 = (TextView) findViewById(R.id.buy_info2);
        info3 = (TextView) findViewById(R.id.buy_info3);
        mTotal = (TextView) findViewById(R.id.buy_total);
        mSubtotal = (TextView) findViewById(R.id.buy_subtotal);

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
                if (!Eztytopup.getSunmiDevice()){
                    if (Eztytopup.getmBTprintService().isAvailable()){              // is blutooth exist on that device?
                        if (!Eztytopup.getmBTprintService().isBTopen()){            // is blutooth Enable on that device?
                            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                        }else if (!Eztytopup.getIsPrinterConnected()){              // is bluetooth connected to printer?
                            Intent serverIntent = new Intent(BuyResellerActivity.this,
                                    DeviceListActivity.class);
                            startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                        }else {
                            String code = "JJ4A1 - L120O - 1IG6S - B0O6S";
                            if (!printImage()){ // logo print
                                return;
                            }
                            byte[] cmd = new byte[5];
                            cmd[0] = 0x1b;
                            cmd[1] = 0x21;
                            Eztytopup.getmBTprintService().write(cmd);
                            Eztytopup.getmBTprintService().sendMessage("Jl. Pangeran Jayakarta No. 129 \n"
                                    + "     Jakarta Pusat - 10730  \n", "GBK");

                            Eztytopup.getmBTprintService().write(cmd);
                            Eztytopup.getmBTprintService().sendMessage(productName + "\n", "GBK");

                            Eztytopup.getmBTprintService().write(cmd);
                            Eztytopup.getmBTprintService()
                                    .sendMessage("  Lorem ipsum dolor sit amet, consectetur adipiscing elit" +
                                            "sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.\n", "GBK");

                            cmd[2] &= 0xEF;
                            Eztytopup.getmBTprintService().write(cmd);
                            Eztytopup.getmBTprintService().sendMessage("Your Voucher code is : \n","GBK");
                            cmd[2] = 0x10;
                            cmd[3] = 0x20;
                            Eztytopup.getmBTprintService().write(cmd);
                            Eztytopup.getmBTprintService().sendMessage(Helper.printTextCenter(code) +
                                    "\n", "GBK");
                        }
                    }else {
                        Toast.makeText(this, R.string.bluetooth_notfound, Toast.LENGTH_LONG).show();
                    }
                }else {
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
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                        }
                    });
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
}
