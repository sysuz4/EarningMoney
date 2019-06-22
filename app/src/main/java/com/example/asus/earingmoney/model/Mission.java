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

    @SerializedName("userId")
    private int userId;
  
    @SerializedName("missionStatus")
    private int missionStatus;
  
    public void setMissionId(int missionId) {
        this.missionId = missionId;
    }

    @SerializedName("taskNum")
    private int taskNum;

    @SerializedName("tags")
    private String tags;

    public int getTaskNum() {
        return taskNum;
    }

    public void setTaskNum(int taskNum) {
        this.taskNum = taskNum;
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

    public void setMissionStatus(int missionStatus) {
        this.missionStatus = missionStatus;
    }

    public int getMissionId() {
        return missionId;
    }

    public String getDeadLine() {
        return deadLine;
    }

    public String getTitle() {
        return title;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public int getMissionStatus() {
        return missionStatus;
    }

    public String getDescription() {
        return description;
    }

    public String getTaskType() {
        return taskType;
    }

    public String getAvator() {
        return avator;
    }

}
