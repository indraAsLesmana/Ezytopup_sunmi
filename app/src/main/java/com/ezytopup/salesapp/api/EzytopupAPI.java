package com.ezytopup.salesapp.api;

import com.ezytopup.salesapp.Eztytopup;
import com.ezytopup.salesapp.utility.Constant;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
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
    Call<TransactionHistoryResponse> getHistory(@Header("Authorize") String token, @Query("customerId") int customerId);
    /**
     * this API i implement with object PaymentResponse have no object data, just get Message for checking result 200 or not
     * */
    @POST("WGS_API_abuyNow.php?" + Constant.API_URL_GENERALUSAGE)
    Call<PaymentResponse> buyNow(@Header("Authorize") String token,
                                 @Query("id") String id,
                                 @Query("session_name") String session_name,
                                 @Query("product_id") String product_id,
                                 @Query("product_name") String product_name,
                                 @Query("price") String price,
                                 @Query("qty") String qty,
                                 @Query("payment_method_id") String payment_method_id,
                                 @Query("email") String email,
                                 @Query("customerId") String customerId,
                                 @Query("templateId") String templateId,
                                 @Query("serviceFee") String serviceFee,
                                 @Query("serviceFeePercentage") String serviceFeePercentage,
                                 @Query("discount") String discount,
                                 @Query("recepientName") String recepientName,
                                 @Query("recepientEmail") String recepientEmail,
                                 @Query("message") String message,
                                 @Query("senderName") String senderName,
                                 @Query("paymentCaption") String paymentCaption,
                                 @Query("paymentNote") String paymentNote,
                                 @Query("templateCaption") String templateCaption,
                                 @Query("coupon_promo") String coupon_promo);

    @GET("WGS_API_template_gift.php?" + Constant.API_URL_GENERALUSAGE)
    Call<TamplateResponse> getTamplateGift();

    @GET("WGS_API_verify_access_token.php?" + Constant.API_URL_GENERALUSAGE)
    Call<TokencheckResponse> checkToken(@Header("Authorize") String token);

    @POST("WGS_API_signup_tanpa_login.php?" + Constant.API_URL_GENERALUSAGE)
    Call<ResponseBody> setLoginskip(@Query("provider") String provider,
                                    @Query("reg_gcm_id") String regfcmid,
                                    @Query("device_id") String deviceid);
}
