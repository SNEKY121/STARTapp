package com.example.login;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.renderscript.Element;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
    private static User USER = null;

    private TextView eNotifs;
    private SwitchCompat eNotifsbtn;
    private TextView eEmail;
    private TextView eUsername;
    private TextView eFeedback;
    private TextView eLogout;

    private String username;
    private String newUsername;
    private String email;
    private String newEmail;
    private String feedbackText;



    Connection connect;

    public SettingsFragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(User user) {
        SettingsFragment fragment = new SettingsFragment();
        //Bundle args = new Bundle();
        USER = user;
        //fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            email = getArguments().getString(ARG_PARAM1);
            username = getArguments().getString(ARG_PARAM2);
        } else email = "null";*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        eNotifs = view.findViewById(R.id.tv_notifs);
        eNotifsbtn = view.findViewById(R.id.sw_notifs);
        eEmail = view.findViewById(R.id.tv_email);
        eUsername = view.findViewById(R.id.tv_username);
        eFeedback = view.findViewById(R.id.tv_feedback);
        eLogout = view.findViewById(R.id.tv_logout);

        eNotifs.setOnClickListener(v -> eNotifsbtn.setChecked(!eNotifsbtn.isChecked()));

        eLogout.setOnClickListener(vv -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.DialogTheme);
            View dialogLayout = inflater.inflate(R.layout.logout_popup, null);
            final AlertDialog dialog = builder.create();
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            dialog.setView(dialogLayout, 0, 0, 0, 0);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);

            WindowManager.LayoutParams wlmp = dialog.getWindow().getAttributes();
            wlmp.gravity = Gravity.BOTTOM;

            Button btnLogout = dialogLayout.findViewById(R.id.btnLogout);
            Button btnCancel = dialogLayout.findViewById(R.id.btnCancel);

            btnLogout.setOnClickListener(v1 -> {
                dialog.dismiss();
                resetPref();
                Intent intent = new Intent(getActivity(), GetStartedPage.class);
                startActivity(intent);
            });
            btnCancel.setOnClickListener(v2 -> dialog.dismiss());

            builder.setView(dialogLayout);
            dialog.show();

        });

        eEmail.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.DialogTheme);
            View dialogLayout = inflater.inflate(R.layout.datachange_popup, null);
            final AlertDialog dialog = builder.create();
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            dialog.setView(dialogLayout, 0, 0, 0, 0);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);
            
            WindowManager.LayoutParams wlmp = dialog.getWindow().getAttributes();
            wlmp.gravity = Gravity.BOTTOM;

            Button btnChange = dialogLayout.findViewById(R.id.btnChange);
            Button btnCancel = dialogLayout.findViewById(R.id.btnCancel);
            TextView tvData = dialogLayout.findViewById(R.id.tv_datashow);
            EditText etData = dialogLayout.findViewById(R.id.etNewData);

            tvData.setText(USER.getEmail());
            builder.setView(tvData);

            btnChange.setOnClickListener(v1 -> {
                newEmail = etData.getText().toString();
                trySubmit(USER.getEmail(), newEmail, "Email");
                dialog.dismiss();
            });
            btnCancel.setOnClickListener(v2 -> dialog.dismiss());


            
            builder.setView(dialogLayout);
            dialog.show();
        });

        eUsername.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.DialogTheme);
            View dialogLayout = inflater.inflate(R.layout.datachange_popup, null);
            final AlertDialog dialog = builder.create();
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            dialog.setView(dialogLayout, 0, 0, 0, 0);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);

            WindowManager.LayoutParams wlmp = dialog.getWindow().getAttributes();
            wlmp.gravity = Gravity.BOTTOM;

            Button btnChange = dialogLayout.findViewById(R.id.btnChange);
            Button btnCancel = dialogLayout.findViewById(R.id.btnCancel);
            TextView tvData = dialogLayout.findViewById(R.id.tv_datashow);
            EditText etData = dialogLayout.findViewById(R.id.etNewData);
            TextView tvTitle = dialogLayout.findViewById(R.id.tv_title);

            tvData.setText(USER.getUsername());
            builder.setView(tvData);

            etData.setHint("Nume utilizator nou");
            builder.setView(etData);

            tvTitle.setText("Schimba nume utlizator");
            builder.setView(tvTitle);

            btnChange.setOnClickListener(v1 -> {
                newUsername = etData.getText().toString();
                trySubmit(USER.getUsername(), newUsername, "Username");
                dialog.dismiss();
            });
            btnCancel.setOnClickListener(v2 -> dialog.dismiss());

            builder.setView(dialogLayout);
            dialog.show();
        });

        eFeedback.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.DialogTheme);
            View dialogLayout = inflater.inflate(R.layout.feedback_popup, null);
            final AlertDialog dialog = builder.create();
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            dialog.setView(dialogLayout, 0, 0, 0, 0);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);

            WindowManager.LayoutParams wlmp = dialog.getWindow().getAttributes();
            wlmp.gravity = Gravity.BOTTOM;

            Button btnChange = dialogLayout.findViewById(R.id.btnChange);
            Button btnCancel = dialogLayout.findViewById(R.id.btnCancel);
            EditText etText = dialogLayout.findViewById(R.id.etText);

            btnChange.setOnClickListener(v1 -> {
                feedbackText = etText.getText().toString();
                trySend(feedbackText);
                dialog.dismiss();
            });
            btnCancel.setOnClickListener(v2 -> dialog.dismiss());

            builder.setView(dialogLayout);
            dialog.show();
        });

        return view;
    }

    private void trySubmit(String currentData, String newData, String dataType) {
        if (newData.length()>4) {
            if (!currentData.equals(newData)) {
                try {
                    SQLConnection connectionHelper = new SQLConnection();
                    connect = connectionHelper.connectionclass();
                    if (connect != null) {
                        boolean k = true;
                        String query = "Select * from " + SQLConnection.accountsTable;
                        Statement st = connect.createStatement();
                        ResultSet rs = st.executeQuery(query);
                        while (rs.next()) {
                            if (newData.equals(rs.getString(1)) || newData.equals(rs.getString(2)))
                                k = false;
                        }
                        if (k) {
                            if (dataType.equals("Username")) {
                                PreparedStatement stmt1 = connect.prepareStatement("UPDATE " + SQLConnection.profilesTable + " SET Username = ? WHERE Username = ?");
                                stmt1.setString(1, newData);
                                stmt1.setString(2, currentData);
                                stmt1.executeUpdate();
                            }
                            PreparedStatement stmt = connect.prepareStatement("UPDATE " + SQLConnection.accountsTable + " SET " + dataType + "= ? WHERE " + dataType + " = ?");
                            stmt.setString(1, newData);
                            stmt.setString(2, currentData);
                            try {
                                stmt.executeUpdate();
                                Toast.makeText(getContext(), "Succes", Toast.LENGTH_LONG).show();
                                if (dataType.equals("Username"))
                                    USER.setUsername(newData);
                                else USER.setEmail(newData);
                                /*GetStartedPage.specialLogin = true;
                                Intent intent = new Intent(getContext(), GetStartedPage.class);
                                startActivity(intent);*/
                            } catch (Exception e) {
                                Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(getContext(), newData + " deja exista", Toast.LENGTH_LONG).show();
                        }
                    }
                } catch (Exception e) {
                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                }
            } else Toast.makeText(getActivity(), "date identice", Toast.LENGTH_SHORT).show();
        } else Toast.makeText(getActivity(), "introduceti macar 5 caractere", Toast.LENGTH_LONG).show();
    }

    private void trySend(String Data) {
        if (Data.length()>10) {
            final String to = "sebastianturbut@gmail.com";
            String subject = "New Feedback from " + username;

            Intent email = new Intent(Intent.ACTION_SEND);
            email.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
            email.putExtra(Intent.EXTRA_SUBJECT, subject);
            email.putExtra(Intent.EXTRA_TEXT, Data);

            email.setType("message/rfc822");

            startActivity(Intent.createChooser(email, "Choose an Email client :"));
        } else Toast.makeText(getActivity(), "Textul e prea scurt!", Toast.LENGTH_SHORT).show();
    }

    private void resetPref() {
        getActivity().getSharedPreferences(GetStartedPage.PREFS_NAME, 0)
                .edit()
                .putString(GetStartedPage.PREF_USERNAME, null)
                .apply();
    }
}