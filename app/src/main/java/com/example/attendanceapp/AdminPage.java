package com.example.attendanceapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class AdminPage extends AppCompatActivity {
    private ImageView studentList,teacherList;
    private Toolbar toolbar;
    private TextView toolbarTitle,toolbarSubTitle;
    private ImageButton backArrow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_page);
        studentList = findViewById(R.id.studentList);
        teacherList = findViewById(R.id.teacherList);
        teacherList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openTeacherInterface();
            }
        });
        studentList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openStudentList();
            }
        });
        toolbar = findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarSubTitle = findViewById(R.id.toolbarSubtitle);
        toolbarTitle.setText("Admin Page");
        toolbarSubTitle.setText("Welcome");
        backArrow = findViewById(R.id.toolbarBackIcon);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ImageView saveIcon = findViewById(R.id.toolbarSaveIcon);
        saveIcon.setVisibility(View.GONE);

    }

    private void openStudentList() {
        Intent intent = new Intent(this,StudentActivity.class);
        intent.putExtra("admin","IamAdmin");
        intent.putExtra("adminPage","toStudent");
        startActivity(intent);
    }
    private void openTeacherInterface(){
        Intent intent = new Intent(this, TeacherPage.class);
        intent.putExtra("teacher","IamTeacher");
        startActivity(intent);
    }
}