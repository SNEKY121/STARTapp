package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class LoginPage extends AppCompatActivity {

    private EditText eName;
    private EditText ePassword;
    private Button eLogin;
    private FloatingActionButton eReturn;

    private boolean isValid = false;

    Connection connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);

        eName = findViewById(R.id.etName);
        ePassword = findViewById(R.id.etPassword);
        eLogin = findViewById(R.id.btnLogin);
        eReturn = findViewById(R.id.login_btnReturn);

        eLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputName = eName.getText().toString().trim();
                String inputPassword = ePassword.getText().toString().trim();

                if (inputName.isEmpty() || inputPassword.isEmpty()) {
                    Toast.makeText(LoginPage.this, "Completati toate formularele!", Toast.LENGTH_SHORT).show();
                } else {
                    isValid = checkCred(inputName, inputPassword);

                    if (!isValid) {
                        // fail message
                        Toast.makeText(LoginPage.this, "Date Introduse Invalide", Toast.LENGTH_LONG).show();
                        // clear password form
                        ePassword.getText().clear();
                    } else {
                        // clear login forms
                        ePassword.getText().clear();
                        eName.getText().clear();
                        // go to new activity
                        Intent intent = new Intent(LoginPage.this, HomePage.class);
                        startActivity(intent);
                    }
                }
            }
        });

        eReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginPage.this, GetStartedPage.class);
                startActivity(intent);
            }
        });
    }

    private boolean checkCred(String name, String password) {
        try {
            SQLConnection connectionHelper = new SQLConnection();
            connect = connectionHelper.connectionclass();
            if (connect != null) {
                String query = "Select * from Table1";
                Statement st = connect.createStatement();
                ResultSet rs = st.executeQuery(query);

                while (rs.next()) {
                    if (name.equals(rs.getString(1)) || name.equals(rs.getString(2))) {
                        password = PasswordHash.generate(password, rs.getBytes(4));
                        if (password.equals(rs.getString(3)))
                            return true;
                    }
                }
                return false;
            } else {
                Toast.makeText(LoginPage.this, "Verificati Conexiunea", Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
            Toast.makeText(LoginPage.this, "Eroare", Toast.LENGTH_LONG).show();
        }
        return false;
    }
}