package com.ezytopup.salesapp;

import android.util.Log;

import com.ezytopup.salesapp.api.EzytopupAPI;
import com.ezytopup.salesapp.utility.Constant;
import com.ezytopup.salesapp.utility.Helper;
import com.ezytopup.salesapp.utility.PreferenceUtils;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        Helper.synchronizeFCMRegToken(this, token);
    }
}