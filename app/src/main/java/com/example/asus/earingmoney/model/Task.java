package com.example.asus.earingmoney.model;

public class Task {
    private int taskId;
    private int missionId;
    private int pubUserId;
    private int accUserId;
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
        return missionId;
    }

    public void setMissionId(int missionId) {
        this.missionId = missionId;
    }

    public int getPubUserId() {
        return pubUserId;
    }

    public void setPubUserId(int pubUserId) {
        this.pubUserId = pubUserId;
    }
  
    public int getAccUserId() {
        return accUserId;
    }

    public void setAccUSerId(int accUSerId) {
        this.accUserId = accUSerId;
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
