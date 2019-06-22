package com.example.asus.earingmoney.model;

public class Task {
    private int taskId;
    private int MissionId;
    private int pubUserId;
    private int accUSerId;
    private int taskStatus;
    private String finishTime;
    private int taskType;

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getMissionId() {
        return MissionId;
    }

    public void setMissionId(int missionId) {
        MissionId = missionId;
    }

    public int getPubUserId() {
        return pubUserId;
    }

    public void setPubUserId(int pubUserId) {
        this.pubUserId = pubUserId;
    }

    public int getAccUSerId() {
        return accUSerId;
    }

    public void setAccUSerId(int accUSerId) {
        this.accUSerId = accUSerId;
    }

    public int getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(int taskStatus) {
        this.taskStatus = taskStatus;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

}
