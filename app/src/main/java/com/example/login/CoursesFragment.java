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
import android.widget.Toast;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class CoursesFragment extends Fragment {

    private CardView cv1;
    private CardView cv2;
    private CardView cv3;
    private CardView cv4;

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

        cv1 = view.findViewById(R.id.cv_course1);
        cv2 = view.findViewById(R.id.cv_course2);
        cv2.setClickable(false);
        cv3 = view.findViewById(R.id.cv_course3);
        cv3.setClickable(false);
        cv4 = view.findViewById(R.id.cv_course4);
        cv4.setClickable(false);

        cv1.setOnClickListener(v -> {
            if (!checkEnrolled(SQLConnection.FINANCE_TABLE))
                startCourse();
            else resumeCourse();
        });

        /*cv2.setOnClickListener(v -> {
            startCourse(2);
        });

        cv3.setOnClickListener(v -> {
            startCourse(3);
        });

        cv4.setOnClickListener(v -> {
            startCourse(4);
        });*/

        return view;
    }

    private void resumeCourse() {
        /*FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container, CourseFragment.newInstance());
        transaction.addToBackStack(null);
        transaction.commit();*/
        Toast.makeText(getContext(), "Finance Course", Toast.LENGTH_LONG).show();
    }

    private void startCourse() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container, CourseFragment.newInstance());
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private boolean checkEnrolled(String table) {
        Connection connect = SQLConnection.getConnection();
        try {
            if (connect != null) {
                PreparedStatement statement = connect.prepareStatement("SELECT * FROM " + table + " WHERE Username = ?");
                statement.setString(1, HomePage.user.getUsername());
                ResultSet resultSet = statement.executeQuery();
                resultSet.next();

                if (resultSet.getString(1) != null) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (Exception e) {
            Log.e("checkEnrolled: ", e.toString());
        }
        return false;
    }
}