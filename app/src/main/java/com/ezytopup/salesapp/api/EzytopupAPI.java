package com.ezytopup.salesapp.api;

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
     *
     * */
    /* login */
    @POST("WGS_API_login.php?H5c30S5aHa9c45J297a680beB0795f87W131556M9b82e471ee1c2baS82699e9eWa218G2c05fG3e259G971562052D03a78c81D6afdb5e92Gb=d61H266a2d1c5ca33475257S13fb490efba4f17ee0013596Dc03da1372e31c6a94f0eb608fbc834af00e4G6141715b4981c41f0L6d2959b43a71b4576fSa66285cf486423fa70fa5b66f1d28b95O24f3d7b8465b0Gf19122d75Ubbe828aF3Tdd8845cc1a7W9bS&Kb0a176e5O7efbaf58OaKaFdfd0C79e353e1dbFCcf2328Ic6477922S5b4a47b79120D0U5WddV0d2b225W0fdf5Q809Vc=Ezy_Apps_WGS")
    Call<Authrequest> login_request (@Body Authrequest authrequest);
    /* product */
    @GET("WGS_API_products.php?H5c30S5aHa9c45J297a680beB0795f87W131556M9b82e471ee1c2baS82699e9eWa218G2c05fG3e259G971562052D03a78c81D6afdb5e92Gb=d61H266a2d1c5ca33475257S13fb490efba4f17ee0013596Dc03da1372e31c6a94f0eb608fbc834af00e4G6141715b4981c41f0L6d2959b43a71b4576fSa66285cf486423fa70fa5b66f1d28b95O24f3d7b8465b0Gf19122d75Ubbe828aF3Tdd8845cc1a7W9bS&Kb0a176e5O7efbaf58OaKaFdfd0C79e353e1dbFCcf2328Ic6477922S5b4a47b79120D0U5WddV0d2b225W0fdf5Q809Vc=Ezy_Apps_WGS")
    Call<ProductResponse> getProduct();
    /*header image*/
    @GET("WGS_API_getHeaderImages.php?H5c30S5aHa9c45J297a680beB0795f87W131556M9b82e471ee1c2baS82699e9eWa218G2c05fG3e259G971562052D03a78c81D6afdb5e92Gb=d61H266a2d1c5ca33475257S13fb490efba4f17ee0013596Dc03da1372e31c6a94f0eb608fbc834af00e4G6141715b4981c41f0L6d2959b43a71b4576fSa66285cf486423fa70fa5b66f1d28b95O24f3d7b8465b0Gf19122d75Ubbe828aF3Tdd8845cc1a7W9bS&Kb0a176e5O7efbaf58OaKaFdfd0C79e353e1dbFCcf2328Ic6477922S5b4a47b79120D0U5WddV0d2b225W0fdf5Q809Vc=Ezy_Apps_WGS")
    Call<HeaderimageResponse> getImageHeader();

}
