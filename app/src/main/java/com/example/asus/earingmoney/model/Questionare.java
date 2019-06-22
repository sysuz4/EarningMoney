package com.example.asus.earingmoney.model;

import java.util.List;

public class Questionare {
    private int questionareId;
    private int questionNum;
    private int taskId;
    private String description;
    private String title;
    private List<Question> questions;

    public int getQuestionareId() {
        return questionareId;
    }

    public void setQuestionareId(int questionareId) {
        this.questionareId = questionareId;
    }

    public int getQuestionNum() {
        return questionNum;
    }

    public void setQuestionNum(int questionNum) {
        this.questionNum = questionNum;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
