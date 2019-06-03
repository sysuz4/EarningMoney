package com.example.asus.earingmoney.model;

public class QuestionModel {
    private long id;
    private int questionType;
    private String question;
    private String answer;
    private int choiceNum;
    private String choiceStr;
    private int questionareId;

    public QuestionModel()
    {

    }

    public QuestionModel(int questionType, String question){
        this.questionType = questionType;
        this.question = question;
        this.answer = "";
    }

    public QuestionModel(int questionType, String question, String choiceStr, int choiceNum)
    {
        this.questionType = questionType;
        this.question = question;
        this.choiceStr = choiceStr;
        this.choiceNum = choiceNum;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getQuestionType() {
        return questionType;
    }

    public void setQuestionType(int questionType) {
        this.questionType = questionType;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public int getChoiceNum() {
        return choiceNum;
    }

    public void setChoiceNum(int choiceNum) {
        this.choiceNum = choiceNum;
    }

    public String getChoiceStr() {
        return choiceStr;
    }

    public void setChoiceStr(String choiceStr) {
        this.choiceStr = choiceStr;
    }

    public int getQuestionareId() {
        return questionareId;
    }

    public void setQuestionareId(int questionareId) {
        this.questionareId = questionareId;
    }
}
