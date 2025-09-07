package com.example.attendanceapp;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.os.Bundle;
import android.os.Message;
import android.se.omapi.Session;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendanceapp.Adapters.StudentAdapter;
import com.example.attendanceapp.ModalClasses.StudentItems;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.ArrayList;
import java.util.Properties;

import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class StudentActivity extends AppCompatActivity {
    Toolbar toolbar;
    String className,subjectName;
    private FloatingActionButton fab;
    private String role;
    private boolean isAdmin=false;
    RecyclerView studentRecyclerView;
    private Boolean saved=false;
    long newClassID;
    ArrayList<StudentItems>studentItemsArrayList;
    DBHelper dbHelper;
    private Boolean isStudentFromAdmin=false;
    long CID;
    String role2="";
    private TextView subTitle;
    int position;
    StudentAdapter studentAdapter;
    MyCalendar calendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student);
        toolbar = findViewById(R.id.toolbar);
        calendar=new MyCalendar();

        dbHelper = new DBHelper(this);
        studentItemsArrayList = new ArrayList<>();
        className = "Title";
        subjectName = "subTitle";
        role2 = getIntent().getStringExtra("admin");
        fab = findViewById(R.id.fab_add_student);
//        Toast.makeText(this, "Role : "+role, Toast.LENGTH_SHORT).show();
        role=getIntent().getStringExtra("role");
//        Toast.makeText(this, "Role "+role, Toast.LENGTH_SHORT).show();
        String msgFromIntentsForRole = getIntent().getStringExtra("adminPage");
        if(msgFromIntentsForRole!=null){
            isStudentFromAdmin = true;
        }
        if("student".equals(role)){
//            Toast.makeText(this, "isStudent "+isStudentFromAdmin, Toast.LENGTH_SHORT).show();
//            Toast.makeText(this, "Yes..!", Toast.LENGTH_SHORT).show();
            fab.setVisibility(View.INVISIBLE);
        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddStudentDialog();
            }
        });
        loadData();
        if("IamAdmin".equals(role2)){
            className = "Student's Log";
            subjectName = "Add Student";
            isAdmin=true;
        }
        else {
            className = getIntent().getStringExtra("Title");
            subjectName = getIntent().getStringExtra("subTitle");
            isAdmin=false;
        }
        CID = getIntent().getLongExtra("ClassID",-1);
//        Toast.makeText(this, "NewClassID "+CID, Toast.LENGTH_SHORT).show();
        position = getIntent().getIntExtra("position",-1);
        studentRecyclerView = findViewById(R.id.student_recyclerview);
        studentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        studentAdapter = new StudentAdapter(this,studentItemsArrayList);
        if(!isAdmin)loadStatusData();
        studentRecyclerView.setAdapter(studentAdapter);
        if(!isAdmin) {
            studentAdapter.setItemListener(new StudentAdapter.onItemClickListener() {
                @Override
                public void onClick(int position) {
//                    Toast.makeText(StudentActivity.this, "here it is", Toast.LENGTH_SHORT).show();
                    changeStatus(position, CID);
                }
            });
        }
        setTollbar();
        subTitle.setText(subjectName+" - "+calendar.getDate());

    }

    private void loadData() {
//        Toast.makeText(this, "loading the data here", Toast.LENGTH_SHORT).show();
        Cursor cursor = dbHelper.getStudentTable();
        studentItemsArrayList.clear();
        while(cursor.moveToNext()){
            long sid = cursor.getLong(cursor.getColumnIndexOrThrow(DBHelper.SID));
            String studentName = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.STUDENT_NAME_KEY));
            int studenRoll = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.STUDENT_ROLL_KEY));
            String email =  cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.STUDENT_EMAIL));
            studentItemsArrayList.add(new StudentItems(sid,studenRoll,studentName,email,newClassID));
        }
        cursor.close();
    }

    private void changeStatus(int position,long newClassID){
        String status = studentItemsArrayList.get(position).getStatus();
        if(status.equals("P")){
            status="A";
        }
        else status="P";
        studentItemsArrayList.get(position).setStatus(status,newClassID);
        studentAdapter.notifyItemChanged(position);
    }
    private void setTollbar(){
        TextView title = toolbar.findViewById(R.id.toolbarTitle);
        subTitle = toolbar.findViewById(R.id.toolbarSubtitle);
        ImageButton backButton = toolbar.findViewById(R.id.toolbarBackIcon);
        String whoIs = getIntent().getStringExtra("admin");
        ImageButton saveButton = toolbar.findViewById(R.id.toolbarSaveIcon);
        if("IamAdmin".equals(whoIs)){
            saveButton.setVisibility(View.INVISIBLE);
        }

        saveButton.setOnClickListener((v)->{
            saved = true;
            Toast.makeText(this, "Data Saved", Toast.LENGTH_SHORT).show();
            saveStatus();
        });
        title.setText(className);
        subTitle.setText(subjectName);
        if(!isAdmin)toolbar.inflateMenu(R.menu.student_menu);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item){
                if(item.getItemId()==R.id.show_attendance_sheet){
                    openSheetList();
                }

                return true;
            }
        });
        backButton.setOnClickListener(v->onBackPressed());
    }

    private void openSheetList() {
        long[] idArray = new long[studentItemsArrayList.size()];
        for(int i=0;i<idArray.length;i++){

            idArray[i] = studentItemsArrayList.get(i).getSid();
            Log.e("IDARRAY "," "+idArray[i]);
        }
        String [] nameArray = new String[studentItemsArrayList.size()];
        for(int i=0;i<idArray.length;i++){

            nameArray[i] = studentItemsArrayList.get(i).getName();
            Log.e("NameArray "," "+nameArray[i]);
        }
        int[] rollArray = new int[studentItemsArrayList.size()];
        for(int i=0;i<idArray.length;i++){
            rollArray[i] = studentItemsArrayList.get(i).getRollNo();
        }
        Log.e("CID "," "+CID);
        Intent intent = new Intent(this,SheetListActivity2.class);
        intent.putExtra("cid",CID);
        intent.putExtra("idArray",idArray);
        intent.putExtra("rollArray",rollArray);
        intent.putExtra("nameArray",nameArray);
        intent.putExtra("subjectName",subjectName);
        startActivity(intent);
    }

    public void saveStatus(){

        for(StudentItems studentItems:studentItemsArrayList){
            String status = studentItems.getStatus();

            long value = dbHelper.addStatus(studentItems.getSid(),status,CID,calendar.getDate());
            if(value==0){
//                Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
//                Toast.makeText(this, "You are not allowed to take attendance", Toast.LENGTH_SHORT).show();
            }
//            Toast.makeText(this,""+value, Toast.LENGTH_SHORT).show();
            if(value==-1){
                Log.e("Updating in StudentActivity","");
//                Toast.makeText(this, "Updating....", Toast.LENGTH_SHORT).show();
                long val = dbHelper.updateStatus(studentItems.getSid(),calendar.getDate(),status);
//                Toast.makeText(this, "SID "+val, Toast.LENGTH_SHORT).show();
//                Toast.makeText(this, ""+val, Toast.LENGTH_SHORT).show();
            }
//            dbHelper.updateStatus(studentItems.getSid(),calendar.getDate(),status);
        }
        studentAdapter.notifyDataSetChanged();
    }
    private void loadStatusData(){
        //Toast.makeText(this, "You are in the statusData", Toast.LENGTH_SHORT).show();
        for(StudentItems studentItems : studentItemsArrayList){
            Log.e("Loading","StatusID "+studentItems.getSid()+"ClassID "+CID);
//            Toast.makeText(this, "Got it Man", Toast.LENGTH_SHORT).show();
            String status = dbHelper.getStatus(studentItems.getSid(),calendar.getDate(),CID);
            if(status!=null) {
                studentItems.setStatus(status,newClassID);
            }
            else{
                Log.e("NoStatus",status);
            }
        }
        studentAdapter.notifyDataSetChanged();
    }

    private void showCalendar() {
        MyCalendar myCalendar = new MyCalendar();
        myCalendar.show(getSupportFragmentManager(),"");
        myCalendar.setOnCalendarOkClickListener(new MyCalendar.onCalendarOkClickListener() {
            @Override
            public void onClick(int year, int month, int day) {
                calendar.setDate(year,month,day);
                subTitle.setText(subjectName+" - "+calendar.getDate());
            }

        });
    }

    private void showAddStudentDialog() {
        MyDialog myDialog= new MyDialog();
        myDialog.show(getSupportFragmentManager(),MyDialog.STUDENT_ADD_DIALOG);
        myDialog.setListener(new MyDialog.onClickListener() {
            @Override
            public void onClick(String text1, String text2,String text3) {
//                if(checkforDuplicancy(text1))
                    AddStudent(text1,text2,text3,newClassID);
//                else{
//                    Toast.makeText(StudentActivity.this, "Same Roll already exist...!", Toast.LENGTH_SHORT).show();
//                }
            }
        });
    }
    private boolean checkforDuplicancy(String text1){
        int enteredRoll = Integer.parseInt(String.valueOf(text1));
        Cursor cursor = dbHelper.getStudentTable();
        while(cursor.moveToNext()){
            int sid = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.SID));
            if(sid==enteredRoll){
                return false;
            }
        }
        return true;
    }
    private void AddStudent(String Roll,String Name,String email,long newClassID){
//        Toast.makeText(this, "adding student "+newClassID, Toast.LENGTH_SHORT).show();
        long sid = dbHelper.addStudent(newClassID,Integer.parseInt(Roll),Name,email);
        if(sid == -1){
            Toast.makeText(this, "Email Already Exists.", Toast.LENGTH_SHORT).show();
            return;
        }
        sendEmailToStudent(Roll,Name,email);
//        Toast.makeText(this, "CID here "+newClassID, Toast.LENGTH_SHORT).show();
        StudentItems studentItems = new StudentItems(sid,Integer.parseInt(Roll),Name,email,newClassID);
        studentItemsArrayList.add(studentItems);
        studentAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case 1:
                deleteStudent(item.getGroupId());
                break;
        }
        return true;
    }
//    private void showUpdateStudentDialog(int position){
//        MyDialog myDialog = new MyDialog(studentItemsArrayList.get(position).getRollNo(),studentItemsArrayList.get(position).getName(),studentItemsArrayList.get(position).getEmail());
//
//        myDialog.show(getSupportFragmentManager(),MyDialog.STUDENT_UPDATE_DIALOG);
//        myDialog.setListener(new MyDialog.onClickListener() {
//            @Override
//            public void onClick(String text1, String text2,String optional) {
//                updateStudent(position,text1,text2);
//            }
//        });
//    }
//    private void updateStudent(int position,String RollNo,String Name){
//        dbHelper.updateStudent(studentItemsArrayList.get(position).getSid(),Name,studentItemsArrayList.get(position).getNewClassID());
//        studentItemsArrayList.get(position).setName(Name);
//        studentAdapter.notifyItemChanged(position);
//    }
    private void deleteStudent(int position){
            if(isAdmin) {
                dbHelper.deleteStudent(studentItemsArrayList.get(position).getSid());
                studentItemsArrayList.remove(position);
                studentAdapter.notifyItemRemoved(position);
            }
            else{
                Toast.makeText(this, "You're not allowed to delete...!", Toast.LENGTH_SHORT).show();
            }

    }

    @Override
    public void onBackPressed() {
        if(!saved && !isStudentFromAdmin){
            openDiscardDialog();
        }
        else{
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "On Resume()", Toast.LENGTH_SHORT).show();
        if(!isAdmin)loadStatusData();
    }

    private void openDiscardDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("You have unsaved changes. Are you sure you want to leave?").setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StudentActivity.super.onBackPressed();
                dialog.dismiss();
            }
        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void sendEmailToStudent(String roll,String name,String to) {

        try {

            String message = "<html>" +
                    "<body style='font-family: Arial, sans-serif; line-height:1.6;'>" +
                    "<h2 style='color:#2E86C1;'>Hello " + name + ",</h2>" +
                    "<p>You have successfully been added to the <b>Attendance App</b>.</p>" +
                    "<p>Now you can use the app to check your <b>attendance progress</b> for every month!</p>" +
                    "<br>" +
                    "<p>Thank you,</p>" +
                    "<p style='font-weight:bold; color:#555;'>Best regards,<br>GANJI PRAVEEN</p>" +
                    "</body>" +
                    "</html>";

            String Subject = "Atttendance App Admin";

            if (to.isEmpty()) {
                Toast.makeText(StudentActivity.this, "You must enter a recipient email", Toast.LENGTH_LONG).show();
            } else if (name.isEmpty()) {
                Toast.makeText(StudentActivity.this, "You must enter a Subject", Toast.LENGTH_LONG).show();
            } else if (message.isEmpty()) {
                Toast.makeText(StudentActivity.this, "You must enter a message", Toast.LENGTH_LONG).show();
            } else {
                to.trim();
                //everything is filled out
                //send email
                new SimpleMail().sendEmail(to, "", Subject, name, message, new SimpleMail.MailcallBack() {
                    @Override
                    public void onSuccess() {
                        runOnUiThread(() ->
                                Toast.makeText(getApplicationContext(), "Mail Sent Succesfully to student", Toast.LENGTH_SHORT).show()
                        );
                    }

                    @Override
                    public void onFailure(Exception e) {
                        runOnUiThread(()->
                            Toast.makeText(getApplicationContext(), "Sorry unable to send Mail, Please delete & try again..!", Toast.LENGTH_SHORT).show()
                        );
                    }
                });
//                Toast.makeText(this, "Email Sent Successfully", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            e.printStackTrace();

        }
    }



};