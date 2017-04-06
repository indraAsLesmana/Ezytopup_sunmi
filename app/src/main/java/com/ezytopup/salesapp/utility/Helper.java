package com.ezytopup.salesapp.utility;

import android.app.ProgressDialog;
import android.content.Context;

import com.ezytopup.salesapp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by indraaguslesmana on 4/1/17.
 */

public class Helper {

    private static ProgressDialog sProgressDialog;

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


}
