package com.example.asus.earingmoney.model;

public class TaskModel extends MissionOrTask{
    private int taskId;
    private int taskType;
    private int taskStatus;
    private String finishTime;
    private String publishTime;
    private String description;
    private double aveMoney;
    private String title;
    private String deadLine;
    private int pubUserId;
    private int missionId;
    private int accUserId;
    private int reportNum;

    public TaskModel(int _taskId, int _taskType, int _taskStatus, String _finishTime,
                     int _pubUserId, int _missionId, int _accUserId) {
            super(false);
            taskId = _taskId;
            taskType = _taskType;
            taskStatus = _taskStatus;
            finishTime = _finishTime;
            pubUserId = _pubUserId;
            missionId = _missionId;
            accUserId = _accUserId;
    }

    public TaskModel()
    {
        super(false);
    }
    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
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

    public int getPubUserId() {
        return pubUserId;
    }

    public void setPubUserId(int pubUserId) {
        this.pubUserId = pubUserId;
    }

    public int getMissionId() {
        return missionId;
    }

    public void setMissionId(int missionId) {
        this.missionId = missionId;
    }

    public int getAccUserId() {
        return accUserId;
    }

    public void setAccUserId(int accUserId) {
        this.accUserId = accUserId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getAveMoney() {
        return aveMoney;
    }

    public void setAveMoney(double aveMoney) {
        this.aveMoney = aveMoney;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDeadLine() {
        return deadLine;
    }

    public void setDeadLine(String deadLine) {
        this.deadLine = deadLine;
    }

    public String getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(String publishTime) {
        this.publishTime = publishTime;
    }

    public int getReportNum() {
        return reportNum;
    }

    public void setReportNum(int reportNum) {
        this.reportNum = reportNum;
    }
}
