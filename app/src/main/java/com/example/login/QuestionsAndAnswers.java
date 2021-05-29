package com.example.login;

import java.util.HashMap;

public class QuestionsAndAnswers {
    HashMap<String, String> financeQuestions = new HashMap<>();
    HashMap<String, String> financeAnswers = new HashMap<>();
    private static final int financeTotalQuestions = 4;

    public HashMap<String, String> getFinanceQuestions() {
        setFinanceQuestions();
        return financeQuestions;
    }

    public HashMap<String, String> getFinanceAnswers() {
        setfinanceAnswers();
        return financeAnswers;
    }

    public static int getFinanceTotalQuestions() {
        return financeTotalQuestions;
    }

    private void setFinanceQuestions() {
        financeQuestions.put("q1", "Question 1");
        financeQuestions.put("q2", "Question 2");
        financeQuestions.put("q3", "Question 3");
        financeQuestions.put("q4", "Question 4");
    }

    private void setfinanceAnswers() {
        financeAnswers.put("a11", "Answer 1a");
        financeAnswers.put("a12", "Answer 1b");
        financeAnswers.put("a13", "Answer 1c");
        financeAnswers.put("a14", "Answer 1d");
        financeAnswers.put("a1", "2");
        financeAnswers.put("a21", "Answer 2a");
        financeAnswers.put("a22", "Answer 2b");
        financeAnswers.put("a23", "Answer 2c");
        financeAnswers.put("a24", "Answer 2d");
        financeAnswers.put("a2", "2");
        financeAnswers.put("a31", "Answer 3a");
        financeAnswers.put("a32", "Answer 3b");
        financeAnswers.put("a33", "Answer 3c");
        financeAnswers.put("a34", "Answer 3d");
        financeAnswers.put("a3", "2");
        financeAnswers.put("a41", "Answer 4a");
        financeAnswers.put("a42", "Answer 4b");
        financeAnswers.put("a43", "Answer 4c");
        financeAnswers.put("a44", "Answer 4d");
        financeAnswers.put("a4", "2");
    }

}
