package com.example.radhegausala.Model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class UpdateProfileModel {

    @SerializedName("data")
    public ArrayList<Data> data;
    @SerializedName("message")
    public String message;
    @SerializedName("status")
    public int status;

    public  class Data{
        @SerializedName("name")
        public String name;
    }
}
