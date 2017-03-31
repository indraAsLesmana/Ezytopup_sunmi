package com.ezytopup.salesapp.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by indraaguslesmana on 3/31/17.
 */

public interface EzytopupAPI {

    @POST("/v1/auth/login")
    Call<Authrequest> login_request (@Body Authrequest authrequest);
}
