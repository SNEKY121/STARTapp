package com.example.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Calendar;

import static java.lang.Integer.parseInt;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TextView eUsername;
    private ProgressBar eBar;
    private FloatingActionButton eLogout;
    private TextView eLevel;
    private TextView eCursuri;
    private TextView eStreak;

    private String username;
    private Integer xp = 0;


    public ProfileFragment() {
        // Required empty public constructor
    }
    Connection connect;
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // TODO: Rename and change types of parameters
            username = getArguments().getString(ARG_PARAM1);
            String mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        eUsername = view.findViewById(R.id.tv_username);
        eUsername.setText(username);
        eLogout = view.findViewById(R.id.home_fabLogout);
        eStreak = view.findViewById(R.id.tv_streak);
        eLevel = view.findViewById(R.id.tv_level);
        eCursuri = view.findViewById(R.id.tv_cursuri);
        eBar = view.findViewById(R.id.pb_XP);

        getData();

        eLogout.setOnClickListener(v -> {
            resetPref();
            Intent intent = new Intent(getActivity(), GetStartedPage.class);
            startActivity(intent);
        });
        return view;
    }

    private void resetPref() {
        getActivity().getSharedPreferences(GetStartedPage.PREFS_NAME, 0)
                .edit()
                .putString(GetStartedPage.PREF_USERNAME, null)
                .apply();
    }

    private void getData() {
        try {
            SQLConnection connectionHelper = new SQLConnection();
            connect = connectionHelper.connectionclass();
            if (connect != null) {
                String query = "Select * from " + SQLConnection.profilesTable;
                Statement st = connect.createStatement();
                ResultSet rs = st.executeQuery(query);

                while (rs.next())
                    if (username.equals(rs.getString(1))) {
                        xp = parseInt(rs.getString(4));
                        eCursuri.setText(rs.getString(2) + " cursuri finalizate");
                        eStreak.setText(getStreaks() + " day streak");
                        eLevel.setText("Nivel " + xp/100);
                        eBar.setProgress(xp);
                        PreparedStatement stmt = connect.prepareStatement("UPADTE " + SQLConnection.profilesTable + " SET Xp = ? WHERE Username = ?");
                        stmt.setInt(1, xp);
                        stmt.setString(2, username);
                        stmt.executeUpdate();
                        return;
                    }
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private int getStreaks() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("StreaksKey", Context.MODE_PRIVATE);
        Calendar c = Calendar.getInstance();

        int thisDay = c.get(Calendar.DAY_OF_YEAR); // GET THE CURRENT DAY OF THE YEAR
        int lastDay = sharedPreferences.getInt("LAST_DAY", 0); //If we don't have a saved value, use 0.
        int counterOfConsecutiveDays = sharedPreferences.getInt("STREAKS_COUNTER", 0); //If we don't have a saved value, use 0.

        if(lastDay == thisDay - 1){
            counterOfConsecutiveDays = counterOfConsecutiveDays + 1;
            sharedPreferences.edit().putInt("THIS_DAY", thisDay);
            sharedPreferences.edit().putInt("STREAKS_COUNTER", counterOfConsecutiveDays).commit();
            xp = xp + 10 * counterOfConsecutiveDays;
        } else {
            sharedPreferences.edit().putInt("THIS_DAY", thisDay);
            sharedPreferences.edit().putInt("STREAKS_COUNTER", 1).commit();
        }
        return counterOfConsecutiveDays;
    }
}
