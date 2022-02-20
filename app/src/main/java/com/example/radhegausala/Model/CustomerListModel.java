package com.example.radhegausala.Model;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class CustomerListModel {
    @Override
    public String toString() {
        return "CustomerListModel{" +
                "data=" + data +
                ", message='" + message + '\'' +
                ", status=" + status +
                '}';
    }

    @SerializedName("data")
    public MainData data;
    @SerializedName("message")
    public String message;
    @SerializedName("status")
    public int status;

    public static class MainData {
        @Override
        public String toString() {
            return "MainData{" +
                    "total=" + total +
                    ", to=" + to +
                    ", prev_page_url='" + prev_page_url + '\'' +
                    ", per_page=" + per_page +
                    ", path='" + path + '\'' +
                    ", next_page_url='" + next_page_url + '\'' +
                    ", last_page_url='" + last_page_url + '\'' +
                    ", last_page=" + last_page +
                    ", from=" + from +
                    ", first_page_url='" + first_page_url + '\'' +
                    ", data=" + data +
                    ", current_page=" + current_page +
                    '}';
        }

        @SerializedName("total")
        public int total;
        @SerializedName("to")
        public int to;
        @SerializedName("prev_page_url")
        public String prev_page_url;
        @SerializedName("per_page")
        public int per_page;
        @SerializedName("path")
        public String path;
        @SerializedName("next_page_url")
        public String next_page_url;
        @SerializedName("last_page_url")
        public String last_page_url;
        @SerializedName("last_page")
        public int last_page;
        @SerializedName("from")
        public int from;
        @SerializedName("first_page_url")
        public String first_page_url;
        @SerializedName("data")
        public ArrayList<Data> data;
        @SerializedName("current_page")
        public int current_page;
    }

    public static class Data {
        @Override
        public String toString() {
            return "Data{" +
                    "milk_yesterday=" + milk_yesterday +
                    ", milks=" + milks +
                    ", address='" + address + '\'' +
                    ", mobile_no='" + mobile_no + '\'' +
                    ", cid='" + cid + '\'' +
                    ", name='" + name + '\'' +
                    ", id=" + id +
                    '}';
        }

        @SerializedName("milk_yesterday")
        public ArrayList<Milk_yesterday> milk_yesterday;
        @SerializedName("milks")
        public ArrayList<Milks> milks;
        @SerializedName("address")
        public String address;
        @SerializedName("mobile_no")
        public String mobile_no;
        @SerializedName("cid")
        public String cid;
        @SerializedName("name")
        public String name;
        @SerializedName("id")
        public int id;
    }

    public static class Milk_yesterday {
        @Override
        public String toString() {
            return "Milk_yesterday{" +
                    "updated_at='" + updated_at + '\'' +
                    ", created_at='" + created_at + '\'' +
                    ", evening=" + evening +
                    ", morning=" + morning +
                    ", rate=" + rate +
                    ", milk_date='" + milk_date + '\'' +
                    ", user_id=" + user_id +
                    ", user_ship_id=" + user_ship_id +
                    ", id=" + id +
                    '}';
        }

        @SerializedName("updated_at")
        public String updated_at;
        @SerializedName("created_at")
        public String created_at;
        @SerializedName("evening")
        public float evening;
        @SerializedName("morning")
        public float morning;
        @SerializedName("rate")
        public float rate;
        @SerializedName("milk_date")
        public String milk_date;
        @SerializedName("user_id")
        public int user_id;
        @SerializedName("user_ship_id")
        public int user_ship_id;
        @SerializedName("id")
        public int id;
    }

    public static class Milks {
        @Override
        public String toString() {
            return "Milks{" +
                    "updated_at='" + updated_at + '\'' +
                    ", created_at='" + created_at + '\'' +
                    ", morning=" + morning +
                    ", evening=" + evening +
                    ", rate=" + rate +
                    ", milk_date='" + milk_date + '\'' +
                    ", user_id=" + user_id +
                    ", user_ship_id=" + user_ship_id +
                    ", id=" + id +
                    '}';
        }

        @SerializedName("updated_at")
        public String updated_at;
        @SerializedName("created_at")
        public String created_at;
        @SerializedName("morning")
        public float morning;
        @SerializedName("evening")
        public float evening;
        @SerializedName("rate")
        public float rate;
        @SerializedName("milk_date")
        public String milk_date;
        @SerializedName("user_id")
        public int user_id;
        @SerializedName("user_ship_id")
        public int user_ship_id;
        @SerializedName("id")
        public int id;
    }
}
