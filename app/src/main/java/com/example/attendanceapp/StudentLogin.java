package com.example.attendanceapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class StudentLogin extends AppCompatActivity {
    private Spinner spinner;
    private TextInputEditText edtStudentPassword;
    private Button btnStudentButton;
    static final String STUDENT_PASSWORD="student";
    private String stdEmail,stdPassword;
    private TextInputEditText edtStudentEmail;
    DBHelper dbHelper;
    private String nameOfStudent;
    private long sid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(0, 0);
        EdgeToEdge.enable(this);

        setContentView(R.layout.activity_student_login);
        btnStudentButton = findViewById(R.id.btnStudentSubmit);
        edtStudentEmail = findViewById(R.id.edtStudentEmail);
        dbHelper = new DBHelper(this);
        edtStudentPassword = findViewById(R.id.edtStudentPassword);
        btnStudentButton.setOnClickListener((v)->{
            stdPassword = edtStudentPassword.getText().toString().trim();
            stdEmail = edtStudentEmail.getText().toString().trim();
            if(checkCredentials(stdEmail,stdPassword)) {
                Cursor cursor = dbHelper.checkStudentExistsInDB(stdEmail,stdPassword);
                if(cursor!=null && cursor.moveToFirst()){
                    nameOfStudent  = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.STUDENT_NAME_KEY));
                    sid = cursor.getLong(cursor.getColumnIndexOrThrow(DBHelper.SID));
                    if (cursor.getCount()>0) {
                        Toast.makeText(this, "Success Logged in..", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(this,StudentPage.class);
                        intent.putExtra("nameOfStudent",nameOfStudent);
                        intent.putExtra("SID",sid);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "Wrong Password", Toast.LENGTH_SHORT).show();
                        edtStudentPassword.setError("Wrong password");
                    }
                }
                else{
                    Toast.makeText(this, "Wrong password/email..!", Toast.LENGTH_SHORT).show();
                }

            }
            else{
                Toast.makeText(this, "Enter correct Data", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private boolean checkCredentials(String stdEmail,String stdPassword){
        if(stdEmail==null || stdPassword==null || stdEmail.equals(" ") ||  stdPassword.equals(" ")){
            return false;
        }
        else return true;
    }
}