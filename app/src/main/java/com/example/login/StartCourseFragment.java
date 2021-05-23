package com.example.login;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static com.example.login.SQLConnection.FINANCE_TABLE;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link StartCourseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StartCourseFragment extends Fragment {
    public StartCourseFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static StartCourseFragment newInstance() {
        StartCourseFragment fragment = new StartCourseFragment();
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
        View view = inflater.inflate(R.layout.fragment_startcourse, container, false);

        Button btnStart = view.findViewById(R.id.btn_start);
        btnStart.setOnClickListener(v -> {
            addUserToFinanceTable();
            getQuestions();
        });

        return view;
    }

    private void addUserToFinanceTable() {
        Connection connect = SQLConnection.getConnection();
        try {
            if (connect != null) {
                PreparedStatement statement = connect.prepareStatement("INSERT INTO " + FINANCE_TABLE + " (Username, Progress, Capitol, LastQuestion) VALUES (?, ?, ?, ?)");
                statement.setString(1, HomePage.user.getUsername());
                statement.setInt(2, 0);
                statement.setInt(3, 1);
                statement.setInt(4, 1);
                statement.execute();
            }
        } catch (Exception e) {
            Log.e("addUserToFinanceTable: ", e.toString());
        }
    }

    private void getQuestions() {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, CourseFragment.newInstance(1));
        transaction.commit();
    }
}