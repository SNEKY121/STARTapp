package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class GetStartedPage extends AppCompatActivity {

    private EditText eName;
    private EditText ePassword;

    private boolean isValid = false;

    Connection connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started_page);

        Button eRegister = findViewById(R.id.btnCreate);
        eName = findViewById(R.id.etName);
        ePassword = findViewById(R.id.etPassword);
        Button eLogin = findViewById(R.id.btnLogin);

        eLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputName = eName.getText().toString().trim();
                String inputPassword = ePassword.getText().toString().trim();

                if (inputName.isEmpty() || inputPassword.isEmpty()) {
                    Toast.makeText(GetStartedPage.this, "Completati toate formularele!", Toast.LENGTH_SHORT).show();
                } else {
                    isValid = checkCred(inputName, inputPassword);

                    if (!isValid) {
                        Toast.makeText(GetStartedPage.this, "Date Introduse Invalide", Toast.LENGTH_LONG).show();
                    } else {
                        Intent intent = new Intent(GetStartedPage.this, HomePage.class);
                        intent.putExtra("username", inputName);
                        eName.getText().clear();
                        startActivity(intent);
                    }
                    ePassword.getText().clear();
                }
            }
        });

        eRegister.setOnClickListener(v -> {
            Intent intent = new Intent(GetStartedPage.this, RegisterPage.class);
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
                Toast.makeText(GetStartedPage.this, "Verificati Conexiunea", Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
            Toast.makeText(GetStartedPage.this, "Eroare", Toast.LENGTH_LONG).show();
        }
        return false;
    }
}