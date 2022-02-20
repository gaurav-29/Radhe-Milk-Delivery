package com.example.radhegausala.Model;

import com.google.gson.annotations.SerializedName;

public class LoginModel {

    @SerializedName("data")
    public Data data;
    @SerializedName("message")
    public String message;
    @SerializedName("status")
    public int status;

    public static class Data {
        @SerializedName("api_token")
        public String api_token;
        @SerializedName("user_id")
        public String user_id;
        @SerializedName("status")
        public String status;
        @SerializedName("profile_image")
        public String profile_image;
        @SerializedName("city")
        public String city;
        @SerializedName("address")
        public String address;
        @SerializedName("dob")
        public String dob;
        @SerializedName("mobile_no")
        public String mobile_no;
        @SerializedName("email")
        public String email;
        @SerializedName("name")
        public String name;
        @SerializedName("cid")
        public String cid;
        @SerializedName("society_ids")
        public String society_ids;
        @SerializedName("type")
        public String type;
        
    }
}
