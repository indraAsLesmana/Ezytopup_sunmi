package com.ezytopup.salesapp.utility;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

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
     * This internal function to reduce redundancy showToast function
     */
    private static void initToast(Context context, Toast toast) {
        ViewGroup toastLayout = (ViewGroup)toast.getView();
        TextView toastTextView = (TextView)toastLayout.getChildAt(0);
        float textSize = context.getResources().getDimension(R.dimen.toast_text_size);
        toastTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        toastTextView.setGravity(Gravity.CENTER);
    }

    /**
     * Show toast from resource string
     */
    public static void showToast(Context ctx, int res, boolean needLong) {
        Toast toast = Toast.makeText(ctx, res, needLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
        initToast(ctx, toast);
        toast.show();
    }

    /**
     * Show toast from string
     */
    public static void showToast(Context ctx, String str, boolean needLong) {
        Toast toast = Toast.makeText(ctx, str, needLong ? Toast.LENGTH_LONG : Toast.LENGTH_SHORT);
        initToast(ctx, toast);
        toast.show();
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
    /**
     * Show soft keyboard for given view
     */
    public static void showSoftKeyboard(View view) {
        if(view == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager)
                view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    /**
     * Show soft keyboard for given view
     */
    public static void hideSoftKeyboard(View view) {
        if(view == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager)
                view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
