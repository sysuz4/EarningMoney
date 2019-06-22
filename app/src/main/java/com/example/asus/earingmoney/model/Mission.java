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

    @SerializedName("money")
    private float money;

    public void setMissionId(int missionId) {
        this.missionId = missionId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public void setDeadLine(String deadLine) {
        this.deadLine = deadLine;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAvator(String avator) {
        this.avator = avator;
    }

    public void setMoney(float money) {
        this.money = money;
    }

    public int getMissionStatus() {
        return missionStatus;
    }

    public void setMissionStatus(int missionStatus) {
        this.missionStatus = missionStatus;
    }

    @SerializedName("missionStatus")
    private int missionStatus;

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

    public float getMoney() {
        return money;
    }
}
