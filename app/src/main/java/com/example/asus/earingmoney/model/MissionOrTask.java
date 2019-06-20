package com.example.asus.earingmoney.model;

public class MissionOrTask {
    //区分是mission还是task
    private boolean isMission = false;

    public MissionOrTask(boolean _isMission) {
        isMission = _isMission;
    }

    public boolean isMission() {
        return isMission;
    }

    public void setMission(boolean mission) {
        isMission = mission;
    }
}
