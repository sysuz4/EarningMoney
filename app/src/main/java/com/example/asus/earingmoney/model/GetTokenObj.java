package com.example.asus.earingmoney.model;

import com.google.gson.annotations.SerializedName;

public class GetTokenObj {
    @SerializedName("userId")
    private int userId;

    @SerializedName("token")
    private String token;

    public int getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
