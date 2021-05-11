package com.example.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class GetStartedPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started_page);

        Button eRegister = findViewById(R.id.btnCreate);
        Button eLogin = findViewById(R.id.btnLog);

        eRegister.setOnClickListener(v -> {
            Intent intent = new Intent(GetStartedPage.this, RegisterPage.class);
            startActivity(intent);
        });

        eLogin.setOnClickListener(v -> {
            Intent intent = new Intent(GetStartedPage.this, LoginPage.class);
            startActivity(intent);
        });
    }
}