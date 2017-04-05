package com.ezytopup.salesapp.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by indraaguslesmana on 4/5/17.
 */

public class BestSellerResponse {

    @SerializedName("products")
    @Expose
    public List<Product> products = null;
    @SerializedName("status")
    @Expose
    public Status status;

    public List<Product> getProducts() {
        return products;
    }

    public Status getStatus() {
        return status;
    }

    public class Product {

        @SerializedName("product_id")
        @Expose
        public String productId;
        @SerializedName("product_name")
        @Expose
        public String productName;
        @SerializedName("category_name")
        @Expose
        public String categoryName;
        @SerializedName("price")
        @Expose
        public String price;
        @SerializedName("review_url")
        @Expose
        public String reviewUrl;
        @SerializedName("background_image_url")
        @Expose
        public String backgroundImageUrl;

        public String getProductId() {
            return productId;
        }

        public String getProductName() {
            return productName;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public String getPrice() {
            return price;
        }

        public String getReviewUrl() {
            return reviewUrl;
        }

        public String getBackgroundImageUrl() {
            return backgroundImageUrl;
        }
    }

    public class Status {

        @SerializedName("code")
        @Expose
        public String code;
        @SerializedName("message")
        @Expose
        public String message;

    }
}
