package com.example.login;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
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

import static com.example.login.SQLConnection.ACCOUNTS_TABLE;
import static com.example.login.SQLConnection.PROFILES_TABLE;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment {
    private static User USER = null;
    private SwitchCompat eNotifsbtn;
    private String newUsername;
    private String newPassword;
    private String feedbackText;



    Connection connect = SQLConnection.getConnection();

    public SettingsFragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static SettingsFragment newInstance(User user) {
        SettingsFragment fragment = new SettingsFragment();
        USER = user;
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
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        ((HomePage)getActivity()).updateStatusBarColor("#00B2E5");

        TextView eNotifs = view.findViewById(R.id.tv_notifs);
        eNotifsbtn = view.findViewById(R.id.sw_notifs);
        TextView eUsername = view.findViewById(R.id.tv_username);
        TextView ePassword = view.findViewById(R.id.tv_password);
        TextView eFeedback = view.findViewById(R.id.tv_feedback);
        TextView eLogout = view.findViewById(R.id.tv_logout);

        eNotifs.setOnClickListener(v -> eNotifsbtn.setChecked(!eNotifsbtn.isChecked()));

        eLogout.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.DialogTheme);
            View dialogLayout = inflater.inflate(R.layout.logout_popup, null);
            final AlertDialog dialog = builder.create();
            setDialog(dialog, dialogLayout);

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

        ePassword.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext(), R.style.DialogTheme);
            View dialogLayout = inflater.inflate(R.layout.datachange_popup, null);
            final AlertDialog dialog = builder.create();
            setDialog(dialog, dialogLayout);

            Button btnChange = dialogLayout.findViewById(R.id.btnChange);
            Button btnCancel = dialogLayout.findViewById(R.id.btnCancel);
            TextView tvData = dialogLayout.findViewById(R.id.tv_datashow);
            EditText etData = dialogLayout.findViewById(R.id.etNewData);
            TextView tvTitle = dialogLayout.findViewById(R.id.tv_title);

            tvData.setVisibility(View.GONE);

            etData.setHint("Parola Noua");
            builder.setView(etData);

            tvTitle.setText("Schimba parola");
            builder.setView(tvTitle);

            btnChange.setOnClickListener(v1 -> {
                newPassword = etData.getText().toString();
                changePass(newPassword);
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
            setDialog(dialog, dialogLayout);

            Button btnChange = dialogLayout.findViewById(R.id.btnChange);
            Button btnCancel = dialogLayout.findViewById(R.id.btnCancel);
            TextView tvData = dialogLayout.findViewById(R.id.tv_datashow);
            EditText etData = dialogLayout.findViewById(R.id.etNewData);
            TextView tvTitle = dialogLayout.findViewById(R.id.tv_title);

            tvData.setText(USER.getUsername());
            builder.setView(tvData);

            etData.setHint("Parola Noua");
            builder.setView(etData);

            tvTitle.setText("Schimba parola");
            builder.setView(tvTitle);

            btnChange.setOnClickListener(v1 -> {
                newUsername = etData.getText().toString();
                trySubmit(USER.getUsername(), newUsername);
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
            setDialog(dialog, dialogLayout);

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

    private void changePass(String pass) {
        pass = pass.trim();
        if (pass.length() > 7) {
            try {
                pass = PasswordHash.generate(pass, null);
                PreparedStatement stmt = connect.prepareStatement("UPDATE " + ACCOUNTS_TABLE + " SET Password = ?, Salt = ? WHERE Username = ?");
                stmt.setString(1, pass);
                stmt.setBytes(2, PasswordHash.Salt);
                stmt.setString(3, USER.getUsername());
                stmt.executeUpdate();
            } catch (Exception e) {
                Log.e("changePass: ", e.toString());
            }
        }
    }

    private void setDialog(AlertDialog dialog, View dialogLayout) {
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        dialog.setView(dialogLayout, 0, 0, 0, 0);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        WindowManager.LayoutParams wlmp = dialog.getWindow().getAttributes();
        wlmp.gravity = Gravity.BOTTOM;
    }

    private void trySubmit(String currentData, String newData) {
        if (newData.length()>4) {
            if (!currentData.equals(newData)) {
                try {
                    if (connect != null) {
                        PreparedStatement stmt = connect.prepareStatement("SELECT Username from " + ACCOUNTS_TABLE + " WHERE Username = ?");
                        stmt.setString(1, currentData);
                        ResultSet resultSet = stmt.executeQuery();
                        resultSet.next();

                        if (resultSet.getString(1) != null) {
                            PreparedStatement stmt1 = connect.prepareStatement("UPDATE " + PROFILES_TABLE + " SET Username = ? WHERE Username = ?");
                            stmt1.setString(1, newData);
                            stmt1.setString(2, currentData);
                            stmt1.execute();
                        }

                        PreparedStatement stmt1 = connect.prepareStatement("UPDATE " + ACCOUNTS_TABLE + " SET Username = ? WHERE Username = ?");
                        stmt1.setString(1, newData);
                        stmt1.setString(2, currentData);
                        try {
                            stmt1.executeUpdate();
                            Toast.makeText(getContext(), "Succes", Toast.LENGTH_LONG).show();
                            USER.setUsername(newData);
                            requireActivity().getSharedPreferences(GetStartedPage.PREFS_NAME, Context.MODE_PRIVATE).edit().putString(GetStartedPage.PREF_USERNAME, newData).apply();
                        } catch (Exception e) {
                            Toast.makeText(getContext(), e.toString(), Toast.LENGTH_LONG).show();
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
            String subject = "New Feedback from " + USER.getUsername();

            Intent email = new Intent(Intent.ACTION_SEND);
            email.putExtra(Intent.EXTRA_EMAIL, new String[]{to});
            email.putExtra(Intent.EXTRA_SUBJECT, subject);
            email.putExtra(Intent.EXTRA_TEXT, Data);

            email.setType("message/rfc822");

            startActivity(Intent.createChooser(email, "Choose an Email client :"));
        } else Toast.makeText(getActivity(), "Textul e prea scurt!", Toast.LENGTH_SHORT).show();
    }

    private void resetPref() {
        requireActivity().getSharedPreferences(GetStartedPage.PREFS_NAME, 0)
                .edit()
                .putString(GetStartedPage.PREF_USERNAME, null)
                .apply();
    }
}