package com.example.login;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.zip.Inflater;

import static com.example.login.SQLConnection.COURSESUSERS_TABLE;
import static com.example.login.SQLConnection.QUESTIONS_TABLE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuestionnaireFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuestionnaireFragment extends Fragment {
    private static int questionNumber;
    private static int capitol_id;
    private static int numberOfQuestions;
    Connection connect = SQLConnection.getConnection();
    private String explanation;
    private String goodAnswer;
    private int xp;

    public QuestionnaireFragment() {
        // Required empty public constructor
    }

    public static QuestionnaireFragment newInstance(int question, int capitolid, int numberofquestions) {
        QuestionnaireFragment fragment = new QuestionnaireFragment();
        questionNumber = question;
        capitol_id = capitolid;
        numberOfQuestions = numberofquestions;
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

        ((HomePage)getActivity()).updateStatusBarColor("#7A1A85");

        TextView Question = view.findViewById(R.id.tv_question);
        Button Answer1 = view.findViewById(R.id.btn_answer1);
        Button Answer2 = view.findViewById(R.id.btn_answer2);
        Button Answer3 = view.findViewById(R.id.btn_answer3);
        Button Answer4 = view.findViewById(R.id.btn_answer4);
        TextView QuestionCounter = view.findViewById(R.id.tv_questionCounter);

        setData(Question, Answer1, Answer2, Answer3, Answer4);

        QuestionCounter.setText(questionNumber + "/" + numberOfQuestions);

        Answer1.setOnClickListener(v -> {
            if (Answer1.getText().equals(goodAnswer))
                nextQuestion();
            else showExplanation(inflater);
        });
        Answer2.setOnClickListener(v -> {
            if (Answer2.getText().equals(goodAnswer))
                nextQuestion();
            else showExplanation(inflater);
        });
        Answer3.setOnClickListener(v -> {
            if (Answer3.getText().equals(goodAnswer))
                nextQuestion();
            else showExplanation(inflater);
        });
        Answer4.setOnClickListener(v -> {
            if (Answer4.getText().equals(goodAnswer))
                nextQuestion();
            else showExplanation(inflater);
        });

        return view;
    }

    private void nextQuestion() {
        setProgress();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        if (numberOfQuestions != questionNumber) {

            transaction.replace(R.id.container, newInstance(questionNumber + 1, capitol_id, numberOfQuestions));
        } else {
            setChapterCompleted();

            transaction.replace(R.id.container, CoursesFragment.newInstance());
        }
        transaction.commit();
    }

    private void setChapterCompleted() {
        try {
            if (connect != null) {
                if (capitol_id == CoursesFragment.getCapitol()) {
                    int course_id = StartCourseFragment.getCourse_id();
                    int capitolIndex = capitol_id;
                    PreparedStatement statement = connect.prepareStatement("UPDATE " + COURSESUSERS_TABLE + " SET Capitol = ?, LastQuestion = ? WHERE Username = ? AND CourseId = ?");
                    statement.setInt(1, capitolIndex + 1);
                    statement.setInt(2, 0);
                    statement.setString(3, HomePage.user.getUsername());
                    statement.setInt(4, course_id);
                    statement.executeUpdate();
                }
            }
        } catch (Exception e) {
            Log.e("setChapterCompleted: ", e.toString());
        }
    }

    private void showExplanation(LayoutInflater inflater) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.ExplanationTheme);
        View dialogLayout = inflater.inflate(R.layout.explanation_popup, null);
        final AlertDialog dialog = builder.create();
        setDialog(dialog, dialogLayout);

        Button btnCancel = dialogLayout.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(v2 -> dialog.dismiss());

        TextView tvExplanation = dialogLayout.findViewById(R.id.tv_showexplanation);
        tvExplanation.setText(explanation);

        builder.setView(dialogLayout);
        dialog.show();
    }

    private void setDialog(AlertDialog dialog, View dialogLayout) {
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.setView(dialogLayout, 0, 0, 0, 0);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);
    }

    private void setProgress() {
        try {
            if (connect != null) {
                float progress = HomePage.user.getProgress();
                if (capitol_id == CoursesFragment.getCapitol()) {
                    progress += (float)(1) / (float) (StartCourseFragment.getNrIntrebariCurs()) * 100;
                    HomePage.user.setProgress(progress);
                    PreparedStatement statement = connect.prepareStatement("UPDATE " + COURSESUSERS_TABLE + " SET Progress = ?, LastQuestion = ? WHERE Username = ? AND CourseId = ?");
                    statement.setFloat(1, progress);
                    statement.setInt(2, questionNumber);
                    statement.setString(3, HomePage.user.getUsername());
                    statement.setInt(4, StartCourseFragment.getCourse_id());
                    statement.execute();
                    HomePage.user.setXp(HomePage.user.getXp() + xp);
                }
            }
        } catch (Exception e) {
            Log.e("setProgress: ", e.toString());
        }
    }

    private void setData(TextView Question, Button Answer1, Button Answer2, Button Answer3, Button Answer4) {
        try {
            if (connect != null) {
                PreparedStatement statement = connect.prepareStatement("SELECT * FROM " + QUESTIONS_TABLE + " WHERE CapitolId = ? AND QuestionIndex = ?");
                statement.setInt(1, capitol_id);
                statement.setInt(2, questionNumber);
                ResultSet resultSet = statement.executeQuery();
                resultSet.next();

                if (resultSet.getInt(8) != 0) {
                    Integer[] qIndex = {2, 3, 4, 5};
                    shuffle(qIndex);

                    Question.setText(resultSet.getString(1));
                    Answer1.setText(resultSet.getString(qIndex[0]));
                    Answer2.setText(resultSet.getString(qIndex[1]));
                    Answer3.setText(resultSet.getString(qIndex[2]));
                    Answer4.setText(resultSet.getString(qIndex[3]));
                    explanation = resultSet.getString(7);
                    goodAnswer = resultSet.getString(2);
                    xp = resultSet.getInt(9);
                }
            }
        } catch (Exception e) {
            Log.e("setData", e.toString());
        }
    }

    private void shuffle(Integer[] arr) {
        List<Integer> intList = Arrays.asList(arr);
        Collections.shuffle(intList);
        intList.toArray(arr);
    }
}