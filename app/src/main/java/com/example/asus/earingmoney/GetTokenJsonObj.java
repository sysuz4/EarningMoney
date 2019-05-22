package com.example.asus.earingmoney;

import com.google.gson.annotations.SerializedName;

public class GetTokenJsonObj {

    @SerializedName("title")
    private String title;

    @SerializedName("userId")
    private String userId;

    @SerializedName("token")
    private String token;

    public String getTitle() {
        return title;
    }

    public String getUserId() {
        return userId;
    }

    public String getToken() {
        return token;
    }

}
