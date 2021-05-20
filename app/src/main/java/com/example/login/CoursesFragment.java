package com.example.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CoursesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
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
        cv3 = view.findViewById(R.id.cv_course3);
        cv4 = view.findViewById(R.id.cv_course4);

        cv1.setOnClickListener(v -> {
            startCourse(1);
        });

        cv2.setOnClickListener(v -> {
            startCourse(2);
        });

        cv3.setOnClickListener(v -> {
            startCourse(3);
        });

        cv4.setOnClickListener(v -> {
            startCourse(4);
        });

        return view;
    }

    private void startCourse(int c) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.container, CourseFragment.newInstance());
        transaction.addToBackStack(null);
        transaction.commit();
    }
}