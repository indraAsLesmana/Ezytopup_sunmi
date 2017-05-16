package com.ezytopup.salesapp.utility;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;

import com.ezytopup.salesapp.Eztytopup;
import com.ezytopup.salesapp.R;
import com.ezytopup.salesapp.api.Authrequest;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.File;
import java.net.HttpURLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by indraaguslesmana on 4/1/17.
 */

public class Helper {

    private static ProgressDialog sProgressDialog;
    private static final String TAG = "Helper";
    /**
     * this method require to complete API parameter, from another developer
     * */
    public static String deviceId(){
        //register device_id
        DateFormat dateFormatter = new SimpleDateFormat("yyyyMMddhhmmss");
        dateFormatter.setLenient(false);
        Date today = new Date();
        String getDeviceDate = dateFormatter.format(today);

        Random r = new Random();
        int getDeviceNum = r.nextInt(99999999 - 10000000) + 10000000;
        return getDeviceDate + String.valueOf(getDeviceNum);
    }

    /**
     * Show progress dialog, can only be called once per tier (show-hide)
     */
    public static void showProgressDialog(Context ctx, int bodyStringId) {
        if(sProgressDialog == null) {
            sProgressDialog = ProgressDialog.show(ctx,
                    ctx.getString(R.string.progress_title_default),
                    ctx.getString(bodyStringId), true, false, null);
        }
    }

    /**
     * Hide current progress dialog and set to NULL
     */
    public static void hideProgressDialog() {
        if(sProgressDialog != null && sProgressDialog.isShowing()) {
            sProgressDialog.dismiss();
            sProgressDialog = null;     // so it can be called in the next showProgressDialog
        }
    }

    public static void synchronizeFCMRegToken(final Context context, String token) {
        String deviceid = null;
        if (token == null) {
            token = FirebaseInstanceId.getInstance().getToken();
            deviceid = PreferenceUtils.getSinglePrefrenceString(context,
                    R.string.settings_def_storeidevice_key);
        }

        Call<Authrequest> skip = Eztytopup.getsAPIService()
                .setLoginskip("email", token, deviceid);
        skip.enqueue(new Callback<Authrequest>() {
            @Override
            public void onResponse(Call<Authrequest> call, Response<Authrequest> response) {
                if (response.isSuccessful() && response.body().status
                        .getCode().equals(String.valueOf(HttpURLConnection.HTTP_CREATED))) {
                    Helper.log(TAG, String.format("userToken %s",
                            response.body().getUser().getAccessToken()), null);
                    PreferenceUtils.setStoreDetail(context,
                            response.body().getUser().getId(),
                            response.body().getUser().getFirstName(),
                            response.body().getUser().getLastName(),
                            response.body().getUser().getEmail(),
                            response.body().getUser().getPhoneNumber(),
                            response.body().getUser().getAccessToken(),
                            response.body().getUser().getImageUser(),
                            response.body().getUser().getSellerShopName(),
                            response.body().getUser().getSellerKasirName(),
                            response.body().getUser().getSellerPhone(),
                            response.body().getUser().getSellerAddress(),
                            response.body().getUser().getSellerWebsite(),
                            response.body().getUser().getSellerLogo(),
                            response.body().getUser().getSellerPrintLogo());
                }
            }

            @Override
            public void onFailure(Call<Authrequest> call, Throwable t) {

            }
        });
    }

    public static String printTextCenter(String src) {
        StringBuilder stringBuilder = new StringBuilder();
        String finalString = null;
        if (src.length() < 30) {
            int n = 30 - src.length();
            int space = n / 2;

            for (int i = 0; i <= space; i++) {
                stringBuilder.append(" ");
            }
            stringBuilder.append(src);
            for (int i = 0; i <= space; i++) {
                stringBuilder.append(" ");
            }
            finalString = stringBuilder.toString();
        }
        return finalString;
    }

    public static void log(String TAG, String message, Throwable throwable) {
        if(Constant.ENABLE_LOG) {
            Log.v(TAG, message, throwable);
        }
    }

    public static void apiSnacbarError(Context context, Throwable t, View view){
        Helper.log(TAG, "onFailure: " + t.getMessage(), null);
        String message = t.getMessage();
        if (t.getMessage().contains("Use JsonReader.setLenient")){
            message = context.getResources().getString(R.string.response_error);
        }
        final Snackbar snackbar = Snackbar.make(view, message,
                Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.dismiss, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
        snackbar.show();
    }

    public static void downloadFile(Context context, String uRl) {
        File direct = new File(Environment.getExternalStorageDirectory()
                + "/Ezytopup");

        if (!direct.exists()) {
            direct.mkdirs();
        }

        @SuppressLint("SdCardPath")
        File file = new File("/mnt/sdcard/Ezytopup/print_logo.jpg");
        if (file.exists()){
            return;
        }else {
            DownloadManager mgr = (DownloadManager) context.getSystemService(Context.DOWNLOAD_SERVICE);

            Uri downloadUri = Uri.parse(uRl);
            DownloadManager.Request request = new DownloadManager.Request(
                    downloadUri);

            request.setAllowedNetworkTypes(
                    DownloadManager.Request.NETWORK_WIFI
                            | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false).setTitle("Ezytopup")
                    .setDescription("Ezytopup resouce folder")
                    .setDestinationInExternalPublicDir("/Ezytopup", "print_logo.jpg");

            mgr.enqueue(request);
        }
    }
}
