package com.ezytopup.salesapp.fragment;


import android.net.Network;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ezytopup.salesapp.Eztytopup;
import com.ezytopup.salesapp.R;
import com.ezytopup.salesapp.adapter.Recyclerlist_HistoryAdapter;
import com.ezytopup.salesapp.api.TransactionHistoryResponse;
import com.ezytopup.salesapp.utility.PreferenceUtils;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.HTTP;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    private ArrayList<TransactionHistoryResponse.Result> Allhistory;
    private Recyclerlist_HistoryAdapter adapter;
    private static final String TAG = "FavoriteFragment";
    private View rootView;

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
        RecyclerView recycler_view = (RecyclerView) rootView.findViewById(R.id.home_recylerview);
        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        adapter = new Recyclerlist_HistoryAdapter(getContext(), Allhistory);
        recycler_view.setAdapter(adapter);
        /*int uid = PreferenceUtils.getSinglePrefrenceInt(getContext(), R.string.settings_def_uid);
        if ( uid != 0) getHistory(uid);*/
//        String token = "4d0d9a51f6d19eed7aceccbdee98440e94543b6edf9c152c8365a2fee60a1ed030437bf1786d8674360fac4dab60eb06";
        getHistory(1485);
        return  rootView;
    }

    private void getHistory(int customerId) {
        Call<TransactionHistoryResponse> history = Eztytopup.getsAPIService().getHistory(customerId);
        history.enqueue(new Callback<TransactionHistoryResponse>() {
            @Override
            public void onResponse(Call<TransactionHistoryResponse> call,
                                   Response<TransactionHistoryResponse> response) {
                if (response.isSuccessful() &&
                        response.body().status.getCode()
                                .equals(String.valueOf(HttpURLConnection.HTTP_OK))){
                    Allhistory.addAll(response.body().result);
                    adapter.notifyDataSetChanged();

                }else {
                    Toast.makeText(getContext(), response.body().status.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TransactionHistoryResponse> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t.getMessage());

            }
        });
    }

}
