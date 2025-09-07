package com.example.attendanceapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.attendanceapp.ModalClasses.AttendanceItems;

import java.util.ArrayList;

public class StudentProfile extends AppCompatActivity {
    ProgressBar studentProgressBar;
    TextView displayNameOfStudent,displayEmailOfStudent;
    TextView displayIDOfStudent;
    Button btnMoveBack;
    long SID;
    DBHelper dbHelper;
    private String nameOfStudent,emailOfStudent,IdOfStudent;
    ArrayList<AttendanceItems>resultList;
    int countPresentDays,countTotalClasses;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.content_view);
        dbHelper = new DBHelper(this);
        displayNameOfStudent = findViewById(R.id.displayNameOfStudent);
        displayEmailOfStudent = findViewById(R.id.displayEmailOfStudent);
        displayIDOfStudent = findViewById(R.id.displayIDOfStudent);


        SID = getIntent().getLongExtra("SID",-1);
        getStudentDetails();

        displayNameOfStudent.setText(nameOfStudent);
        displayEmailOfStudent.setText(emailOfStudent);
        IdOfStudent = String.valueOf(dbHelper.getSecretIDOfStudent(SID));
        displayIDOfStudent.setText(IdOfStudent);

        btnMoveBack = findViewById(R.id.moveButton);
        btnMoveBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        getAttendanceFromDB();

        setUpProgressBar();



    }
    public void getStudentDetails(){
        Cursor cursor =  dbHelper.getStudentProfile(SID);
        if(cursor!=null && cursor.moveToFirst()) {
            nameOfStudent = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.STUDENT_NAME_KEY));
            emailOfStudent = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.STUDENT_EMAIL));
        }
        else{
            Toast.makeText(this, "Failed to fetch the Student Profile", Toast.LENGTH_SHORT).show();
        }
    }
    public void getAttendanceFromDB(){
        countPresentDays = 0;
        countTotalClasses = 0;
        Cursor cursor = dbHelper.getMonthlyAttendance(SID);
        if(cursor!=null && cursor.moveToFirst()){
            resultList = new ArrayList<>();
            do{
                String presentDays = cursor.getString(cursor.getColumnIndexOrThrow("PresentCount"));
                String  totalClasses = cursor.getString(cursor.getColumnIndexOrThrow("TotalClasses"));
//                IdOfStudent = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.STUDENT_ROLL_KEY));

                countPresentDays += Integer.parseInt(presentDays);
                countTotalClasses +=Integer.parseInt(totalClasses);
            }
            while(cursor.moveToNext());
        }
    }
    public void setUpProgressBar(){
        studentProgressBar = findViewById(R.id.attendanceProgressBarAtStudentProfile);
        studentProgressBar.setProgress(countPresentDays);
        TextView tvTotalPercentage = findViewById(R.id.tvAttendancePercentage);
        if (countTotalClasses > 0) {
            int percentage = (countPresentDays * 100) / countTotalClasses;
            tvTotalPercentage.setText(percentage + "%");
            studentProgressBar.setProgress(percentage);
        } else {
            tvTotalPercentage.setText("0%");
            studentProgressBar.setProgress(0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}