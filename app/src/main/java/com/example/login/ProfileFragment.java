package com.example.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.io.File;
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
    private TextView eUsername;
    private ProgressBar eBar;
    private TextView eLevel;
    private TextView eCursuri;
    private TextView eStreak;
    public static ImageView eAvatar;

    private String username;
    private Integer xp = 0;


    public ProfileFragment() {
        // Required empty public constructor
    }
    Connection connect;

    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // TODO: Rename and change types of parameters
            username = getArguments().getString(ARG_PARAM1);
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        eUsername = view.findViewById(R.id.tv_username);
        eUsername.setText(username);

        eStreak = view.findViewById(R.id.tv_streak);
        eLevel = view.findViewById(R.id.tv_level);
        eCursuri = view.findViewById(R.id.tv_cursuri);
        eBar = view.findViewById(R.id.pb_XP);
        eAvatar = view.findViewById(R.id.iv_avatar);

        getData();

        eAvatar.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogTheme);
            View dialogLayout = inflater.inflate(R.layout.profilepic_popup, null);
            final AlertDialog dialog = builder.create();

            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            dialog.setView(dialogLayout, 0, 0, 0, 0);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            //dialog.getWindow().getDecorView().setPadding(0,0,0,0);
            WindowManager.LayoutParams wlmp = dialog.getWindow().getAttributes();
            wlmp.gravity = Gravity.BOTTOM;

            Button btnUpload = dialogLayout.findViewById(R.id.btnUpload);
            Button btnTake = dialogLayout.findViewById(R.id.btnTake);
            Button btnCancel = dialogLayout.findViewById(R.id.btnCancel);

            btnUpload.setOnClickListener(v3 -> {
                getImageFromAlbum();
                dialog.dismiss();
            });

            btnTake.setOnClickListener(v4 -> {
                dialog.dismiss();
            });

            btnCancel.setOnClickListener(v5 -> {
                dialog.dismiss();
            });

            builder.setView(dialogLayout);
            dialog.show();
        });

        return view;
    }



    private void getData() {
        try {
            SQLConnection connectionHelper = new SQLConnection();
            connect = connectionHelper.connectionclass();
            if (connect != null) {
                String query = "SELECT * FROM " + SQLConnection.profilesTable;
                Statement st = connect.createStatement();
                ResultSet rs = st.executeQuery(query);

                while (rs.next())
                    if (username.equals(rs.getString(1))) {
                        xp = rs.getInt(4);
                        eCursuri.setText(rs.getString(2) + " cursuri finalizate");
                        eStreak.setText(getStreaks() + " day streak");
                        eLevel.setText("Nivel " + xp/100);
                        eBar.setProgress(xp);

                        setXp();
                        return;
                    }
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
            Log.e("getData() ", e.toString());
        }
    }

    private void setXp() {
        try {
            SQLConnection connectionHelper = new SQLConnection();
            connect = connectionHelper.connectionclass();
            if (connect != null) {
                PreparedStatement stmt = connect.prepareStatement("UPDATE " + SQLConnection.profilesTable + " SET Xp = ? WHERE Username = ?");
                stmt.setInt(1, xp);
                stmt.setString(2, username);
                stmt.executeUpdate();
            }
        } catch (Exception e) {
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
            Log.e("setXp() ", e.toString());
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

    private void getImageFromAlbum() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 100);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode != getActivity().RESULT_CANCELED) {
            switch (requestCode) {
                case 101:
                    if (resultCode == getActivity().RESULT_OK && data != null) {
                        Bitmap Image = (Bitmap) data.getExtras().get("data");
                        eAvatar.setImageBitmap(Image);
                    }

                    break;
                case 100:
                    Uri selectedImageUri = data.getData();
                    if (selectedImageUri != null) {
                        eAvatar.setImageURI(selectedImageUri);
                    } else {
                        Toast.makeText(getActivity(), "no data", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    }
}
