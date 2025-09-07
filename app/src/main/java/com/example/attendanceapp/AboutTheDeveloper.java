package com.example.attendanceapp;

import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AboutTheDeveloper extends AppCompatActivity {
    private Button moveBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_about_the_developer);
        moveBack = findViewById(R.id.moveBack);
        moveBack.setOnClickListener((v)->{
            onBackPressed();
        });
    }
}