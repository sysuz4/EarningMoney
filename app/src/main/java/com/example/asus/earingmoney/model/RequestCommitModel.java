package com.example.asus.earingmoney.model;

public class RequestCommitModel {
    private int missionId;
    private String reason;

    public void setMissionId(int missionId) {
        this.missionId = missionId;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getMissionId() {
        return missionId;
    }

    public String getReason() {
        return reason;
    }
}
