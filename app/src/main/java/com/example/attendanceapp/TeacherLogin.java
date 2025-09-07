package com.example.attendanceapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class TeacherLogin extends AppCompatActivity {
    private Button btnSubmit;
    private TextInputEditText edtTeacherPassword;
     String secretPassword;
     private DBHelper dbHelper;
     private String nameOfTeacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_teacher_login);
        edtTeacherPassword = findViewById(R.id.edtTeacherSecretPassword);
        btnSubmit = findViewById(R.id.btnTeacherSubmit);
        dbHelper = new DBHelper(this);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                secretPassword = edtTeacherPassword.getText().toString().trim();
                if(checkCredentail(secretPassword)){
                    Cursor cursor = dbHelper.checkTeacherExistInDB(secretPassword);
                    if(cursor!=null && cursor.moveToFirst()){
                        nameOfTeacher  = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.TEACHER_NAME));
                    }
                    else{
                        Toast.makeText(TeacherLogin.this, "No Teacher Found", Toast.LENGTH_SHORT).show();
                    }
                    if(cursor.getCount()>0){
                        Intent intent = new Intent(TeacherLogin.this,MainActivity.class);
                        intent.putExtra("nameOfTeacher",nameOfTeacher);
                        startActivity(intent);
                    }
                }
            }
        });

    }
    private boolean checkCredentail(String secretPassword){
        if(secretPassword ==null || secretPassword.equals(" "))return false;
        return true;
    }
}