package com.ezytopup.salesapp.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
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
import com.ezytopup.salesapp.activity.BuyProductActivity;
import com.ezytopup.salesapp.adapter.RecyclerList_favoriteAdapter;
import com.ezytopup.salesapp.api.BestSellerResponse;
import com.ezytopup.salesapp.utility.Helper;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment implements
        RecyclerList_favoriteAdapter.RecyclerList_favoriteAdapterlistener{

    private ArrayList<BestSellerResponse.Product> AllFavoritedata;
    private RecyclerList_favoriteAdapter adapter;
    private static final String TAG = "FavoriteFragment";
    private View rootView;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AllFavoritedata = new ArrayList<>();
        getFavProduct();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_generallist, container, false);
        RecyclerView recycler_view = (RecyclerView) rootView.findViewById(R.id.home_recylerview);
        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        adapter = new RecyclerList_favoriteAdapter(getContext(), AllFavoritedata, FavoriteFragment.this);
        recycler_view.setAdapter(adapter);
        return  rootView;
    }

    private void getFavProduct() {

        Call<BestSellerResponse> call = Eztytopup.getsAPIService().getBestSeller();
        call.enqueue(new Callback<BestSellerResponse>() {
            @Override
            public void onResponse(Call<BestSellerResponse> call, Response<BestSellerResponse> response) {
                if (response.isSuccessful() &&
                        response.body().status.getCode()
                                .equals(String.valueOf(HttpURLConnection.HTTP_OK))){
                    AllFavoritedata.addAll(response.body().getProducts());
                    adapter.notifyDataSetChanged();
                }else {
                    Toast.makeText(getContext(), response.body().status.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BestSellerResponse> call, Throwable t) {
                Helper.log(TAG, "onFailure: " + t.getMessage(), null);
                String message = t.getMessage();
                if (t.getMessage().contains("Use JsonReader.setLenient")){
                    message = getResources().getString(R.string.cantreachserver);
                }
                final Snackbar snackbar = Snackbar.make(rootView, message,
                        Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction(R.string.dismiss, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        snackbar.dismiss();
                    }
                }).show();
            }
        });
    }

    @Override
    public void onCardclick(BestSellerResponse.Product product) {
        BuyProductActivity.start(getActivity(),
                product.getProductId(),
                product.getProductName(),
                product.getReviewUrl(),
                product.getBackgroundImageUrl(),
                product.getPrice());
    }
}
