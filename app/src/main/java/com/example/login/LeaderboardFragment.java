package com.example.login;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;

import android.text.Layout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LeaderboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LeaderboardFragment extends Fragment {

    private View view;

    public LeaderboardFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static LeaderboardFragment newInstance() {
        LeaderboardFragment fragment = new LeaderboardFragment();
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
        view = inflater.inflate(R.layout.fragment_leaderboard, container, false);

        setData();

        return view;
    }

    private void setData() {
        Connection connect = SQLConnection.getConnection();
        try {
            if (connect != null) {
                PreparedStatement statement = connect.prepareStatement("SELECT Username, Xp FROM " + SQLConnection.PROFILES_TABLE + " ORDER BY Xp DESC");
                ResultSet resultSet = statement.executeQuery();

                int index = 1;
                while (resultSet.next())
                    makeConstraint(resultSet.getString(1), resultSet.getString(2), index++);
            }
        } catch (Exception e) {
            Log.e("setData: ", e.toString());
        }
    }

    private void makeConstraint(String username, String xp, int index) {
        LinearLayout linearlayout = view.findViewById(R.id.llLeaderboard);
        ConstraintLayout constraintLayout = new ConstraintLayout(getContext());
        ConstraintSet constraintSet = new ConstraintSet();
        TextView placement = new TextView(getContext());
        TextView user = new TextView(getContext());
        TextView score = new TextView(getContext());

        setTextView(placement);
        placement.setText("#" + index);
        setTextView(user);
        user.setText(username);
        setTextView(score);
        score.setText(xp + "xp");

        addViewsToConstraintLayout(constraintLayout, placement);
        addViewsToConstraintLayout(constraintLayout, user);
        addViewsToConstraintLayout(constraintLayout, score);

        constraintSet.clone(constraintLayout);
        constraintSet.connect(placement.getId(), ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT, pxToDp(10));
        constraintSet.connect(placement.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        constraintSet.connect(placement.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
        constraintSet.connect(user.getId(), ConstraintSet.LEFT, placement.getId(), ConstraintSet.RIGHT);
        constraintSet.connect(user.getId(), ConstraintSet.RIGHT, score.getId(), ConstraintSet.LEFT);
        constraintSet.connect(user.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        constraintSet.connect(user.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
        constraintSet.connect(score.getId(), ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT, pxToDp(10));
        constraintSet.connect(score.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP);
        constraintSet.connect(score.getId(), ConstraintSet.BOTTOM, ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM);
        constraintSet.applyTo(constraintLayout);

        linearlayout.addView(constraintLayout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, pxToDp(60)));

    }

    private void addViewsToConstraintLayout(ConstraintLayout constraintLayout, TextView tv) {
        constraintLayout.addView(tv, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
    }

    private void setTextView(TextView tv) {
        //tv.setLayoutParams(new ViewGroup.LayoutParams(60, ViewGroup.LayoutParams.MATCH_PARENT));
        tv.setId(View.generateViewId());
        tv.setGravity(Gravity.CENTER);
        tv.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tv.setTextSize(30);

    }

    private int pxToDp(int dp) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}