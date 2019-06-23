package com.example.asus.earingmoney.model;

import java.util.ArrayList;

public class QuestionareCommitModel {
    private ArrayList<QuestionCommitModel> questions;

    public void setQuestions(ArrayList<QuestionCommitModel> questions) {
        this.questions = questions;
    }

    public ArrayList<QuestionCommitModel> getQuestions() {
        return questions;
    }


    public static class QuestionCommitModel{
        public int questionId;
        public String answer;

        public QuestionCommitModel(int questionId, String answer) {
            this.questionId = questionId;
            this.answer = answer;
        }

        public void setQuestionId(int questionId) {
            this.questionId = questionId;
        }

        public int getQuestionId() {
            return questionId;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public String getAnswer() {
            return answer;
        }
    }
}
