package com.example.attendanceapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.example.attendanceapp.Adapters.TeacherAdapter;
import com.example.attendanceapp.ModalClasses.StudentItems;
import com.example.attendanceapp.ModalClasses.TeacherItems;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendanceapp.databinding.ActivityTeacherPageBinding;

import java.util.ArrayList;

public class TeacherPage extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private Toolbar toolbar;
    private ArrayList<TeacherItems> teacherItemsArrayList;

    private RecyclerView recyclerView;
    private TeacherAdapter teacherAdapter;
    private TextView toolbarTitle,toolbarSubTitle;
    private DBHelper dbHelper;

    private FloatingActionButton fab;
    private String msgFromIntentForTitleofToolbar;
    private ImageButton saveIcon,backArrow;
    private RecyclerView teacherrecyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_teacher_page);
        toolbar =findViewById(R.id.toolbar);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        saveIcon = findViewById(R.id.toolbarSaveIcon);
        saveIcon.setVisibility(View.GONE);
        toolbarSubTitle = findViewById(R.id.toolbarSubtitle);
        setSupportActionBar(toolbar);
        backArrow = findViewById(R.id.toolbarBackIcon);
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        teacherItemsArrayList = new ArrayList<>();
        recyclerView= findViewById(R.id.teacher_recyclerview);
        fab = findViewById(R.id.fab_add_teacher);
        dbHelper =  new DBHelper(this);

        msgFromIntentForTitleofToolbar = getIntent().getStringExtra("teacher");
//        Toast.makeText(this, "msgfromIntent()"+msgFromIntentForTitleofToolbar, Toast.LENGTH_SHORT).show();
        if(msgFromIntentForTitleofToolbar!=null){
//            Toast.makeText(this, "Came", Toast.LENGTH_SHORT).show();
            toolbarTitle.setText("Teacher's Log");
            toolbarSubTitle.setText("Add Teacher");
        }


        loadTeacherDataFromTable();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAddTeacherDialog();
            }
        });
        teacherrecyclerView =findViewById(R.id.teacher_recyclerview);
        teacherrecyclerView.setLayoutManager(new LinearLayoutManager(this));
        teacherAdapter = new TeacherAdapter(this,teacherItemsArrayList);
        teacherrecyclerView.setAdapter(teacherAdapter);

    }

    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case 0:
                deleteTeacherFromDB(item.getGroupId());
                break;
        }
        return true;
    }
    private void deleteTeacherFromDB(int position){
        long rowid = dbHelper.deleteTeacher(teacherItemsArrayList.get(position).getTeacherID());
        Log.e("ROWID",""+rowid);
        teacherItemsArrayList.remove(position);
        teacherAdapter.notifyItemRemoved(position);
    }

    private void showAddStudentDialog() {
        MyDialog myDialog= new MyDialog();
        myDialog.show(getSupportFragmentManager(),MyDialog.STUDENT_ADD_DIALOG);
        myDialog.setListener(new MyDialog.onClickListener() {
            @Override
            public void onClick(String text1, String text2,String text3) {
                if(checkforDuplicancy(text1))
                    AddTeacher(text2,text3);
                else{
                    Toast.makeText(TeacherPage.this, "Same Teacher ID already exist...!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void openAddTeacherDialog() {
        MyDialog myDialog= new MyDialog();
        myDialog.show(getSupportFragmentManager(),MyDialog.TEACHER_ADD_DIALOG);
        myDialog.setListener(new MyDialog.onClickListener() {
            @Override
            public void onClick(String text1, String text2,String optional) {
                if(checkforDuplicancy(text1))
                    AddTeacher(text2,optional);
                else{
                    Toast.makeText(TeacherPage.this, "Same Roll already exist...!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private boolean checkforDuplicancy(String text1){
        int enteredRoll = Integer.parseInt(String.valueOf(text1));
        Cursor cursor = dbHelper.getTeacherTable();
        while(cursor.moveToNext()){
            int roll = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.TEACHER_ID));

            if(roll==enteredRoll){
                return false;
            }
        }
        return true;
    }

    private void AddTeacher(String teacher_Name,String optional){
        long teacherID = dbHelper.addTeacher(teacher_Name);
        Toast.makeText(this, "adding student "+teacherID, Toast.LENGTH_SHORT).show();
//        Toast.makeText(this, "teacherID here "+teacherID, Toast.LENGTH_SHORT).show();
        TeacherItems teacherItems = new TeacherItems(teacherID,teacher_Name);
        teacherItemsArrayList.add(teacherItems);
        teacherAdapter.notifyDataSetChanged();
    }
    public void loadTeacherDataFromTable(){
        Cursor cursor = dbHelper.getTeacherTable();
        teacherItemsArrayList.clear();
        while(cursor.moveToNext()){
            String teachername = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.TEACHER_NAME));
            int teacherID = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.TEACHER_ID));
            teacherItemsArrayList.add(new TeacherItems(teacherID,teachername));
        }
        cursor.close();
    }
};
