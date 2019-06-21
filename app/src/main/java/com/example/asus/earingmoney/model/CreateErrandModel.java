package com.example.asus.earingmoney.model;

public class CreateErrandModel {
    private Mission mission;
    private Errand Errand;
    private Task task;


    public Mission getMission() {
        return mission;
    }

    public void setMission(Mission mission) {
        this.mission = mission;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }

    public com.example.asus.earingmoney.model.Errand getErrand() {
        return Errand;
    }

    public void setErrand(com.example.asus.earingmoney.model.Errand errand) {
        Errand = errand;
    }
}
