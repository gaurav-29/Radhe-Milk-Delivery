package com.example.radhegausala.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ProfileModel {
    @Override
    public String toString() {
        return "ProfileModel{" +
                "data=" + data +
                ", message='" + message + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    @SerializedName("data")
    public Data data;
    @SerializedName("message")
    public String message;
    @SerializedName("status")
    public int status;

    public static class Data {
        @Override
        public String toString() {
            return "Data{" +
                    "user_id='" + user_id + '\'' +
                    ", created_at='" + created_at + '\'' +
                    ", status='" + status + '\'' +
                    ", profile_image='" + profile_image + '\'' +
                    ", city='" + city + '\'' +
                    ", address='" + address + '\'' +
                    ", dob='" + dob + '\'' +
                    ", mobile_no='" + mobile_no + '\'' +
                    ", email='" + email + '\'' +
                    ", name='" + name + '\'' +
                    ", cid='" + cid + '\'' +
                    ", society_ids='" + society_ids + '\'' +
                    ", type='" + type + '\'' +
                    '}';
        }

        @SerializedName("user_id")
        public String user_id;
        @SerializedName("created_at")
        public String created_at;
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
