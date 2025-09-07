package com.example.attendanceapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class StudentPage extends AppCompatActivity {
    private ImageView showAttendance;
    private NavigationView sideNavigationView;
    private DrawerLayout drawerLayout;
    DBHelper dbHelper;
    private Toolbar toolbar;
    private ImageButton icon_back, toolbarSaveIcon;
    private Long SID;
    private String nameOfStudent;
    private TextView nameofStudentTV, greetingText;
    private TextView toolbarTitle, toolbarSubTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_page);
        showAttendance = findViewById(R.id.showAttendance);
        showAttendance.setOnClickListener((v) -> {
            Intent intent = new Intent(this, AttendanceReportActivity.class);
            intent.putExtra("SID", SID);
            startActivity(intent);
        });

        toolbar = findViewById(R.id.toolbar);
        toolbarSaveIcon = findViewById(R.id.toolbarSaveIcon);
        toolbarSaveIcon.setVisibility(View.GONE);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        toolbarSubTitle = findViewById(R.id.toolbarSubtitle);
        toolbarTitle.setText("Student DashBoard");
        toolbarSubTitle.setText("Welcome");
        ImageButton backButton = findViewById(R.id.toolbarBackIcon);
        backButton.setOnClickListener((v) -> {
            onBackPressed();
        });
//        nameOfStudent = findViewById(R.id.)
        drawerLayout = findViewById(R.id.studentDrawerLayout);
        sideNavigationView = findViewById(R.id.sideNavigationDrawerOfStudent);

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        sideNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.myHome) {
                    startActivity(new Intent(StudentPage.this, EntryPage.class));
                }
                if (menuItem.getItemId() == R.id.myAccount) {
                    Toast.makeText(StudentPage.this, "Will Come Soon..!", Toast.LENGTH_SHORT).show();
                }
                if (menuItem.getItemId() == R.id.FeedBack) {
                    startActivity(new Intent(StudentPage.this, FeedbackActivity.class));
                }
                if (menuItem.getItemId() == R.id.myLogout) {
                    creatAlertDialog();
                }
                if (menuItem.getItemId() == R.id.myAccount) {
                    Intent intent = new Intent(StudentPage.this, StudentProfile.class);
                    intent.putExtra("SID", SID);
                    startActivity(intent);
                }
                return true;
            }
        });


        icon_back = findViewById(R.id.toolbarBackIcon);
        icon_back.setVisibility(View.GONE);
        View navHeaderView = sideNavigationView.getHeaderView(0);
        nameofStudentTV = navHeaderView.findViewById(R.id.txtHeaderName);
        nameOfStudent = getIntent().getStringExtra("nameOfStudent");
        SID = getIntent().getLongExtra("SID", -1);
        greetingText = findViewById(R.id.greetingText);
        greetingText.setText("Hi " + nameOfStudent + " ..!");
        nameofStudentTV.setText(nameOfStudent);
        dbHelper = new DBHelper(this);


//        View headerView = sideNavigationView.getHeaderView(0);
//
//// Get status bar height
//        int statusBarHeight = getResources().getDimensionPixelSize(
//                getResources().getIdentifier("status_bar_height", "dimen", "android")
//        );
//
//// Add padding so header content starts below system UI
//        headerView.setPadding(
//                headerView.getPaddingLeft(),
//                statusBarHeight + headerView.getPaddingTop(),
//                headerView.getPaddingRight(),
//                headerView.getPaddingBottom()
//        );
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    public void creatAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setMessage("Are you sure want to Logout?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(StudentPage.this, EntryPage.class));
                        finish();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }
};