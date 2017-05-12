package com.ezytopup.salesapp.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by indraaguslesmana on 5/12/17.
 */

public class ChangepasswordResponse {
    @SerializedName("user")
    @Expose
    public Object user;
    @SerializedName("status")
    @Expose
    public Status status;

    public Object getUser() {
        return user;
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
}
