package com.example.attendanceapp;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class AttendanceResult extends AppCompatActivity {
    private long SID;
    private TextView attendanceResultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_attendance_result);

        SID = getIntent().getLongExtra("SID",-1);
        attendanceResultTextView = findViewById(R.id.attendanceResult);

        Cursor cursor = new DBHelper(this).getMonthlyAttendance(SID);

        if(cursor != null && cursor.moveToFirst()) {
            StringBuilder result = new StringBuilder();

            do {
                String subject = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.SUBJECT_NAME_KEY));
                String presentCount = cursor.getString(cursor.getColumnIndexOrThrow("PresentCount"));

                result.append(subject)
                        .append(" : ")
                        .append(presentCount)
                        .append("\n");
            } while (cursor.moveToNext());

            attendanceResultTextView.setText(result.toString());
            cursor.close();
        } else {
            Toast.makeText(this, "No attendance records found", Toast.LENGTH_SHORT).show();
        }
    }
}
