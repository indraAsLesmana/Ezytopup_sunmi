package com.ezytopup.salesapp.fragment;


import android.os.Bundle;
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
import com.ezytopup.salesapp.adapter.Recyclerlist_HistoryAdapter;
import com.ezytopup.salesapp.api.TransactionHistoryResponse;
import com.ezytopup.salesapp.utility.Constant;
import com.ezytopup.salesapp.utility.Helper;
import com.ezytopup.salesapp.utility.PreferenceUtils;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment implements
        Recyclerlist_HistoryAdapter.Recyclerlist_HistoryAdapterlistener{

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
        adapter = new Recyclerlist_HistoryAdapter(getContext(), Allhistory, HistoryFragment.this);
        recycler_view.setAdapter(adapter);
        String uid = PreferenceUtils.getSinglePrefrenceString(getContext(),
                R.string.settings_def_uid_key);
        String token = PreferenceUtils.getSinglePrefrenceString(getContext(),
                R.string.settings_def_storeaccess_token_key);
        if (!uid.equals(Constant.PREF_NULL) && !token.equals(Constant.PREF_NULL))
            getHistory(token, uid);
        return  rootView;
    }

    private void getHistory(String token, String customerId) {
        Call<TransactionHistoryResponse> history = Eztytopup.getsAPIService().getHistory(token, customerId);
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
                Helper.apiSnacbarError(getContext(), t, rootView);
            }
        });
    }

    @Override
    public void onReprintClick(TransactionHistoryResponse.Result historyItem) {
        Toast.makeText(getContext(), historyItem.getTotal(), Toast.LENGTH_SHORT).show();
    }
}
