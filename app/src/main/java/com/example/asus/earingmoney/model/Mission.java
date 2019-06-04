package com.example.asus.earingmoney.model;

import com.google.gson.annotations.SerializedName;

public class Mission {

    @SerializedName("missionId")
    private int missionId;

    @SerializedName("missionStatus")
    private int missionStatus;

    @SerializedName("publishTime")
    private String publishTime;

    @SerializedName("title")
    private String title;

    @SerializedName("deadLine")
    private String deadLine;

    @SerializedName("tags")
    private String tags;

    @SerializedName("money")
    private int money;

    @SerializedName("userId")
    private int userId;

    public String getTitle() {
        return title;
    }

    public int getMissionId() {
        return missionId;
    }

    public int getMissionStatus() {
        return missionStatus;
    }

    public int getMoney() {
        return money;
    }

    public int getUserId() {
        return userId;
    }

    public String getDeadLine() {
        return deadLine;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public String getTags() {
        return tags;
    }
}
