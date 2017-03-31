package com.ezytopup.salesapp.api;

/**
 * Created by indraaguslesmana on 3/30/17.
 */

public class Authrequest {

    //POST request
    private String provider;
    private String token;
    private String send_email;
    private String password;
    private User user;

    public Authrequest(String provider, String token, String send_email, String password) {
        this.provider = provider;
        this.token = token;
        this.send_email = send_email;
        this.password = password;
    }

    public User getUser() {
        return user;
    }

    public static class User{
        //Response
        private int id;
        private String email;
        private String first_name;
        private String last_name;
        private String access_token;
        private String token_generated_at;
        private String phone_number;
        private String last_login_at;
        private String saldo;
        private String store_logo;


        public int getId() {
            return id;
        }

        public String getEmail() {
            return email;
        }

        public String getFirst_name() {
            return first_name;
        }

        public String getLast_name() {
            return last_name;
        }

        public String getAccess_token() {
            return access_token;
        }

        public String getToken_generated_at() {
            return token_generated_at;
        }

        public String getPhone_number() {
            return phone_number;
        }

        public String getLast_login_at() {
            return last_login_at;
        }

        public String getSaldo() {
            return saldo;
        }

        public String getStore_logo() {
            return store_logo;
        }
    }

}
