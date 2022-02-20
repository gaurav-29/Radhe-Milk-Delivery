package com.example.radhegausala.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class RearrangeMorningListModel {

    @SerializedName("data")
    public MainData data;
    @SerializedName("message")
    public String message;
    @SerializedName("status")
    public int status;

    @Override
    public String toString() {
        return "RearrangeListModel{" +
                "data=" + data +
                ", message='" + message + '\'' +
                ", status=" + status +
                '}';
    }

    public static class MainData {
        @SerializedName("total")
        public int total;
        @SerializedName("to")
        public int to;
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

        @Override
        public String toString() {
            return "MainData{" +
                    "total=" + total +
                    ", to=" + to +
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
    }

    public static class Data {
        @SerializedName("sort_evening_no")
        public int sort_evening_no;
        @SerializedName("sort_morning_no")
        public int sort_morning_no;
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

        @Override
        public String toString() {
            return "Data{" +
                    "sort_evening_no=" + sort_evening_no +
                    ", sort_morning_no=" + sort_morning_no +
                    ", address='" + address + '\'' +
                    ", mobile_no='" + mobile_no + '\'' +
                    ", cid='" + cid + '\'' +
                    ", name='" + name + '\'' +
                    ", id=" + id +
                    '}';
        }
    }
}
