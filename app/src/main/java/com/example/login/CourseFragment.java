package com.example.login;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

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
 * Use the {@link CourseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CourseFragment extends Fragment {
    public CourseFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static CourseFragment newInstance() {
        CourseFragment fragment = new CourseFragment();
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

        Button btnStart = view.findViewById(R.id.btn_start);
        btnStart.setOnClickListener(v -> {
            addUserToFinanceTable();
        });

        return view;
    }

    private void addUserToFinanceTable() {
        Connection connect = SQLConnection.getConnection();
        try {
            if (connect != null) {
                PreparedStatement statement = connect.prepareStatement("INSERT INTO " + FINANCE_TABLE + " (Username, Progress, Capitol) VALUES (?, ?, ?)");
                statement.setString(1, HomePage.user.getUsername());
                statement.setFloat(2, 0.0f);
                statement.setInt(3, 1);
                statement.execute();
            }
        } catch (Exception e) {
            Log.e("addUserToFinanceTable: ", e.toString());
        }
    }
}