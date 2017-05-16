package com.ezytopup.salesapp.api;

/**
 * Created by indraaguslesmana on 5/17/17.
 */

public class BuynowReseller {
    private String deviceId;
    private String productId;
    private String email;
    private String customerId;
    private String sessionName;
    private String sellerId;
    private String sellerPassword;
    private String sellerShopname;
    private String sellerKasirname;

    public BuynowReseller() {
    }

    public BuynowReseller(String deviceId, String productId, String email, String customerId,
                          String sessionName, String sellerId, String sellerPassword,
                          String sellerShopname, String sellerKasirname) {
        this.deviceId = deviceId;
        this.productId = productId;
        this.email = email;
        this.customerId = customerId;
        this.sessionName = sessionName;
        this.sellerId = sellerId;
        this.sellerPassword = sellerPassword;
        this.sellerShopname = sellerShopname;
        this.sellerKasirname = sellerKasirname;
    }

}
