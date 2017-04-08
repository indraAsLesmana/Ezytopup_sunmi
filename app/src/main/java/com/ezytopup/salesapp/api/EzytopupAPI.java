package com.ezytopup.salesapp.api;

import com.ezytopup.salesapp.utility.Constant;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by indraaguslesmana on 3/31/17.
 */

public interface EzytopupAPI {

    /**
     * Apiary Documentation for ezytoptup
     *
     * */
    @POST("/v1/auth/login")
    Call<Authrequest> login_request1 (@Body Authrequest authrequest);

    /**
     * Live version
     * */

    @POST("WGS_API_login.php?" + Constant.API_URL_GENERALUSAGE)
    Call<Authrequest> login_request (@Body Authrequest authrequest);

    @GET("WGS_API_products.php?" + Constant.API_URL_GENERALUSAGE)
    Call<ProductResponse> getProduct();

    @GET("WGS_API_getHeaderImages.php?" + Constant.API_URL_GENERALUSAGE)
    Call<HeaderimageResponse> getImageHeader();

    @GET("WGS_API_best_seller_products.php?" + Constant.API_URL_GENERALUSAGE)
    Call<BestSellerResponse> getBestSeller();

    @GET("WGS_API_search_products.php?" + Constant.API_URL_GENERALUSAGE)
    Call<SearchResponse> getSearch();

    @GET("WGS_API_get_detail_products.php?" + Constant.API_URL_GENERALUSAGE)
    Call<DetailProductResponse> getDetailProduct(@Query("id") String productid);

    @GET("WGS_API_panduan_awal.php?" + Constant.API_URL_GENERALUSAGE)
    Call<TutorialResponse> getTutorial();
}
