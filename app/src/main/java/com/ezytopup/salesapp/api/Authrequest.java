package com.ezytopup.salesapp.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by indraaguslesmana on 3/30/17.
 */

public class Authrequest {
    private String provider;
    private String email;
    private String password;
    private String device_id;

    public Authrequest() {
    }

    public Authrequest(String provider, String email, String password, String device_id) {
        this.provider = provider;
        this.email = email;
        this.password = password;
        this.device_id = device_id;
    }

    @SerializedName("user")
    @Expose
    public User user;
    @SerializedName("status")
    @Expose
    public Status status;

    public User getUser() {
        return user;
    }

    public Status getStatus() {
        return status;
    }

    public class Status {
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

    public class User {
        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("image_user")
        @Expose
        public String imageUser;
        @SerializedName("email")
        @Expose
        public String email;
        @SerializedName("first_name")
        @Expose
        public String firstName;
        @SerializedName("last_name")
        @Expose
        public String lastName;
        @SerializedName("access_token")
        @Expose
        public String accessToken;
        @SerializedName("token_generated_at")
        @Expose
        public String tokenGeneratedAt;
        @SerializedName("phone_number")
        @Expose
        public String phoneNumber;
        @SerializedName("last_login_at")
        @Expose
        public String lastLoginAt;

        public String getId() {
            return id;
        }

        public String getImageUser() {
            return imageUser;
        }

        public String getEmail() {
            return email;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public String getTokenGeneratedAt() {
            return tokenGeneratedAt;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public String getLastLoginAt() {
            return lastLoginAt;
        }
    }

}
