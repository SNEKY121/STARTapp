package com.example.login;

import android.media.Image;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

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



        ImageButton btnChapter1 = view.findViewById(R.id.btn_node1);
        ImageButton btnChapter2 = view.findViewById(R.id.btn_node2);
        ImageButton btnChapter3 = view.findViewById(R.id.btn_node3);

        ImageView lock2 = view.findViewById(R.id.lock3);
        ImageView lock3 = view.findViewById(R.id.lock3);

        setClickable(btnChapter1,btnChapter2,btnChapter3, lock2, lock3);

        btnChapter1.setOnClickListener(v -> {
            addUserToFinanceTable();
            getQuestions();
        });


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