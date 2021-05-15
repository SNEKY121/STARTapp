package com.example.login;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private TextView eNotifs;
    private SwitchCompat eNotifsbtn;
    private TextView eProfilepic;
    private TextView eEmail;
    private TextView eUsername;
    private TextView eFeedback;

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
    public static SettingsFragment newInstance(String getEmail, String getUsername) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, getEmail);
        args.putString(ARG_PARAM2, getUsername);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            email = getArguments().getString(ARG_PARAM1);
            username = getArguments().getString(ARG_PARAM2);
        } else email = "null";
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        eNotifs = view.findViewById(R.id.tv_notifs);
        eNotifsbtn = view.findViewById(R.id.sw_notifs);
        eProfilepic = view.findViewById(R.id.tv_profile);
        eEmail = view.findViewById(R.id.tv_email);
        eUsername = view.findViewById(R.id.tv_username);
        eFeedback = view.findViewById(R.id.tv_feedback);

        eNotifs.setOnClickListener(v -> eNotifsbtn.setChecked(!eNotifsbtn.isChecked()));

        eEmail.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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

            tvData.setText(email);
            builder.setView(tvData);

            btnChange.setOnClickListener(v1 -> {
                newEmail = etData.getText().toString();
                trySubmit(email, newEmail, "Email");
                dialog.dismiss();
            });
            btnCancel.setOnClickListener(v2 -> dialog.dismiss());


            
            builder.setView(dialogLayout);
            dialog.show();
        });

        eUsername.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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

            tvData.setText(username);
            builder.setView(tvData);

            etData.setHint("Nume utilizator nou");
            builder.setView(etData);

            tvTitle.setText("Schimba nume utlizator");
            builder.setView(tvTitle);

            btnChange.setOnClickListener(v1 -> {
                newUsername = etData.getText().toString();
                trySubmit(username, newUsername, "Username");
                dialog.dismiss();
            });
            btnCancel.setOnClickListener(v2 -> dialog.dismiss());

            builder.setView(dialogLayout);
            dialog.show();
        });

        eFeedback.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
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

        eProfilepic.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            View dialogLayout = inflater.inflate(R.layout.profilepic_popup, null);
            final AlertDialog dialog = builder.create();
            dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            dialog.setView(dialogLayout, 0, 0, 0, 0);
            dialog.setCanceledOnTouchOutside(true);
            dialog.setCancelable(true);

            WindowManager.LayoutParams wlmp = dialog.getWindow().getAttributes();
            wlmp.gravity = Gravity.BOTTOM;

            Button btnUpload = dialogLayout.findViewById(R.id.btnUpload);
            Button btnTake = dialogLayout.findViewById(R.id.btnTake);

            btnUpload.setOnClickListener(v3 -> {
                dialog.dismiss();
            });

            btnTake.setOnClickListener(v4 -> {
                dialog.dismiss();
            });

            builder.setView(dialogLayout);
            dialog.show();
        });

        return view;
    }

    private void trySubmit(String currentData, String newData, String dataType) {
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
                        PreparedStatement stmt = connect.prepareStatement("UPDATE " + SQLConnection.accountsTable + " SET " + dataType + "= ? WHERE " + dataType + " = ?");
                        stmt.setString(1, newData);
                        stmt.setString(2, currentData);
                        try {
                            stmt.executeUpdate();
                            Toast.makeText(getContext(), "Succes", Toast.LENGTH_LONG).show();
                            GetStartedPage.specialLogin = true;
                            Intent intent = new Intent(getContext(), GetStartedPage.class);
                            startActivity(intent);
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
    }

    private void trySend(String Data) {
        final String to = "sebastianturbut@gmail.com";
        String subject = "New Feedback from " + username;

        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[] {to});
        email.putExtra(Intent.EXTRA_SUBJECT, subject);
        email.putExtra(Intent.EXTRA_TEXT, Data);

        email.setType("message/rfc822");

        startActivity(Intent.createChooser(email, "Choose an Email client :"));
    }
}