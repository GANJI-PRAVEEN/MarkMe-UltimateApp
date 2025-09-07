package com.example.attendanceapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class EntryPage extends AppCompatActivity {
    private ImageView teacherActive,studentActive,adminActive;
    private TextView tvAboutTheDeveloper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_entry_page);
        teacherActive = findViewById(R.id.teacherActive);
        studentActive = findViewById(R.id.studentActive);
        adminActive = findViewById(R.id.adminActive);
        tvAboutTheDeveloper  = findViewById(R.id.tvAboutDeveloper);
        tvAboutTheDeveloper.setOnClickListener((v)->{
            startActivity(new Intent(this, AboutTheDeveloper.class));
        });
        studentActive.setOnClickListener((v)->{
            startActivity(new Intent(this,StudentLogin.class));
        });
        adminActive.setOnClickListener((v)->{
            startActivity(new Intent(this,AdminLogin.class));
        });
        teacherActive.setOnClickListener((v)->{
            startActivity(new Intent(this,TeacherLogin.class));
        });

    }
}