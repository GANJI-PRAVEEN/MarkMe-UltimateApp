package com.example.attendanceapp;

import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendanceapp.Adapters.AttendanceAdapter;
import com.example.attendanceapp.ModalClasses.AttendanceItems;

import java.util.ArrayList;
import java.util.List;


public class AttendanceReportActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private long SID;
    DBHelper dbHelper;
    private List<AttendanceItems>resultList;
    private TextView tvTotalPercentage;
    private ProgressBar progressBar;
    AttendanceItems attendanceItems ;
    private ImageButton saveImageIcon,backPressImage;
    private Toolbar toolbar;
    private TextView title,subTitle;
    String secretIdOfStudent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_attendance_report);
        recyclerView = findViewById(R.id.recyclerAttendanceReport);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        progressBar = findViewById(R.id.progressBar);
        tvTotalPercentage = findViewById(R.id.tvTotalPercentage);
        SID = getIntent().getLongExtra("SID",-1);

        //toolbar content
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        saveImageIcon = findViewById(R.id.toolbarSaveIcon);
        title = findViewById(R.id.toolbarTitle);
        subTitle = findViewById(R.id.toolbarSubtitle);
        saveImageIcon.setVisibility(View.GONE);
        title.setText("Track Attendance");
        if(SID>=10){
            secretIdOfStudent = "20250"+SID;
        }
        else{
            secretIdOfStudent ="202500"+SID;
        }
        subTitle.setText("ID: "+secretIdOfStudent);
        backPressImage = findViewById(R.id.toolbarBackIcon);
        backPressImage.setOnClickListener((v)->onBackPressed());





        dbHelper = new DBHelper(this);
        Cursor cursor = dbHelper.getMonthlyAttendance(SID);

        if(cursor!=null && cursor.moveToFirst()){
            resultList  = new ArrayList<>();
            do {
                String subject = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.SUBJECT_NAME_KEY));
                String presentCount = cursor.getString(cursor.getColumnIndexOrThrow("PresentCount"));
                resultList.add(new AttendanceItems(subject,Integer.parseInt(presentCount),30));
            } while (cursor.moveToNext());
        }
        AttendanceAdapter attendanceAdapter = new AttendanceAdapter(resultList);
        recyclerView.setAdapter(attendanceAdapter);

        calculateOverallPercentage();

    }
    private void calculateOverallPercentage() {
        int totalPresent = 0, totalClasses = 0;

        for (AttendanceItems item : resultList) {
            totalPresent += item.getPresent();
            totalClasses += item.getTotal();
        }

        if (totalClasses > 0) {
            int percentage = (totalPresent * 100) / totalClasses;
            tvTotalPercentage.setText(percentage + "%");
            progressBar.setProgress(percentage);
        } else {
            tvTotalPercentage.setText("0%");
            progressBar.setProgress(0);
        }
    }
}