package com.example.asus.earingmoney.model;

public class Question {

    private int questionId;
    private int questionType;
    private String question;
    private String answer;
    private int choiceNum;
    private String choiceStr;
    private int questionareId;

    public Question()
    {

    }

    public Question(int questionType, String question){
        this.questionType = questionType;
        this.question = question;
        this.answer = "";
    }

    public Question(int questionType, String question, String choiceStr, int choiceNum)
    {
        this.questionType = questionType;
        this.question = question;
        this.choiceStr = choiceStr;
        this.choiceNum = choiceNum;
    }

    public int getQuestionId() {
        return questionId;
    }

    public void setQuestionId(int questionId) {
        this.questionId = questionId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public int getQuestionType() {
        return questionType;
    }

    public void setQuestionType(int questionType) {
        this.questionType = questionType;
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
