package com.ezytopup.salesapp;

import android.app.Application;

import com.ezytopup.salesapp.api.EzytopupAPI;
import com.ezytopup.salesapp.utility.Constant;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by indraaguslesmana on 3/31/17.
 */

public class Eztytopup extends Application {

    private static EzytopupAPI sAPIService;
    private static Eztytopup sInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        okhttpClientBuilder.addInterceptor(logging);

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(Constant.API_APIARY_ENDPOINT)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okhttpClientBuilder.build());

        Retrofit retrofit = builder.build();
        sAPIService = retrofit.create(EzytopupAPI.class);

    }

    public static EzytopupAPI getsAPIService() {
        return sAPIService;
    }

    public static Eztytopup getsInstance() {
        return sInstance;
    }
}
