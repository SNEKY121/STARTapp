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
import android.widget.Toast;

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
    private static int numberOfQuestions;
    private static int capitol_id;
    private static int nrCapitole;
    private static int nrIntrebariCurs;
    private boolean chapter2Locked = true;
    private boolean chapter3Locked = true;

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
        View view = inflater.inflate(R.layout.fragment_startcourse, container, false);

        ((HomePage)getActivity()).updateStatusBarColor("#7A1A85");

        ImageButton btnChapter1 = view.findViewById(R.id.btn_node1);
        ImageButton btnChapter2 = view.findViewById(R.id.btn_node2);
        ImageButton btnChapter3 = view.findViewById(R.id.btn_node3);
        TextView chapterProgress = view.findViewById(R.id.course_progress1);
        TextView tvProgress = view.findViewById(R.id.progress_percent);
        ImageView lock2 = view.findViewById(R.id.lock2);
        ImageView lock3 = view.findViewById(R.id.lock3);

        setClickable(lock2, lock3, btnChapter2, btnChapter3);

        btnChapter1.setOnClickListener(v -> {
            capitol_id = 1;
            addUserToFinanceTable();
            getCapitol(1);
            getQuestions();
        });

        btnChapter2.setOnClickListener(v -> {
            if (!chapter2Locked) {
                capitol_id = 2;
                addUserToFinanceTable();
                getCapitol(2);
                getQuestions();
            } else Toast.makeText(getContext(), "Completeaza capitolul anterior", Toast.LENGTH_SHORT).show();
        });

        btnChapter3.setOnClickListener(v -> {
            if (!chapter3Locked) {
            capitol_id = 3;
            addUserToFinanceTable();
            getCapitol(3);
            getQuestions();
            } else Toast.makeText(getContext(), "Completeaza capitolul anterior", Toast.LENGTH_SHORT).show();
        });
	
	    int capitoleTerminate = CoursesFragment.getCapitol();
        if (capitoleTerminate > 0)
            capitoleTerminate--;

        chapterProgress.setText(capitoleTerminate + "/" + getNumarCapitole());
        tvProgress.setText(CoursesFragment.getProgress() + "%");

        return view;
    }

    private void setClickable(ImageView lock2, ImageView lock3, ImageView ch2, ImageView ch3){
        int capitol = CoursesFragment.getCapitol();
        if (capitol >= 2) {
            chapter2Locked = false;
            lock2.setVisibility(View.INVISIBLE);
            ch2.setAlpha(1.0f);
            if (capitol >= 3) {
                chapter3Locked = false;
                lock3.setVisibility(View.INVISIBLE);
                ch3.setAlpha(1.0f);
            }
        }
    }

    private void getCapitol(int index) {
        try {
            if (connect != null) {
                PreparedStatement checkUsernameStatement = connect.prepareStatement("SELECT * FROM " + CAPITOLE_TABLE + " WHERE id = ?");
                checkUsernameStatement.setInt(1, index);
                ResultSet resultSet = checkUsernameStatement.executeQuery();
                resultSet.next();
                if (resultSet.getString(1) != null) {
                    numberOfQuestions = resultSet.getInt(5);
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
                PreparedStatement checkUsernameStatement = connect.prepareStatement("SELECT * FROM " + COURSESUSERS_TABLE + " WHERE Username = ? AND CourseId = ?");
                checkUsernameStatement.setString(1, HomePage.user.getUsername());
                checkUsernameStatement.setInt(2, course_id);
                ResultSet resultSet = checkUsernameStatement.executeQuery();
                resultSet.next();

                if (resultSet.getString(2) == null) {
                    PreparedStatement createProfileStatement = connect.prepareStatement("INSERT INTO " + COURSESUSERS_TABLE + " (Username, CourseId, Progress, Capitol, LastQuestion) VALUES (?,?, ?, ?, ?)");
                    createProfileStatement.setString(1, HomePage.user.getUsername());
                    createProfileStatement.setInt(2, course_id);
                    createProfileStatement.setInt(3, 0);
                    createProfileStatement.setInt(4, 1);
                    createProfileStatement.setInt(5, 0);
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
        /*if (lastQuestion + 1 < numberOfQuestions)
            lastQuestion++;*/
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, QuestionnaireFragment.newInstance(lastQuestion, capitol_id, numberOfQuestions));
        transaction.commit();
    }

    private int getNumarCapitole() {
        try {
            if (connect != null) {
                PreparedStatement statement = connect.prepareStatement("SELECT NumarCapitole, NumarIntrebari FROM " + COURSES_TABLE + " WHERE id = ?");
                statement.setInt(1, course_id );
                ResultSet resultSet = statement.executeQuery();
                resultSet.next();
                nrCapitole = resultSet.getInt(1);
                nrIntrebariCurs = resultSet.getInt(2);
                return nrCapitole;
            }
        } catch (Exception e) {
            Log.e("getCourse: ", e.toString());
        }
        return 0;
    }

    public static int getCourse_id() {
        return course_id;
    }

    public static int getNrCapitole() {
        return nrCapitole;
    }

    public static int getNrIntrebariCurs() {
        return nrIntrebariCurs;
    }

    /*public static int getCapitol_id() { return capitol_id; }

    public static int getTotalQuestions() {
        return numberOfQuestions;
    }*/

}