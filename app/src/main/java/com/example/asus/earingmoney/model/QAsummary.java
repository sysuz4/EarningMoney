package com.example.asus.earingmoney.model;

import java.util.ArrayList;

public class QAsummary {
    private int missionId;
    private int taskNum;
    private int finishNum;
    private String QATitle;
    private String QADes;
    private ArrayList<QuestionModel> questions;

    public QAsummary(int _missionId, int _taskNum, int _finishNum, String _QATitle,
                     String _QADes,  ArrayList<QuestionModel> _questions) {
            this.missionId = _missionId;
            this.taskNum = _taskNum;
            this.finishNum = _finishNum;
            this.QATitle = _QATitle;
            this.QADes = _QADes;
            this.questions = _questions;
    }

    public int getMissionId() {
        return missionId;
    }

    public void setMissionId(int missionId) {
        this.missionId = missionId;
    }

    public int getTaskNum() {
        return taskNum;
    }

    public void setTaskNum(int taskNum) {
        this.taskNum = taskNum;
    }

    public int getFinishNum() {
        return finishNum;
    }

    public void setFinishNum(int finishNum) {
        this.finishNum = finishNum;
    }

    public String getQATitle() {
        return QATitle;
    }

    public void setQATitle(String QATitle) {
        this.QATitle = QATitle;
    }

    public String getQADes() {
        return QADes;
    }

    public void setQADes(String QADes) {
        this.QADes = QADes;
    }

    public ArrayList<QuestionModel> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<QuestionModel> questions) {
        this.questions = questions;
    }

    public double getCompleteness() {
        return (double)finishNum / taskNum;
    }

}
