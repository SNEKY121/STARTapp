package com.example.login;

import android.content.Context;
import android.graphics.drawable.RotateDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static com.example.login.SQLConnection.CAPITOLE_TABLE;
import static com.example.login.SQLConnection.COURSESUSERS_TABLE;
import static com.example.login.SQLConnection.COURSES_TABLE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StartCourseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StartCourseFragment extends Fragment {
    public StartCourseFragment() {
        // Required empty public constructor
    }
    Connection connect = SQLConnection.getConnection();
    private static int course_id;
    private int numberOfQuestions;

    // TODO: Rename and change types and number of parameters
    public static StartCourseFragment newInstance(int courseId) {
        StartCourseFragment fragment = new StartCourseFragment();
        course_id = courseId;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // create ContextThemeWrapper from the original Activity Context with the custom theme
        final Context contextThemeWrapper = new ContextThemeWrapper(getActivity(), R.style.Theme_CursSDV);

        // clone the inflater using the ContextThemeWrapper
        LayoutInflater localInflater = inflater.cloneInContext(contextThemeWrapper);

        // inflate the layout using the cloned inflater, not default inflater
        View view = localInflater.inflate(R.layout.fragment_startcourse, container, false);


        ImageButton btnChapter1 = view.findViewById(R.id.btn_node1);
        ImageButton btnChapter2 = view.findViewById(R.id.btn_node2);
        ImageButton btnChapter3 = view.findViewById(R.id.btn_node3);
        TextView chapterProgress = view.findViewById(R.id.course_progress1);
        //ProgressBar progressBar = view.findViewById(R.id.course_progress2);
        TextView tvProgress = view.findViewById(R.id.progress_percent);
        ImageView lock2 = view.findViewById(R.id.lock2);
        ImageView lock3 = view.findViewById(R.id.lock3);

        setClickable(btnChapter1,btnChapter2,btnChapter3, lock2, lock3);

        btnChapter1.setOnClickListener(v -> {
            addUserToFinanceTable();
            getCapitol(1);
            getQuestions();
        });
	
	int capitoleTerminate = CoursesFragment.getCapitol();
        if (capitoleTerminate > 0)
            capitoleTerminate--;

        chapterProgress.setText(capitoleTerminate + "/" + getNumarCapitole());
        //progressBar.setProgress(CoursesFragment.getProgress());
        tvProgress.setText(String.valueOf(CoursesFragment.getProgress()) + "%");

        return view;
    }

    private void setClickable(ImageButton btn1, ImageButton btn2, ImageButton btn3, ImageView lock2, ImageView lock3){

        btn1.setClickable(true);
        btn2.setClickable(false);
        btn3.setClickable(false);
        lock2.setVisibility(View.VISIBLE);
        lock3.setVisibility(View.VISIBLE);

        /*
        HomePage.user.getUsername()
        course.getChapters()
                vector bools (true false true false)
        if vector[1] == true s
        */

    }

    private void getCapitol(int index) {
        try {
            if (connect != null) {
                PreparedStatement checkUsernameStatement = connect.prepareStatement("SELECT * FROM " + CAPITOLE_TABLE + " WHERE CapitolIndex = ?");
                checkUsernameStatement.setInt(1, index);
                ResultSet resultSet = checkUsernameStatement.executeQuery();
                resultSet.next();
                if (resultSet.getString(1) != null) {
                    numberOfQuestions = resultSet.getInt(6);
                    //other stuff
                }
            }
        } catch (Exception e) {
            Log.e("getCapitol: ", e.toString());
        }
    }

    private void addUserToFinanceTable() {
        try {
            if (connect != null) {
                PreparedStatement checkUsernameStatement = connect.prepareStatement("SELECT * FROM " + COURSESUSERS_TABLE + " WHERE Username = ?");
                checkUsernameStatement.setString(1, HomePage.user.getUsername());
                ResultSet resultSet = checkUsernameStatement.executeQuery();
                resultSet.next();

                if (resultSet.getString(2) == null) {
                    PreparedStatement createProfileStatement = connect.prepareStatement("INSERT INTO " + COURSESUSERS_TABLE + " (Username, CourseId, Progress, Capitol, LastQuestion) VALUES (?,?, ?, ?, ?)");
                    createProfileStatement.setString(1, HomePage.user.getUsername());
                    createProfileStatement.setInt(2, course_id);
                    createProfileStatement.setInt(3, 0);
                    createProfileStatement.setInt(4, 1);
                    createProfileStatement.setInt(5, 1);
                    createProfileStatement.execute();
                }
            }
        } catch (Exception e) {
            Log.e("addUserToFinanceTable: ", e.toString());
        }
    }

    private void getQuestions() {
        int lastQuestion = CoursesFragment.getLastQuestion();
        if (lastQuestion == 0)
            lastQuestion = 1;
        if (lastQuestion + 1 < numberOfQuestions)
            lastQuestion++;
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, QuestionnaireFragment.newInstance(lastQuestion, course_id, numberOfQuestions));
        transaction.commit();
    }

    private int getNumarCapitole() {
        try {
            if (connect != null) {
                PreparedStatement statement = connect.prepareStatement("SELECT NumarCapitole FROM " + COURSES_TABLE + " WHERE id = ?");
                statement.setInt(1, course_id );
                ResultSet resultSet = statement.executeQuery();
                resultSet.next();
                return resultSet.getInt(1);
            }
        } catch (Exception e) {
            Log.e("getCourse: ", e.toString());
        }
        return 0;
    }

    public static int getCourse_id() {
        return course_id;
    }

}