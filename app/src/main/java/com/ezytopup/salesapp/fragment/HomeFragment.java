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
import com.ezytopup.salesapp.activity.BuyProductActivity;
import com.ezytopup.salesapp.activity.BuyResellerActivity;
import com.ezytopup.salesapp.activity.CategoryActivity;
import com.ezytopup.salesapp.adapter.RecyclerList_homeAdapter;
import com.ezytopup.salesapp.adapter.SectionListDataAdapter;
import com.ezytopup.salesapp.api.ProductResponse;
import com.ezytopup.salesapp.utility.Helper;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class HomeFragment extends Fragment implements
        RecyclerList_homeAdapter.RecyclerList_homeAdapterListener, SectionListDataAdapter.SectionListDataAdapterListener {

    private ArrayList<ProductResponse.Result> allProductdata;
    private RecyclerView my_recycler_view;
    private RecyclerList_homeAdapter adapter;
    private static final String TAG = "HomeFragment";
    private View rootView;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        allProductdata = new ArrayList<>();
        getProduct();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_generallist, container, false);

        my_recycler_view = (RecyclerView) rootView.findViewById(R.id.home_recylerview);
        my_recycler_view.setHasFixedSize(true);
        my_recycler_view.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        adapter = new RecyclerList_homeAdapter(getContext(), allProductdata, this, this);
        my_recycler_view.setAdapter(adapter);

        return rootView;
    }

    private void getProduct() {
        Call<ProductResponse> call = Eztytopup.getsAPIService().getProduct();
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call,
                                   Response<ProductResponse> response) {
                if (response.isSuccessful()){
                    allProductdata.addAll(response.body().getResult());
                    adapter.notifyDataSetChanged();
                }
                Log.i(TAG, "onResponse: " + response.message());
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Helper.apiSnacbarError(getContext(), t, rootView);
            }
        });
    }

    @Override
    public void onMoreClick(String categoryName, String categoryId) {
        CategoryActivity.start(getActivity(), categoryName, categoryId);
    }

    @Override
    public void onCardClick(ProductResponse.Product itemProduct) {
        if (!Eztytopup.getIsUserReseller()){
            BuyProductActivity.start(getActivity(),
                    itemProduct.getProductId(),
                    itemProduct.getProductName(),
                    itemProduct.getImageUrl(),
                    itemProduct.getBackgroundImageUrl(),
                    itemProduct.getHargaToko());
        }else {
            BuyResellerActivity.start(getActivity(),
                    itemProduct.getProductId(),
                    itemProduct.getProductName(),
                    itemProduct.getImageUrl(),
                    itemProduct.getBackgroundImageUrl(),
                    itemProduct.getHargaToko());
        }
    }
}
