package com.ezytopup.salesapp;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import com.ezytopup.salesapp.api.EzytopupAPI;
import com.ezytopup.salesapp.api.PaymentResponse;
import com.ezytopup.salesapp.api.TamplateResponse;
import com.ezytopup.salesapp.utility.Constant;
import com.ezytopup.salesapp.utility.Helper;
import com.ezytopup.salesapp.utility.PreferenceUtils;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import woyou.aidlservice.jiuiv5.ICallback;
import woyou.aidlservice.jiuiv5.IWoyouService;

/**
 * Created by indraaguslesmana on 3/31/17.
 */

public class Eztytopup extends Application {

    private static final String TAG = "Eztytopup";
    private static EzytopupAPI sAPIService;
    private static Eztytopup sInstance;
    private static SharedPreferences sPreferences;

    private static Bitmap mBitmap;
    private static IWoyouService woyouService;
    private static ICallback callback = null;

    private final String HEADER_KEY1 = "application_id";
    private final String HEADER_KEY2 = "Authorize";
    private static ArrayList<String> paymentActiveInfo;
    private static ArrayList<PaymentResponse.PaymentMethod> paymentInternet;
    private static ArrayList<PaymentResponse.PaymentMethod> paymentTransfer;
    private static ArrayList<PaymentResponse.PaymentMethod> paymentCredit;
    private static ArrayList<PaymentResponse.PaymentMethod> paymentWallet;
    private static ArrayList<PaymentResponse.PaymentMethod> paymentActive;
    private static ArrayList<TamplateResponse.Result> tamplateActive;


    @Override
    public void onCreate() {
        super.onCreate();
        sPreferences = getSharedPreferences(getPackageName(), MODE_PRIVATE);
        OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        logging.setLevel(HttpLoggingInterceptor.Level.HEADERS);

        okhttpClientBuilder
                .addInterceptor(logging)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Request request = chain.request().newBuilder()
                                .addHeader(HEADER_KEY1, "Ezy_Apps_WGS")
                                .build();
                        return chain.proceed(request);
                    }
                });

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Constant.API_ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okhttpClientBuilder.build());

        Retrofit retrofit = builder.build();
        sAPIService = retrofit.create(EzytopupAPI.class);
        paymentActive = new ArrayList<>();
        paymentActiveInfo = new ArrayList<>();
        paymentInternet = new ArrayList<>();
        paymentTransfer = new ArrayList<>();
        paymentCredit = new ArrayList<>();
        paymentWallet = new ArrayList<>();
        tamplateActive = new ArrayList<>();
        loadPaymentInfo();
        loadGiftTamplte();
        //initPrint(); disable couse client not usind Sunmi Device for future its must be detect hardware sunmi to automaticly init
        setDeviceId();
    }

    private void loadGiftTamplte() {
        Call<TamplateResponse> tamplate = Eztytopup.getsAPIService().getTamplateGift();
        tamplate.enqueue(new Callback<TamplateResponse>() {
            @Override
            public void onResponse(Call<TamplateResponse> call,
                                   retrofit2.Response<TamplateResponse> response) {
                if (response.isSuccessful() &&
                        response.body().status.getCode()
                                .equals(String.valueOf(HttpURLConnection.HTTP_OK))){
                        tamplateActive.addAll(response.body().result);

                }else {
                    Toast.makeText(Eztytopup.this, response.body().status.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<TamplateResponse> call, Throwable t) {

            }
        });
    }

    private void setDeviceId() {
        if (PreferenceUtils.getSinglePrefrenceString(this,
                R.string.settings_def_storeidevice_key).equals(Constant.DEVICEID_NULL)){
            PreferenceUtils.setDeviceId(this, Helper.deviceId());
        }
    }


    private void loadPaymentInfo() {
        Call<PaymentResponse> payment = Eztytopup.getsAPIService().getCheckactivePayment();
        payment.enqueue(new Callback<PaymentResponse>() {
            @Override
            public void onResponse(Call<PaymentResponse> call,
                                   retrofit2.Response<PaymentResponse> response) {
                if (response.isSuccessful() &&
                        response.body().status.getCode()
                                .equals(String.valueOf(HttpURLConnection.HTTP_OK))){
                    paymentActive.addAll(response.body().paymentMethods);
                    for (PaymentResponse.PaymentMethod activePayment : paymentActive){
                        paymentActiveInfo.add(activePayment.getId());
                        getLoadActivePayment(activePayment.getId());
                    }
                }else {
                    Toast.makeText(Eztytopup.this, response.body().status.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PaymentResponse> call, Throwable t) {

            }
        });
    }

    /* printer connection snipet*/
    private static ServiceConnection connService = new ServiceConnection() {

        @Override
        public void onServiceDisconnected(ComponentName name) {

            woyouService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            woyouService = IWoyouService.Stub.asInterface(service);
        }
    };
    /* initial code to AIDL file*/
    private void initPrint(){
        callback = new ICallback.Stub() {

            @Override
            public void onRunResult(final boolean success) throws RemoteException {
            }

            @Override
            public void onReturnString(final String value) throws RemoteException {
                Log.i(TAG,"printlength:" + value + "\n");
            }

            @Override
            public void onRaiseException(int code, final String msg) throws RemoteException {
                Log.i(TAG,"onRaiseException: " + msg);

            }
        };

        Intent intent=new Intent();
        intent.setPackage("woyou.aidlservice.jiuiv5");
        intent.setAction("woyou.aidlservice.jiuiv5.IWoyouService");
        startService(intent);
        bindService(intent, connService, Context.BIND_AUTO_CREATE);
    }

    private void getLoadActivePayment(String id) {
        switch (id) {
            case Constant.INTERNET_BANK:
                Call<PaymentResponse> payInternet = Eztytopup.getsAPIService().getPaymentInetBanking();
                payInternet.enqueue(new Callback<PaymentResponse>() {
                    @Override
                    public void onResponse(Call<PaymentResponse> call, retrofit2.Response<PaymentResponse> response) {
                        if (response.isSuccessful() &&
                                response.body().status.getCode()
                                        .equals(String.valueOf(HttpURLConnection.HTTP_OK))){
                            paymentInternet.clear();
                            paymentInternet.addAll(response.body().paymentMethods);
                        }else {
                            Toast.makeText(Eztytopup.this, response.body().status.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<PaymentResponse> call, Throwable t) {

                    }
                });

                break;
            case Constant.BANK_TRANSFER:
                Call<PaymentResponse> payTransfer = Eztytopup.getsAPIService().getPaymentBankTransfer();
                payTransfer.enqueue(new Callback<PaymentResponse>() {
                    @Override
                    public void onResponse(Call<PaymentResponse> call, retrofit2.Response<PaymentResponse> response) {
                        if (response.isSuccessful() &&
                                response.body().status.getCode()
                                        .equals(String.valueOf(HttpURLConnection.HTTP_OK))){
                            paymentTransfer.clear();
                            paymentTransfer.addAll(response.body().paymentMethods);
                        }else {
                            Toast.makeText(Eztytopup.this, response.body().status.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<PaymentResponse> call, Throwable t) {

                    }
                });

                break;
            case Constant.CREADIT_CARD:
                Call<PaymentResponse> payCredit = Eztytopup.getsAPIService().getPaymentCreditcard();
                payCredit.enqueue(new Callback<PaymentResponse>() {
                    @Override
                    public void onResponse(Call<PaymentResponse> call, retrofit2.Response<PaymentResponse> response) {
                        if (response.isSuccessful() &&
                                response.body().status.getCode()
                                        .equals(String.valueOf(HttpURLConnection.HTTP_OK))){
                            paymentCredit.clear();
                            paymentCredit.addAll(response.body().paymentMethods);
                        }else {
                            Toast.makeText(Eztytopup.this, response.body().status.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<PaymentResponse> call, Throwable t) {

                    }
                });

                break;
            case Constant.EZYTOPUP_WALLET:
                Call<PaymentResponse> payWallet = Eztytopup.getsAPIService().getPaymentEzyWallet();
                payWallet.enqueue(new Callback<PaymentResponse>() {
                    @Override
                    public void onResponse(Call<PaymentResponse> call, retrofit2.Response<PaymentResponse> response) {
                        if (response.isSuccessful() &&
                                response.body().status.getCode()
                                        .equals(String.valueOf(HttpURLConnection.HTTP_OK))){
                            paymentWallet.clear();
                            paymentWallet.addAll(response.body().paymentMethods);
                        }else {
                            Toast.makeText(Eztytopup.this, response.body().status.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<PaymentResponse> call, Throwable t) {

                    }
                });

                break;
        }
    }

    public static ArrayList<TamplateResponse.Result> getTamplateActive() {
        return tamplateActive;
    }

    public static void setTamplateActive(ArrayList<TamplateResponse.Result> tamplateActive) {
        Eztytopup.tamplateActive = tamplateActive;
    }

    public static ArrayList<PaymentResponse.PaymentMethod> getPaymentActive() {
        return paymentActive;
    }

    public static ArrayList<PaymentResponse.PaymentMethod> getPaymentInternet() {
        return paymentInternet;
    }

    public static ArrayList<PaymentResponse.PaymentMethod> getPaymentTransfer() {
        return paymentTransfer;
    }

    public static ArrayList<PaymentResponse.PaymentMethod> getPaymentCredit() {
        return paymentCredit;
    }

    public static ArrayList<PaymentResponse.PaymentMethod> getPaymentWallet() {
        return paymentWallet;
    }

    public static ArrayList<String> getPaymentActiveInfo() {
        return paymentActiveInfo;
    }

    public static void setPaymentActiveInfo(ArrayList<String> paymentActiveInfo) {
        Eztytopup.paymentActiveInfo = paymentActiveInfo;
    }

    public static EzytopupAPI getsAPIService() {
        return sAPIService;
    }

    public static SharedPreferences getsPreferences() {
        return sPreferences;
    }

    public static Eztytopup getsInstance() {
        return sInstance;
    }

    public static Bitmap getmBitmap() {
        return mBitmap;
    }

    public static void setmBitmap(Bitmap mBitmap) {
        Eztytopup.mBitmap = mBitmap;
    }

    public static IWoyouService getWoyouService() {
        return woyouService;
    }

    public static ICallback getCallback() {
        return callback;
    }

    public static ServiceConnection getConnService() {
        return connService;
    }
}
