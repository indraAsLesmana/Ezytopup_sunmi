package com.ezytopup.salesapp.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by indraaguslesmana on 4/7/17.
 */

public class DetailProductResponse {

        @SerializedName("result")
        @Expose
        public ArrayList<Result> result = null;
        @SerializedName("status")
        @Expose
        public Status status;

    public static class Result {

        @SerializedName("product_name")
        @Expose
        public String productName;
        @SerializedName("review_url")
        @Expose
        public String reviewUrl;
        @SerializedName("share_url")
        @Expose
        public String shareUrl;
        @SerializedName("harga_toko")
        @Expose
        public String hargaToko;
        @SerializedName("info1")
        @Expose
        public String info1;
        @SerializedName("info2")
        @Expose
        public String info2;
        @SerializedName("info3")
        @Expose
        public String info3;
        @SerializedName("description")
        @Expose
        public String description;

    }

    public static class Status {

        @SerializedName("code")
        @Expose
        public String code;
        @SerializedName("message")
        @Expose
        public String message;

    }
}
