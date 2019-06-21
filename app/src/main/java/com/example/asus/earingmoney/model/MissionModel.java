package com.example.asus.earingmoney.model;

public class MissionModel extends MissionOrTask{
    private int missionId;
    private String publishTime;
    private int missionStatus;
    private String title;
    private String deadLine;
    private String tags;
    private int money;
    private int userId;
    private int taskNum;

    public MissionModel(int _missionId, String _publishTime, int _missionStatus, String _title,
                        String _deadLine, String _tags, int _money, int _userId, int _taskNum) {
        super(true);
        missionId = _missionId;
        publishTime = _publishTime;
        missionStatus = _missionStatus;
        title = _title;
        deadLine = _deadLine;
        tags = _tags;
        money = _money;
        taskNum = _taskNum;
        userId = _userId;
    }

    public MissionModel()
    {
        super(true);
    }
    public int getMissionId() {
        return missionId;
    }

    public void setMissionId(int missionId) {
        this.missionId = missionId;
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

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTaskNum() {
        return taskNum;
    }

    public void setTaskNum(int taskNum) {
        this.taskNum = taskNum;
    }
}
