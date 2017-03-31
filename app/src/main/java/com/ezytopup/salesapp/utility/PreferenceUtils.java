package com.ezytopup.salesapp.utility;

import android.content.Context;
import android.content.SharedPreferences;

import com.ezytopup.salesapp.Eztytopup;
import com.ezytopup.salesapp.R;

/**
 * Created by indraaguslesmana on 3/30/17.
 */

public class PreferenceUtils {

    private final int STORENAME_KEY = R.string.settings_def_storename_key;
    private final int STORELOGO_KEY = R.string.settings_def_storelogo_key;

    public static void setStoreDetail (Context context, String storeName, String logoUrl){
        SharedPreferences.Editor editor = Eztytopup.getsPreferences().edit();
        editor.putString(context.getString(R.string.settings_def_storename_key), storeName);
        editor.putString(context.getString(R.string.settings_def_storelogo_key), logoUrl);
        editor.apply();
    }

    public static String getSinglePrefrence (Context context, int prefereceKeyName){
        String result = null;
        SharedPreferences dataPreferece = Eztytopup.getsPreferences();
        switch (prefereceKeyName){
            case R.string.settings_def_storename_key:
                result = dataPreferece.getString(context.getString(R.string.settings_def_storename_key),
                        context.getString(R.string.settings_def_storename_default));
                break;
            case R.string.settings_def_storelogo_key:
                result = dataPreferece.getString(
                        context.getString(R.string.settings_def_storelogo_key),
                        context.getString(R.string.settings_def_storelogo_default));
                break;
        }
        return result;
    }


}
