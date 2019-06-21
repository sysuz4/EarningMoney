package com.example.asus.earingmoney.model;

import com.google.gson.annotations.SerializedName;

public class Mission {

    @SerializedName("missionId")
    private int missionId;

    @SerializedName("title")
    private String title;

    @SerializedName("publishTime")
    private String publishTime;

    @SerializedName("deadLine")
    private String deadLine;

    @SerializedName("taskType")
    private String taskType;

    @SerializedName("description")
    private String description;

    @SerializedName("avator")
    private String avator;

    public String getTitle() {
        return title;
    }

    public int getMissionId() {
        return missionId;
    }

    public String getDeadLine() {
        return deadLine;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public String getAvator() {
        return avator;
    }

    public String getDescription() {
        return description;
    }

    public String getTaskType() {
        return taskType;
    }
}
