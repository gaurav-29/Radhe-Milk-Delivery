package com.example.radhegausala.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class AddMilkModel {

    @SerializedName("data")
    public List<String> data;
    @SerializedName("message")
    public String message;
    @SerializedName("status")
    public int status;
}
