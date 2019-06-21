package com.example.asus.earingmoney.model;

import com.google.gson.annotations.SerializedName;

public class GetTokenObj {

    @SerializedName("useId")
    private int userId;

    @SerializedName("token")
    private String token;

    public int getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }
}
