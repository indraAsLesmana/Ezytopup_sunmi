package com.ezytopup.salesapp.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

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
    @SerializedName("status")
    @Expose
    public BestSellerResponse.Status status;

    public BestSellerResponse.Status getStatus() {
        return status;
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

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    public String getSellerPassword() {
        return sellerPassword;
    }

    public void setSellerPassword(String sellerPassword) {
        this.sellerPassword = sellerPassword;
    }

    public String getSellerShopname() {
        return sellerShopname;
    }

    public void setSellerShopname(String sellerShopname) {
        this.sellerShopname = sellerShopname;
    }

    public String getSellerKasirname() {
        return sellerKasirname;
    }

    public void setSellerKasirname(String sellerKasirname) {
        this.sellerKasirname = sellerKasirname;
    }

    public static class Status {

        @SerializedName("code")
        @Expose
        public String code;
        @SerializedName("message")
        @Expose
        public String message;

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }
    }
}
