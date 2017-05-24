package com.ezytopup.salesapp.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ezytopup.salesapp.Eztytopup;
import com.ezytopup.salesapp.R;
import com.ezytopup.salesapp.activity.BuyProductActivity;
import com.ezytopup.salesapp.activity.BuyResellerActivity;
import com.ezytopup.salesapp.adapter.RecyclerList_searchAdapter;
import com.ezytopup.salesapp.api.BestSellerResponse;
import com.ezytopup.salesapp.api.SearchResponse;
import com.ezytopup.salesapp.utility.Helper;

import java.net.HttpURLConnection;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment implements
        RecyclerList_searchAdapter.RecyclerList_searchAdapterlistener{

    private ArrayList<BestSellerResponse.Product> AllFavoritedata;
    private RecyclerList_searchAdapter adapter;
    private static final String TAG = "SearchFragment";
    private FrameLayout container_list;

    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AllFavoritedata = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_generallist, container, false);

        AllFavoritedata = new ArrayList<>();

        LinearLayout searchBar = (LinearLayout) rootView.findViewById(R.id.search_field);
        RecyclerView recycler_view = (RecyclerView) rootView.findViewById(R.id.home_recylerview);
        container_list = (FrameLayout) rootView.findViewById(R.id.container_list);

        searchBar.setVisibility(View.VISIBLE);
        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false));
        adapter = new RecyclerList_searchAdapter(getContext(), AllFavoritedata, this);

        recycler_view.setAdapter(adapter);
        final EditText seachBar = (EditText) rootView.findViewById(R.id.text_search);

        seachBar.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    String productSearch = seachBar.getText().toString();
                    getSearchProduct(productSearch);

                    Helper.hideSoftKeyboard(container_list);
                    return true;
                }
                return false;
            }
        });

        if (Eztytopup.getSunmiDevice()){
            Helper.setImmersivebyKeyboard(rootView);
        }
        return rootView;
    }

    private void getSearchProduct(String productName) {
        Call<SearchResponse> searchResult = Eztytopup.getsAPIService().getSearch(productName);
        searchResult.enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                if (response.isSuccessful()
                        && response.body().status.getCode()
                                .equals(String.valueOf(HttpURLConnection.HTTP_OK))){

                    if (response.body().products.size() == 0){
                        Helper.snacbarError(R.string.result_null, container_list);
                        return;
                    }

                    AllFavoritedata.clear();
                    AllFavoritedata.addAll(response.body().products);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), response.body().status.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                Helper.apiSnacbarError(getContext(), t, container_list);
            }
        });
    }

    @Override
    public void onCardclick(BestSellerResponse.Product product) {
        if (!Eztytopup.getIsUserReseller()){
            BuyProductActivity.start(getActivity(),
                    product.getProductId(),
                    product.getProductName(),
                    product.getReviewUrl(),
                    product.getBackgroundImageUrl(),
                    product.getPrice());
        }else {
            BuyResellerActivity.start(getActivity(),
                    product.getProductId(),
                    product.getProductName(),
                    product.getReviewUrl(),
                    product.getBackgroundImageUrl(),
                    product.getPrice());
        }
    }
}
