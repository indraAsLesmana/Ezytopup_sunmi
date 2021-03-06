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

    public static void setStoreDetail (Context context, int id, String first_name, String last_name,
                                       String email, String phone_number, String access_token,
                                       String image_user){
        SharedPreferences.Editor editor = Eztytopup.getsPreferences().edit();
        editor.putInt(context.getString(R.string.settings_def_storeid_key), id);
        editor.putString(context.getString(R.string.settings_def_storefirst_name_key), first_name);
        editor.putString(context.getString(R.string.settings_def_storelast_name_key), last_name);
        editor.putString(context.getString(R.string.settings_def_storeemail_key), email);
        editor.putString(context.getString(R.string.settings_def_storephone_number_key), phone_number);
        editor.putString(context.getString(R.string.settings_def_storeaccess_token_key), access_token);
        editor.putString(context.getString(R.string.settings_def_storeimage_user_key), image_user);
        editor.apply();
    }
    public static void destroyUserSession(Context context) {
        SharedPreferences.Editor editor = Eztytopup.getsPreferences().edit();
        editor.remove(context.getString(R.string.settings_def_storeid_key));
        editor.remove(context.getString(R.string.settings_def_storefirst_name_key));
        editor.remove(context.getString(R.string.settings_def_storelast_name_key));
        editor.remove(context.getString(R.string.settings_def_storeemail_key));
        editor.remove(context.getString(R.string.settings_def_storeemail_key));
        editor.remove(context.getString(R.string.settings_def_storeaccess_token_key));
        editor.remove(context.getString(R.string.settings_def_storeimage_user_key));
        editor.apply();
    }

    public static String getSinglePrefrenceString(Context context, int prefereceKeyName){
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
            case R.string.settings_def_storefirst_name_key:
                result = dataPreferece.getString(
                        context.getString(R.string.settings_def_storefirst_name_key),
                        context.getString(R.string.settings_def_storefirst_name_default));
                break;

            case R.string.settings_def_storelast_name_key:
                result = dataPreferece.getString(
                        context.getString(R.string.settings_def_storelast_name_key),
                        context.getString(R.string.settings_def_storelast_name_default));
                break;
            case R.string.settings_def_storeemail_key:
                result = dataPreferece.getString(
                        context.getString(R.string.settings_def_storeemail_key),
                        context.getString(R.string.settings_def_storeemail_default));
                break;
            case R.string.settings_def_storephone_number_key:
                result = dataPreferece.getString(
                        context.getString(R.string.settings_def_storephone_number_key),
                        context.getString(R.string.settings_def_storephone_number_default));
                break;
            case R.string.settings_def_storeaccess_token_key:
                result = dataPreferece.getString(
                        context.getString(R.string.settings_def_storeaccess_token_key),
                        context.getString(R.string.settings_def_storeaccess_token_default));
                break;
            case R.string.settings_def_storeimage_user_key:
                result = dataPreferece.getString(
                        context.getString(R.string.settings_def_storeimage_user_key),
                        context.getString(R.string.settings_def_storeimage_user_default));
                break;

        }
        return result;
    }

    public static int getSinglePrefrenceInt (Context context, int prefereceKeyName){
        int result = 0;
        SharedPreferences dataPreferece = Eztytopup.getsPreferences();
        switch (prefereceKeyName){
            case R.string.settings_def_storeid_key:
                result = dataPreferece.getInt(
                        context.getString(R.string.settings_def_storeid_key), 0);
                break;
        }
        return result;
    }


}
