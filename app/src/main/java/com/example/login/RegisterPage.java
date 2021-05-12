package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegisterPage extends AppCompatActivity {

    private EditText eEmail;
    private EditText eName;
    private EditText ePassword;
    private EditText ePasswordConfirm;

    Connection connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);

        eEmail = findViewById(R.id.etEmail);
        eName = findViewById(R.id.etName);
        ePassword = findViewById(R.id.etPassword);
        ePasswordConfirm = findViewById(R.id.etPasswordConfirm);
        Button eRegister = findViewById(R.id.btnRegister);
        FloatingActionButton eReturn = findViewById(R.id.register_btnReturn);

        eRegister.setOnClickListener(v -> {
            String inputEmail = eEmail.getText().toString();
            String inputName = eName.getText().toString();
            String inputPassword = ePassword.getText().toString();
            String inputPasswordConfirm = ePasswordConfirm.getText().toString();

            if (CheckCreds(inputEmail, inputName, inputPassword, inputPasswordConfirm)) {
                if(SubmitRegister(inputName, inputEmail, inputPassword)) {
                    Toast.makeText(RegisterPage.this, "Inregistrat!", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(RegisterPage.this, GetStartedPage.class);
                    startActivity(intent);
                }
            } else {
                ePassword.getText().clear();
                ePasswordConfirm.getText().clear();
            }
        });

        eReturn.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterPage.this, GetStartedPage.class);
            startActivity(intent);
        });
    }

    private long pressedTime;

    @Override
    public void onBackPressed() {

        if (pressedTime + 2000 > System.currentTimeMillis()) {
            super.onBackPressed();
            finish();
        } else {
            String close = getResources().getString(R.string.close_app);
            Toast.makeText(getBaseContext(), close, Toast.LENGTH_SHORT).show();
        }
        pressedTime = System.currentTimeMillis();
    }

    public boolean SubmitRegister(String usr, String email, String pass) {
        try {
            SQLConnection connectionHelper = new SQLConnection();
            connect = connectionHelper.connectionclass();
            if (connect != null) {
                pass = pass.trim();
                pass = PasswordHash.generate(pass, null);
                PreparedStatement stmt = connect.prepareStatement("INSERT INTO Table1(Username, Email, Password, Salt) VALUES (?, ?, ?, ?)");
                stmt.setString(1, usr.trim());
                stmt.setString(2, email.trim());
                stmt.setString(3, pass);
                stmt.setBytes(4, PasswordHash.Salt);

                stmt.executeUpdate();
                return true;
            } else {
                Toast.makeText(RegisterPage.this, "Connection Failed.", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            Toast.makeText(RegisterPage.this, "Connection Error!", Toast.LENGTH_SHORT).show();
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.FINE, "lol? : ", ex);
        }
        return false;
    }

    private boolean CheckCreds(String email, String name, String password, String passwordConfirm) {
        if (email.contains("@") && email.contains(".") && email.length() > 4) {
            if (EmailCheck(email, name)) {
                if (password.equals(passwordConfirm)) {
                    if (password.length() > 7)
                        return true;
                    Toast.makeText(RegisterPage.this, "Introduceti o parola de macar 8 caractere", Toast.LENGTH_LONG).show();
                    return false;
                }
                Toast.makeText(RegisterPage.this, "Parolele nu coincid!", Toast.LENGTH_LONG).show();
                return false;
            }
            return false;
        }
        Toast.makeText(RegisterPage.this, "Email invalid!", Toast.LENGTH_LONG).show();
        return false;
    }

    private boolean EmailCheck(String email, String name) {
        try {
            SQLConnection connectionHelper = new SQLConnection();
            connect = connectionHelper.connectionclass();
            if (connect != null) {
                String query = "Select * from Table1";
                Statement st = connect.createStatement();
                ResultSet rs = st.executeQuery(query);

                while (rs.next()) {
                    if (email.equals(rs.getString(2))) {
                        Toast.makeText(RegisterPage.this, "Email deja inregistrat!", Toast.LENGTH_LONG).show();
                        eEmail.getText().clear();
                        return false;
                    } else if (name.equals(rs.getString(1))) {
                        Toast.makeText(RegisterPage.this, "Nume Utilizator Existent!", Toast.LENGTH_LONG).show();
                        eName.getText().clear();
                        return false;
                    }
                }
            }
        } catch (Exception ex) {
            Toast.makeText(RegisterPage.this, "Connection Failed.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}