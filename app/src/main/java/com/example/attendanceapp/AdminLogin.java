package com.example.attendanceapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AdminLogin extends AppCompatActivity {
    private Button btnSubmit;
    private TextInputEditText edtAdminPassword;
    private TextInputLayout textInputLayout;
    final static  String ADMIN_PASSWORD = "1234";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_login);
        textInputLayout = findViewById(R.id.textInputLayout4);
        edtAdminPassword = findViewById(R.id.edtAdminPassword);
        btnSubmit = findViewById(R.id.btnAdminSubmit);
        btnSubmit.setOnClickListener((v)->{
            String pass = edtAdminPassword.getText().toString();
            if(pass.equals(ADMIN_PASSWORD)){
                startActivity(new Intent(this, AdminPage.class));
            }
            else{
                edtAdminPassword.setError("Wrong Password");
                Toast.makeText(this, "Wrong Password", Toast.LENGTH_SHORT).show();
            }
        });

    }
}