package com.example.login;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.login.SQLConnection.COURSESUSERS_TABLE;
import static com.example.login.SQLConnection.COURSES_TABLE;


public class CoursesFragment extends Fragment {
    private static int progress = 0;
    Connection connect = SQLConnection.getConnection();
    private static int capitol;
    private static int lastQuestion;
    private static int numarCapitole;
    private int course_id;

    public CoursesFragment() {
        // Required empty public constructor
    }
    public static CoursesFragment newInstance() {
        CoursesFragment fragment = new CoursesFragment();
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
        View view = inflater.inflate(R.layout.fragment_courses, container, false);

        CardView cv1 = view.findViewById(R.id.cv_course1);
        ProgressBar financeProgressBar = view.findViewById(R.id.pb_course1);
        boolean isEnrolledFinance = checkEnrolled(getSet(COURSESUSERS_TABLE));
        if (isEnrolledFinance) {
            financeProgressBar.setVisibility(View.VISIBLE);
            financeProgressBar.setProgress(progress);
        }
        
        CardView cv2 = view.findViewById(R.id.cv_course2);
        cv2.setClickable(false);


        cv1.setOnClickListener(v -> {
            course_id = 1;
            getCourse(course_id);
            startCourse(course_id);
        });

        cv2.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Curs In Lucru", Toast.LENGTH_LONG).show();
        });

        return view;
    }

    private void startCourse(int id) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container, StartCourseFragment.newInstance(id));
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private boolean checkEnrolled(ResultSet resultSet) {
        try {
            if (resultSet != null) {
                //to be changed when more courses are added
                capitol = resultSet.getInt(5);
                //lastQuestion = resultSet.getInt(6);
                progress = resultSet.getInt(4);
                return progress != 100;
            }
        } catch (Exception e) {
            Log.e("checkEnrolled: ", e.toString());
        }
        return false;
    }

    private ResultSet getSet(String table) {
        try {
            if (connect != null) {
                PreparedStatement statement = connect.prepareStatement("SELECT * FROM " + table + " WHERE Username = ?");
                statement.setString(1, HomePage.user.getUsername());
                ResultSet resultSet = statement.executeQuery();
                resultSet.next();

                if (resultSet.getString(1) != null)
                    return resultSet;
            }
        } catch (Exception e) {
            Log.e("checkEnrolled: ", e.toString());
        }
        return null;
    }

    private void getCourse(int id) {
        try {
            if (connect != null) {
                PreparedStatement statement = connect.prepareStatement("SELECT * FROM " + COURSES_TABLE + " WHERE id = ?");
                statement.setInt(1, id);
                ResultSet resultSet = statement.executeQuery();
                resultSet.next();
                numarCapitole = resultSet.getInt(4);
            }
        } catch (Exception e) {
            Log.e("getCourse: ", e.toString());
        }
    }

    public static int getCapitol() {
        return capitol;
    }

    /*public static int getLastQuestion() {
        return lastQuestion;
    }*/

    public static int getProgress() {
        return progress;
    }
}