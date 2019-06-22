package com.example.asus.earingmoney.model;

public class Errand {
    private int errandId;
    private String description;
    private int taskId;
    private String privateInfo;
    private String pic;

    public int getErrandId() {
        return errandId;
    }

    public void setErrandId(int errandId) {
        this.errandId = errandId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrivateInfo() {
        return privateInfo;
    }

    public void setPrivateInfo(String privateInfo) {
        this.privateInfo = privateInfo;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
