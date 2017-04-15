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
    Call<SearchResponse> getSearch(@Query("name") String productName);

    @GET("WGS_API_get_detail_products.php?" + Constant.API_URL_GENERALUSAGE)
    Call<DetailProductResponse> getDetailProduct(@Query("id") String productid);

    @GET("WGS_API_panduan_awal.php?" + Constant.API_URL_GENERALUSAGE)
    Call<TutorialResponse> getTutorial();

    @GET("WGS_API_getFaq.php?" + Constant.API_URL_GENERALUSAGE)
    Call<FaqResponse> getFaq();

    @GET("WGS_API_getTerm.php?" + Constant.API_URL_GENERALUSAGE)
    Call<TermResponse> getTerm();

    @GET("WGS_API_categorized_products.php?" + Constant.API_URL_GENERALUSAGE)
    Call<CategoryResponse> getCategory(@Query("category_id") String productId);

    @GET("WGS_API_payment_method.php?" + Constant.API_URL_GENERALUSAGE)
    Call<PaymentResponse> getCheckactivePayment();

    @GET("WGS_API_payment_method_internet_banking.php?" + Constant.API_URL_GENERALUSAGE)
    Call<PaymentResponse> getPaymentInetBanking();

    @GET("WGS_API_payment_method_bank_transfer.php?" + Constant.API_URL_GENERALUSAGE)
    Call<PaymentResponse> getPaymentBankTransfer();

    @GET("WGS_API_payment_method_credit_card.php?" + Constant.API_URL_GENERALUSAGE)
    Call<PaymentResponse> getPaymentCreditcard();

    @GET("WGS_API_payment_method_wallet.php?" + Constant.API_URL_GENERALUSAGE)
    Call<PaymentResponse> getPaymentEzyWallet();

    @GET("WGS_API_categories.php?" + Constant.API_URL_GENERALUSAGE)
    Call<ListCategoryResponse> getListCategory();

    @GET("WGS_API_get_order_transaction_history.php?" + Constant.API_URL_GENERALUSAGE)
    Call<TransactionHistoryResponse> getHistory(@Query("customerId") String customerId);

}
