package com.example.radhegausala.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class EveningReformatModel {
    @SerializedName("data")
    public ArrayList<Data> data;
    @SerializedName("status")
    public int status;
    @SerializedName("message")
    public String message;

    @Override
    public String toString() {
        return "EveningReformatModel{" +
                "data=" + data +
                '}';
    }

    public static class Data {
        @SerializedName("user_id")
        public String UserId;
        @SerializedName("sort")
        public String ShortNum;

        @Override
        public String toString() {
            return "{" +
                    "\"user_id\":" + UserId +
                    ",\"sort\":" + ShortNum +
                    '}';
        }
    }
}
