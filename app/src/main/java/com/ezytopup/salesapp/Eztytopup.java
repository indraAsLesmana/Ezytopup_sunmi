package com.ezytopup.salesapp;

import android.app.Application;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.ezytopup.salesapp.api.EzytopupAPI;
import com.ezytopup.salesapp.utility.Constant;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by indraaguslesmana on 3/31/17.
 */

public class Eztytopup extends Application {

    private static EzytopupAPI sAPIService;
    private static Eztytopup sInstance;
    private static SharedPreferences sPreferences;


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
                                .addHeader("application_id", "Ezy_Apps_WGS")
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
}
