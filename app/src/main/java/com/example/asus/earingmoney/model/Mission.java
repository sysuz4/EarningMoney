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
    private Float money;

    @SerializedName("userId")
    private int userId;

    @SerializedName("taskNum")
    private int taskNum;


    public int getTaskNum() {
        return taskNum;
    }

    public void setTaskNum(int taskNum) {
        this.taskNum = taskNum;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Float getMoney() {
        return money;
    }

    public void setMoney(Float money) {
        this.money = money;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(String deadLine) {
        this.deadLine = deadLine;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public int getMissionStatus() {
        return missionStatus;
    }

    public void setMissionStatus(int missionStatus) {
        this.missionStatus = missionStatus;
    }

    public int getMissionId() {
        return missionId;
    }

    public void setMissionId(int missionId) {
        this.missionId = missionId;
    }
}
