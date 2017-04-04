package com.ezytopup.salesapp.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.ezytopup.salesapp.Eztytopup;
import com.ezytopup.salesapp.R;
import com.ezytopup.salesapp.activity.MainActivity;
import com.ezytopup.salesapp.adapter.RecyclerListAdapter;
import com.ezytopup.salesapp.api.ProductResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class HomeFragment extends Fragment {


    private ArrayList<ProductResponse.Result> allProductdata;
    private RecyclerView my_recycler_view;
    private RecyclerListAdapter adapter;
    private static final String TAG = "HomeFragment";
    private SliderLayout headerImages;


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
        View rootView =  inflater.inflate(R.layout.fragment_home, container, false);

        my_recycler_view = (RecyclerView) rootView.findViewById(R.id.home_recylerview);
        my_recycler_view.setHasFixedSize(true);
        my_recycler_view.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        adapter = new RecyclerListAdapter(getContext(), allProductdata);
        my_recycler_view.setAdapter(adapter);

        headerImages = (SliderLayout) rootView.findViewById(R.id.slider);


//        if (getImage()) headerImages.setVisibility(View.VISIBLE);
        return rootView;
    }

    private void getProduct() {

        Call<ProductResponse> call = Eztytopup.getsAPIService().getProduct();
        call.enqueue(new Callback<ProductResponse>() {
            @Override
            public void onResponse(Call<ProductResponse> call, Response<ProductResponse> response) {
                if (response.isSuccessful()){
                    allProductdata.addAll(response.body().getResult());
                    adapter.notifyDataSetChanged();
                }
                Log.i(TAG, "onResponse: " + response.message());
            }

            @Override
            public void onFailure(Call<ProductResponse> call, Throwable t) {
                Log.i(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private boolean getImage() {

        TextSliderView textSliderView = new TextSliderView(getActivity());
        // initialize a SliderLayout
        textSliderView
                .image(R.drawable.header1)
                .setScaleType(BaseSliderView.ScaleType.Fit);
        headerImages.addSlider(textSliderView);

        headerImages.setPresetTransformer(SliderLayout.Transformer.Accordion);
        headerImages.setPresetTransformer(SliderLayout.Transformer.ZoomOut);
        headerImages.setIndicatorVisibility(PagerIndicator.IndicatorVisibility.Invisible);

        return true;
    }


}
