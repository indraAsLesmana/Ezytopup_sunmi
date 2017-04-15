package com.ezytopup.salesapp.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ezytopup.salesapp.Eztytopup;
import com.ezytopup.salesapp.R;
import com.ezytopup.salesapp.adapter.Recyclerlist_HistoryAdapter;
import com.ezytopup.salesapp.api.TransactionHistoryResponse;
import com.ezytopup.salesapp.utility.PreferenceUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        getHistory(1485);
        return  rootView;
    }

    private void getHistory(int customerId) {
        Call<TransactionHistoryResponse> history = Eztytopup.getsAPIService().getHistory(customerId);
        history.enqueue(new Callback<TransactionHistoryResponse>() {
            @Override
            public void onResponse(Call<TransactionHistoryResponse> call,
                                   Response<TransactionHistoryResponse> response) {
                if (response.isSuccessful()){
                    Allhistory.addAll(response.body().result);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<TransactionHistoryResponse> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t.getMessage());

            }
        });
    }

}
