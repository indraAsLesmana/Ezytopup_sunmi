package com.ezytopup.salesapp.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ezytopup.salesapp.Eztytopup;
import com.ezytopup.salesapp.R;
import com.ezytopup.salesapp.api.DetailProductResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailproductFragment extends Fragment {
    private ArrayList<DetailProductResponse.Result> results;
    private static final String TAG = "DetailproductFragment";
    private TextView mTotal, mSubtotal;
    private String productId;

    public DetailproductFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_detailproduct, container, false);
        mTotal = (TextView) rootView.findViewById(R.id.buy_total);
        mSubtotal = (TextView) rootView.findViewById(R.id.buy_subtotal);
        Bundle bundle = this.getArguments();
        if (bundle != null){
//            var = bundle.getString()
        }
        results = new ArrayList<>();

        return rootView;
    }

    private void getDetailProduct(){
        Call<DetailProductResponse> product = Eztytopup.getsAPIService().
                getDetailProduct(productId);
        product.enqueue(new Callback<DetailProductResponse>() {
            @Override
            public void onResponse(Call<DetailProductResponse> call,
                                   Response<DetailProductResponse> response) {
                if (response.isSuccessful()){
                    results.addAll(response.body().result);
                    DetailProductResponse.Result r = results.get(0);
                    mTotal.setText(r.getHargaToko());
                    mSubtotal.setText(r.getHargaToko());
                }
            }
            @Override
            public void onFailure(Call<DetailProductResponse> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

}
