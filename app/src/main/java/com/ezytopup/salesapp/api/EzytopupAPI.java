package com.ezytopup.salesapp.api;

import com.ezytopup.salesapp.Eztytopup;
import com.ezytopup.salesapp.utility.Constant;

import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

import static com.ezytopup.salesapp.utility.Constant.API_URL_GENERALUSAGE;

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

    @POST("WGS_API_login.php?" + API_URL_GENERALUSAGE)
    Call<Authrequest> login_request (@Body Authrequest authrequest);

    @GET("WGS_API_products.php?" + API_URL_GENERALUSAGE)
    Call<ProductResponse> getProduct();

    @GET("WGS_API_getHeaderImages.php?" + API_URL_GENERALUSAGE)
    Call<HeaderimageResponse> getImageHeader();

    @GET("WGS_API_best_seller_products.php?" + API_URL_GENERALUSAGE)
    Call<BestSellerResponse> getBestSeller();

    @GET("WGS_API_search_products.php?" + API_URL_GENERALUSAGE)
    Call<SearchResponse> getSearch(@Query("name") String productName);

    @GET("WGS_API_get_detail_products.php?" + API_URL_GENERALUSAGE)
    Call<DetailProductResponse> getDetailProduct(@Query("id") String productid);

    @GET("WGS_API_panduan_awal.php?" + API_URL_GENERALUSAGE)
    Call<TutorialResponse> getTutorial();

    @GET("WGS_API_getFaq.php?" + API_URL_GENERALUSAGE)
    Call<FaqResponse> getFaq();

    @GET("WGS_API_getTerm.php?" + API_URL_GENERALUSAGE)
    Call<TermResponse> getTerm();

    @GET("WGS_API_categorized_products.php?" + API_URL_GENERALUSAGE)
    Call<CategoryResponse> getCategory(@Query("category_id") String productId);

    @GET("WGS_API_payment_method.php?" + API_URL_GENERALUSAGE)
    Call<PaymentResponse> getCheckactivePayment();

    @GET("WGS_API_payment_method_internet_banking.php?" + API_URL_GENERALUSAGE)
    Call<PaymentResponse> getPaymentInetBanking();

    @GET("WGS_API_payment_method_bank_transfer.php?" + API_URL_GENERALUSAGE)
    Call<PaymentResponse> getPaymentBankTransfer();

    @GET("WGS_API_payment_method_credit_card.php?" + API_URL_GENERALUSAGE)
    Call<PaymentResponse> getPaymentCreditcard();

    @GET("WGS_API_payment_method_wallet.php?" + API_URL_GENERALUSAGE)
    Call<PaymentResponse> getPaymentEzyWallet();

    @GET("WGS_API_categories.php?" + API_URL_GENERALUSAGE)
    Call<ListCategoryResponse> getListCategory();

    @GET("WGS_API_get_order_transaction_history.php?" + API_URL_GENERALUSAGE)
    Call<TransactionHistoryResponse> getHistory(@Header("Authorize") String token, @Query("customerId") int customerId);
    /**
     * this API i implement with object PaymentResponse have no object data, just get Message for checking result 200 or not
     * */
    @POST("WGS_API_abuyNow.php?" + API_URL_GENERALUSAGE)
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

    @GET("WGS_API_template_gift.php?" + API_URL_GENERALUSAGE)
    Call<TamplateResponse> getTamplateGift();

    @GET("WGS_API_verify_access_token.php?" + API_URL_GENERALUSAGE)
    Call<TokencheckResponse> checkToken(@Header("Authorize") String token);

    @POST("WGS_API_signup_tanpa_login.php?" + API_URL_GENERALUSAGE)
    Call<Authrequest> setLoginskip(@Query("provider") String provider,
                                    @Query("reg_gcm_id") String regfcmid,
                                    @Query("device_id") String deviceid);

    @POST("WGS_API_login_reg_gcm_id.php?" + API_URL_GENERALUSAGE)
    Call<RegfcmResponse> setRegFcm(@Query("provider") String provider,
                                    @Query("reg_gcm_id") String regfcmid,
                                    @Query("device_id") String deviceid);

    @POST("WGS_API_logout.php?" + API_URL_GENERALUSAGE)
    Call<TokencheckResponse> setLogout(@Header("Authorize") String headerToken,
                                       @Query("device_id") String device_id,
                                       @Query("token") String token);

    @GET("WGS_API_get_current_user_wallet.php?" + API_URL_GENERALUSAGE)
    Call<WalletbalanceResponse> getWalletbalance(@Header("Authorize") String headerToken);

    @POST("WGS_API_change_password.php?" + API_URL_GENERALUSAGE)
    Call<ChangepasswordResponse> setChangePassword(@Header("Authorize") String headerToken,
                                                   @Query("token") String token,
                                                   @Query("pass_baru1") String newPassword,
                                                   @Query("pass_baru2") String confirmPassword,
                                                   @Query("pass_lama") String oldPassword);

    @POST("WGS_API_forget_password.php?" + API_URL_GENERALUSAGE)
    Call<ForgotpasswordResponse> setForgotpassword(@Header("Authorize") String headerToken,
                                                   @Query("email") String email,
                                                   @Query("phone") String phone);

    @POST("WGS_API_buyNow_reseller.php?" + API_URL_GENERALUSAGE)
    Call<VoucherprintResponse> getBuyreseller(@Body HashMap<String, String> data);

}
