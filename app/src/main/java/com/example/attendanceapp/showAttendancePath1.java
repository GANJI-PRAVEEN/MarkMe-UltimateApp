package com.example.attendanceapp;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.airbnb.lottie.LottieAnimationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class showAttendancePath1 extends AppCompatActivity {
    private Spinner spinner, spinner2;
    String selectedSubject;
    String studentName;
    DBHelper dbHelper;
    String selectedDate;
    int classIndex, studentIndex;
    ImageButton btnOpenDatePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_show_attendance_path1);
        spinner = findViewById(R.id.classListSpinner);
        Button button = findViewById(R.id.btnStudentSubmit);
        button.setOnClickListener((v) -> onCheckStatus());
        ArrayList<String> listOfClasses = new ArrayList<String>();
        dbHelper = new DBHelper(this);
        List<Integer> classIDS = new ArrayList<>();
        List<Integer> studentIDS = new ArrayList<>();

        spinner2 = findViewById(R.id.nameListSpinner);
        ArrayList<String> listOfNames = new ArrayList<>();

        Cursor cursor = dbHelper.getClassTable();
        Cursor cursor1 = dbHelper.getStudentTable();
        while (cursor.moveToNext()) {
            String classes = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.SUBJECT_NAME_KEY));
            int cid = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.CID));
            classIDS.add(cid);
            listOfClasses.add(classes);
        }

        while (cursor1.moveToNext()) {
            String name = cursor1.getString(cursor1.getColumnIndexOrThrow(DBHelper.STUDENT_NAME_KEY));
            int sid = cursor1.getInt(cursor1.getColumnIndexOrThrow(DBHelper.SID));
            studentIDS.add(sid);
            listOfNames.add(name);
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listOfClasses);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ArrayAdapter<String> arrayAdapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listOfNames);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(arrayAdapter);
        spinner2.setAdapter(arrayAdapter2);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSubject = parent.getItemAtPosition(position).toString();
                classIndex = classIDS.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinner.setPrompt("Please Selected The Class");
            }
        });
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                studentName = parent.getItemAtPosition(position).toString();
                studentIndex = studentIDS.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinner2.setPrompt("Please Selected The name");
            }
        });

    }

    public void onCheckStatus() {
        Boolean todayStatus= dbHelper.getSatusOfStudent(studentIndex);
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.mark_attendance_custom_dialog);
        LottieAnimationView lottieAnimationView = dialog.findViewById(R.id.lottieAnimation);
        TextView successMessage = dialog.findViewById(R.id.successMessage);
        if (todayStatus) {
            successMessage.setText("Your Attendance has Marked");
            lottieAnimationView.setAnimation("successjson.json");
        } else {
            lottieAnimationView.setAnimation("failure.json");
            successMessage.setText("Sorry,Your Attendance has Not Marked");
        }
        lottieAnimationView.playAnimation();
        dialog.show();
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                dialog.dismiss();
            }
        }, 2000);

    }
}