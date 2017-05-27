package com.ezytopup.salesapp.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ezytopup.salesapp.Eztytopup;
import com.ezytopup.salesapp.R;
import com.ezytopup.salesapp.activity.BuyResellerActivity;
import com.ezytopup.salesapp.activity.DeviceListActivity;
import com.ezytopup.salesapp.adapter.Recyclerlist_HistoryAdapter;
import com.ezytopup.salesapp.api.ServertimeResponse;
import com.ezytopup.salesapp.api.TransactionHistoryResponse;
import com.ezytopup.salesapp.printhelper.ThreadPoolManager;
import com.ezytopup.salesapp.utility.Constant;
import com.ezytopup.salesapp.utility.Helper;
import com.ezytopup.salesapp.utility.PermissionHelper;
import com.ezytopup.salesapp.utility.PreferenceUtils;
import com.zj.btsdk.PrintPic;

import java.io.File;
import java.net.HttpURLConnection;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment implements
        Recyclerlist_HistoryAdapter.Recyclerlist_HistoryAdapterlistener,
        BuyResellerActivity.BuynowInterface {

    private ArrayList<TransactionHistoryResponse.Result> Allhistory;
    private Recyclerlist_HistoryAdapter adapter;
    private static final String TAG = "FavoriteFragment";
    private View rootView;
    private static final int REQUEST_CONNECT_DEVICE = 1;
    private static final int REQUEST_ENABLE_BT = 2;
    private String uid, token;
    private View view_nodatafound;

    public HistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Allhistory = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_generallist, container, false);

        view_nodatafound = rootView.findViewById(R.id.view_nodatafound);
        RecyclerView recycler_view = (RecyclerView) rootView.findViewById(R.id.home_recylerview);
        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));

        adapter = new Recyclerlist_HistoryAdapter(getContext(), Allhistory, HistoryFragment.this);
        recycler_view.setAdapter(adapter);

        uid = PreferenceUtils.getSinglePrefrenceString(getContext(),
                R.string.settings_def_uid_key);
        token = PreferenceUtils.getSinglePrefrenceString(getContext(),
                R.string.settings_def_storeaccess_token_key);

        if (!uid.equals(Constant.PREF_NULL) && !token.equals(Constant.PREF_NULL))
            getHistory(token, uid);

        BuyResellerActivity.buynowListener(this);
        return rootView;
    }

    private void getHistory(String token, String customerId) {
        Call<TransactionHistoryResponse> history = Eztytopup.getsAPIService().getHistory(token,
                customerId);
        history.enqueue(new Callback<TransactionHistoryResponse>() {
            @Override
            public void onResponse(Call<TransactionHistoryResponse> call,
                                   Response<TransactionHistoryResponse> response) {
                if (response.isSuccessful() &&
                        response.body().status.getCode()
                                .equals(String.valueOf(HttpURLConnection.HTTP_OK))){

                    if (response.body().result.size() == 0){
                        Allhistory.clear();
                        adapter.notifyDataSetChanged();
                        view_nodatafound.setVisibility(View.VISIBLE);
                    }else {
                        view_nodatafound.setVisibility(View.GONE);
                        Allhistory.clear();
                        Allhistory.addAll(response.body().result);
                        adapter.notifyDataSetChanged();
                    }
                }else {
                    Toast.makeText(getContext(), response.body().status.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TransactionHistoryResponse> call, Throwable t) {
                Helper.apiSnacbarError(getContext(), t, rootView);
            }
        });
    }


    @Override
    public void onReprintClick(TransactionHistoryResponse.Result historyItem) {
       validatePrint();
    }

    private void validatePrint(){
        Call<ServertimeResponse> serverTime = Eztytopup.getsAPIService().getServertime();
        serverTime.enqueue(new Callback<ServertimeResponse>() {
            @Override
            public void onResponse(Call<ServertimeResponse> call,
                                   Response<ServertimeResponse> response) {
                if (response.isSuccessful() && response.body().status.getCode()
                        .equals(String.valueOf(HttpURLConnection.HTTP_OK))) {
                    //get servertime
                    if (PreferenceUtils.getLastProduct() == null){
                        Toast.makeText(getContext(), R.string.cant_getlastproduct,
                                Toast.LENGTH_SHORT).show();
                        return;
                    }

                    String serverTime = response.body().result.getServerTime();
                    String tanggalCetak = PreferenceUtils.getLastProduct().tglCetak;
                    String reprintTime = PreferenceUtils.getLastProduct().reprintTime;
                    if (tanggalCetak.equals("") || reprintTime.equals("") || serverTime.equals("")){
                        Toast.makeText(getContext(), R.string.cant_get_time,
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //validate reprinted time
                    if (Helper.dateCheck(tanggalCetak, reprintTime, serverTime)) {
                        if (!Eztytopup.getSunmiDevice()) {
                            if (!Eztytopup.getmBTprintService().isAvailable()){
                                Toast.makeText(getContext(), R.string.bluetooth_notfound,
                                        Toast.LENGTH_SHORT).show();
                            } else if (!Eztytopup.getmBTprintService().isBTopen()) {
                                // is blutooth Enable on that device?
                                Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                                startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
                            } else if (!Eztytopup.getIsPrinterConnected()) {
                                // is bluetooth connected to printer?
                                Intent serverIntent = new Intent(getContext(),
                                        DeviceListActivity.class);
                                startActivityForResult(serverIntent, REQUEST_CONNECT_DEVICE);
                            } else {
                                bluetoothPrint();
                            }
                        } else {
                            sunmiPrint();
                        }

                    } else {
                        Toast.makeText(getContext(), R.string.reprint_timeout, Toast.LENGTH_SHORT).show();
                        PreferenceUtils.destroyLastProduct();
                    }

                } else {
                    Toast.makeText(getContext(), response.body().status.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ServertimeResponse> call, Throwable t) {
                Helper.apiSnacbarError(getContext(), t, rootView);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    Toast.makeText(getContext(), R.string.bluetooth_open,
                            Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getContext(), R.string.failed_open_bluetooth,
                            Toast.LENGTH_LONG).show();
                }
                break;
            case  REQUEST_CONNECT_DEVICE:
                if (resultCode == Activity.RESULT_OK) {
                    String address = data.getExtras()
                            .getString(DeviceListActivity.EXTRA_DEVICE_ADDRESS);
                    Eztytopup.setCon_dev(Eztytopup.getmBTprintService().getDevByMac(address));
                    Eztytopup.getmBTprintService().connect(Eztytopup.getCon_dev());
                }
                break;
        }
    }

    private boolean validatePrint(String word){
        if (!word.isEmpty()){
            if (!Eztytopup.getSunmiDevice()){
                Eztytopup.getmBTprintService().sendMessage(word, "ENG");
                return true;
            }else {
                try {
                    Eztytopup.getWoyouService().printOriginalText(word+"\n",
                            Eztytopup.getCallback());
                } catch (RemoteException e) {
                    Helper.log(TAG, "Sunmi print error: " + e.getMessage(), null);
                    e.printStackTrace();
                }
                return true;
            }
        }else {
            PreferenceUtils.destroyLastProduct();
            return false;
        }
    }

    private void sunmiPrint(){
        ThreadPoolManager.getInstance().executeTask(new Runnable() {
            @Override
            public void run() {
                try {
                    if (printImage()) {
                        Eztytopup.getWoyouService().setAlignment(1, Eztytopup.getCallback());
                        Eztytopup.getWoyouService().printBitmap(Eztytopup.getmBitmap(),
                                Eztytopup.getCallback());
                        Eztytopup.getWoyouService().lineWrap(1, Eztytopup.getCallback());
                        Eztytopup.getWoyouService().setAlignment(0, Eztytopup.getCallback());
                    }
                    Eztytopup.getWoyouService().setFontSize(20, Eztytopup.getCallback());

                    byte[] cmd = new byte[5];
                    cmd[0] = 0x1b;
                    cmd[1] = 0x21;
                    cmd[2] |= 0x10;
                    Eztytopup.getWoyouService().sendRAWData(cmd, Eztytopup.getCallback());
                    if (!validatePrint(PreferenceUtils.getLastProduct().getBaris01())) return;
                    cmd[2] &= 0xEF;
                    Eztytopup.getWoyouService().sendRAWData(cmd, Eztytopup.getCallback());
                    if (!validatePrint(PreferenceUtils.getLastProduct().getBaris02())) return;
                    if (!validatePrint(PreferenceUtils.getLastProduct().getBaris03())) return;
                    if (!validatePrint(PreferenceUtils.getLastProduct().getBaris04())) return;
                    cmd[2] |= 0x10;
                    Eztytopup.getWoyouService().sendRAWData(cmd, Eztytopup.getCallback());
                    if (!validatePrint(PreferenceUtils.getLastProduct().getBaris05())) return;
                    cmd[2] &= 0xEF;
                    Eztytopup.getWoyouService().sendRAWData(cmd, Eztytopup.getCallback());
                    if (!validatePrint(PreferenceUtils.getLastProduct().getBaris06())) return;
                    if (!validatePrint(PreferenceUtils.getLastProduct().getBaris07())) return;
                    if (!validatePrint(PreferenceUtils.getLastProduct().getBaris08())) return;
                    if (!validatePrint(PreferenceUtils.getLastProduct().getBaris09())) return;
                    if (!validatePrint(PreferenceUtils.getLastProduct().getBaris10())) return;
                    if (!validatePrint(PreferenceUtils.getLastProduct().getBaris11())) return;
                    if (!validatePrint(PreferenceUtils.getLastProduct().getBaris12())) return;
                    if (!validatePrint(PreferenceUtils.getLastProduct().getBaris13())) return;
                    cmd[2] |= 0x10;
                    Eztytopup.getWoyouService().sendRAWData(cmd, Eztytopup.getCallback());
                    if (!validatePrint(PreferenceUtils.getLastProduct().getBaris14())) return;
                    cmd[2] &= 0xEF;
                    Eztytopup.getWoyouService().sendRAWData(cmd, Eztytopup.getCallback());
                    if (!validatePrint(PreferenceUtils.getLastProduct().getBaris15())) return;
                    if (!validatePrint(PreferenceUtils.getLastProduct().getBaris16())) return;
                    if (!validatePrint(PreferenceUtils.getLastProduct().getBaris17())) return;
                    if (!validatePrint(PreferenceUtils.getLastProduct().getBaris18())) return;
                    if (!validatePrint(PreferenceUtils.getLastProduct().getBaris19())) return;
                    if (!validatePrint(PreferenceUtils.getLastProduct().getBaris20())) return;
                    if (!validatePrint(PreferenceUtils.getLastProduct().getBaris21())) return;
                    if (!validatePrint(PreferenceUtils.getLastProduct().getBaris22())) return;
                    if (!validatePrint(PreferenceUtils.getLastProduct().getBaris23())) return;
                    if (!validatePrint(PreferenceUtils.getLastProduct().getBaris24())) return;
                    if (!validatePrint(PreferenceUtils.getLastProduct().getBaris25())) return;
                    if (!validatePrint(PreferenceUtils.getLastProduct().getBaris26())) return;
                    if (!validatePrint(PreferenceUtils.getLastProduct().getBaris27())) return;
                    if (!validatePrint(PreferenceUtils.getLastProduct().getBaris28())) return;
                    if (!validatePrint(PreferenceUtils.getLastProduct().getBaris29())) return;
                    if (!validatePrint(PreferenceUtils.getLastProduct().getBaris30())) return;
                    if (!validatePrint(PreferenceUtils.getLastProduct().getBaris31())) return;
                    if (!validatePrint(PreferenceUtils.getLastProduct().getBaris32())) return;
                    if (!validatePrint(PreferenceUtils.getLastProduct().getBaris33())) return;
                    if (!validatePrint(PreferenceUtils.getLastProduct().getBaris34())) return;
                    if (!validatePrint(PreferenceUtils.getLastProduct().getBaris35())) ;

                    Eztytopup.getWoyouService().lineWrap(1, Eztytopup.getCallback());
                    PreferenceUtils.destroyLastProduct();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void bluetoothPrint(){
        // logo print
        printImage();
        byte[] cmd = new byte[5];
        cmd[0] = 0x1b;
        cmd[1] = 0x21;
        cmd[2] |= 0x10;
        Eztytopup.getmBTprintService().write(cmd);
        if (!validatePrint(PreferenceUtils.getLastProduct().getBaris01())) return;
        cmd[2] &= 0xEF;
        Eztytopup.getmBTprintService().write(cmd);
        if (!validatePrint(PreferenceUtils.getLastProduct().getBaris02())) return;
        if (!validatePrint(PreferenceUtils.getLastProduct().getBaris03())) return;
        if (!validatePrint(PreferenceUtils.getLastProduct().getBaris04())) return;
        cmd[2] |= 0x10;
        Eztytopup.getmBTprintService().write(cmd);
        if (!validatePrint(PreferenceUtils.getLastProduct().getBaris05())) return;
        cmd[2] &= 0xEF;
        Eztytopup.getmBTprintService().write(cmd);
        if (!validatePrint(PreferenceUtils.getLastProduct().getBaris06())) return;
        if (!validatePrint(PreferenceUtils.getLastProduct().getBaris07())) return;
        if (!validatePrint(PreferenceUtils.getLastProduct().getBaris08())) return;
        if (!validatePrint(PreferenceUtils.getLastProduct().getBaris09())) return;
        if (!validatePrint(PreferenceUtils.getLastProduct().getBaris10())) return;
        if (!validatePrint(PreferenceUtils.getLastProduct().getBaris11())) return;
        if (!validatePrint(PreferenceUtils.getLastProduct().getBaris12())) return;
        if (!validatePrint(PreferenceUtils.getLastProduct().getBaris13())) return;
        cmd[2] |= 0x10;
        Eztytopup.getmBTprintService().write(cmd);
        if (!validatePrint(PreferenceUtils.getLastProduct().getBaris14())) return;
        cmd[2] &= 0xEF;
        Eztytopup.getmBTprintService().write(cmd);
        if (!validatePrint(PreferenceUtils.getLastProduct().getBaris15())) return;
        if (!validatePrint(PreferenceUtils.getLastProduct().getBaris16())) return;
        if (!validatePrint(PreferenceUtils.getLastProduct().getBaris17())) return;
        if (!validatePrint(PreferenceUtils.getLastProduct().getBaris18())) return;
        if (!validatePrint(PreferenceUtils.getLastProduct().getBaris19())) return;
        if (!validatePrint(PreferenceUtils.getLastProduct().getBaris20())) return;
        if (!validatePrint(PreferenceUtils.getLastProduct().getBaris21())) return;
        if (!validatePrint(PreferenceUtils.getLastProduct().getBaris22())) return;
        if (!validatePrint(PreferenceUtils.getLastProduct().getBaris23())) return;
        if (!validatePrint(PreferenceUtils.getLastProduct().getBaris24())) return;
        if (!validatePrint(PreferenceUtils.getLastProduct().getBaris25())) return;
        if (!validatePrint(PreferenceUtils.getLastProduct().getBaris26())) return;
        if (!validatePrint(PreferenceUtils.getLastProduct().getBaris27())) return;
        if (!validatePrint(PreferenceUtils.getLastProduct().getBaris28())) return;
        if (!validatePrint(PreferenceUtils.getLastProduct().getBaris29())) return;
        if (!validatePrint(PreferenceUtils.getLastProduct().getBaris30())) return;
        if (!validatePrint(PreferenceUtils.getLastProduct().getBaris31())) return;
        if (!validatePrint(PreferenceUtils.getLastProduct().getBaris32())) return;
        if (!validatePrint(PreferenceUtils.getLastProduct().getBaris33())) return;
        if (!validatePrint(PreferenceUtils.getLastProduct().getBaris34())) return;
        if (!validatePrint(PreferenceUtils.getLastProduct().getBaris35())) ;

        PreferenceUtils.destroyLastProduct();
    }

    @SuppressLint("SdCardPath")
    private Boolean printImage() {
        File file = new File(Constant.DEF_PATH_IMAGEPRINT);
        if (!file.exists() && !PreferenceUtils.getSinglePrefrenceString(getContext(),
                R.string.settings_def_sellerprintlogo_key).equals(Constant.PREF_NULL) &&
                PermissionHelper.isPermissionGranted(getContext(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE})) {
            Helper.downloadFile(getContext(), PreferenceUtils.getSinglePrefrenceString(getContext(),
                    R.string.settings_def_sellerprintlogo_key));
            Toast.makeText(getContext(), R.string.please_wait_imageprint,
                    Toast.LENGTH_SHORT).show();
            return Boolean.FALSE;
        } else {
            if (!Eztytopup.getSunmiDevice()){
                byte[] sendData = null;
                PrintPic pg = new PrintPic();
                pg.initCanvas(384);
                pg.initPaint();
                pg.drawImage(100, 0, Constant.DEF_PATH_IMAGEPRINT);
                sendData = pg.printDraw();
                Eztytopup.getmBTprintService().write(sendData);

                return Boolean.TRUE;
            }else {
                Bitmap bitmap = BitmapFactory.decodeFile(Constant.DEF_PATH_IMAGEPRINT);
                Eztytopup.setmBitmap(bitmap);

                return  Boolean.TRUE;
            }
        }
    }

    @Override
    public void buyNowClick() {
        getHistory(token, uid);
    }
}
