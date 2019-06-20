package com.example.asus.earingmoney.model;

public class TaskModel extends MissionOrTask{
    private int taskId;
    private int taskType;
    private int taskStatus;
    private String finishTime;
    private int pubUserId;
    private int missionId;
    private int accUserId;

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
}
