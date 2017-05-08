package com.ezytopup.salesapp;

import com.ezytopup.salesapp.utility.Helper;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        String token = FirebaseInstanceId.getInstance().getToken();
        Helper.synchronizeFCMRegToken(this, token);
    }
}