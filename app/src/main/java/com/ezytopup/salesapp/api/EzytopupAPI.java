package com.ezytopup.salesapp.api;

import com.ezytopup.salesapp.utility.Constant;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

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
    /* login */
    @POST("WGS_API_login.php?" + Constant.API_URL_PARAM1 + Constant.API_URL_PARAM1_VALUE + Constant.API_URL_PARAM2 + Constant.API_URL_PARAM2_VALUE)
    Call<Authrequest> login_request (@Body Authrequest authrequest);
    /* product */
    @GET("WGS_API_products.php?" + Constant.API_URL_PARAM1 + Constant.API_URL_PARAM1_VALUE + Constant.API_URL_PARAM2 + Constant.API_URL_PARAM2_VALUE)
    Call<ProductResponse> getProduct();
    /*header image*/
    @GET("WGS_API_getHeaderImages.php?" + Constant.API_URL_PARAM1 + Constant.API_URL_PARAM1_VALUE + Constant.API_URL_PARAM2 + Constant.API_URL_PARAM2_VALUE)
    Call<HeaderimageResponse> getImageHeader();

    @GET("WGS_API_best_seller_products.php?" + Constant.API_URL_PARAM1 + Constant.API_URL_PARAM1_VALUE + Constant.API_URL_PARAM2 + Constant.API_URL_PARAM2_VALUE)
    Call<BestSellerResponse> getBestSeller();
}
