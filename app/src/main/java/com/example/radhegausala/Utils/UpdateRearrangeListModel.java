package com.example.radhegausala.Utils;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class UpdateRearrangeListModel {

    @SerializedName("data")
    public ArrayList<String> data;
    @SerializedName("message")
    public String message;
    @SerializedName("status")
    public int status;
}
