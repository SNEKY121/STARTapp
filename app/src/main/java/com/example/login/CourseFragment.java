package com.example.login;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CourseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseFragment extends Fragment {

    private static final HashMap FinanceQuestions = new QuestionsAndAnswers().getFinanceQuestions();
    private static final HashMap FinanceAnswers = new QuestionsAndAnswers().getFinanceAnswers();
    private static String goodAnswer = "0";
    private static int questionNumber;

    public CourseFragment() {
        // Required empty public constructor
    }

    public static CourseFragment newInstance(int question) {
        CourseFragment fragment = new CourseFragment();
        questionNumber = question;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_course, container, false);

        TextView Question = view.findViewById(R.id.tv_question);
        Button Answer1 = view.findViewById(R.id.btn_answer1);
        Button Answer2 = view.findViewById(R.id.btn_answer2);
        Button Answer3 = view.findViewById(R.id.btn_answer3);
        Button Answer4 = view.findViewById(R.id.btn_answer4);

        setData(Question, Answer1, Answer2, Answer3, Answer4);

        Answer1.setOnClickListener(v -> {
            if (goodAnswer.equals("1")) {
                nextQuestion();
            } else {
                showExplanation();
            }
        });

        Answer2.setOnClickListener(v -> {
            if (goodAnswer.equals("2")) {
                nextQuestion();
            } else {
                showExplanation();
            }
        });

        Answer3.setOnClickListener(v -> {
            if (goodAnswer.equals("3")) {
                nextQuestion();
            } else {
                showExplanation();
            }
        });

        Answer4.setOnClickListener(v -> {
            if (goodAnswer.equals("4")) {
                nextQuestion();
            } else {
                showExplanation();
            }
        });

        return view;
    }

    private void nextQuestion() {
        setProgress();
        if (QuestionsAndAnswers.getFinanceTotalQuestions() != questionNumber) {
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.container, newInstance(questionNumber + 1));
            transaction.commit();
        } else {
            Toast.makeText(getContext(), "no more questions", Toast.LENGTH_SHORT).show();
        }
    }

    private void showExplanation() {
        Toast.makeText(getContext(), "so baaaad", Toast.LENGTH_SHORT).show();
    }

    private void setProgress() {
        Connection connect = SQLConnection.getConnection();
        try {
            if (connect != null) {
                int progress = questionNumber/QuestionsAndAnswers.getFinanceTotalQuestions()*100;

                PreparedStatement statement = connect.prepareStatement("UPDATE " + " SET Progress = ? WHERE Username = ?");
                statement.setInt(1, progress);
                statement.setString(2, HomePage.user.getUsername());
                statement.execute();
            }
        } catch (Exception e) {
            Log.e("setProgress: ", e.toString());
        }
    }

    private void setData(TextView Question, Button Answer1, Button Answer2, Button Answer3, Button Answer4) {
        Question.setText(FinanceQuestions.get("q" + questionNumber).toString());
        Answer1.setText(FinanceAnswers.get("a"+ questionNumber + "1").toString());
        Answer2.setText(FinanceAnswers.get("a"+ questionNumber + "2").toString());
        Answer3.setText(FinanceAnswers.get("a"+ questionNumber + "3").toString());
        Answer4.setText(FinanceAnswers.get("a"+ questionNumber + "4").toString());
        goodAnswer = FinanceAnswers.get("a" + questionNumber).toString();
    }
}