package com.example.attendanceapp;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendanceapp.Adapters.ClassAdapter;
import com.example.attendanceapp.ModalClasses.ClassItems;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.DrawerLayoutUtils;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab;
    RecyclerView recyclerView;
    DBHelper dbHelper;
    ArrayList<ClassItems>runningClassesList;
    Switch darkModeSwitch;
    private String nameOfStudent;
    ClassAdapter classAdapter;
    Toolbar toolbar;
    EditText edtClassName,edtSubjectName;
    private AlertDialog.Builder builder;
    private ActionBarDrawerToggle toggle;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private String nameofTeacher;
    private TextView greetingTextOfTeacher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            WindowManager.LayoutParams params = getWindow().getAttributes();
            params.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            getWindow().setAttributes(params);
        }
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        runningClassesList = new ArrayList<>();
//        navigationView = findViewById(R.id.navigationView);
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(v->showDialog());
        toolbar = findViewById(R.id.toolbar);
        dbHelper = new DBHelper(this);
        greetingTextOfTeacher = findViewById(R.id.greetingTextOfTeacher);
//        drawerLayout = findViewById(R.id.drawer_layout);

//        toggle = new ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close);
//        drawerLayout.addDrawerListener(toggle);
//        toggle.syncState();
//        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//                if(menuItem.getItemId()==R.id.myAccount){
//
//                }if(menuItem.getItemId()==R.id.nav_profile){
//
//                }if(menuItem.getItemId()==R.id.nav_settings){
//
//                }if(menuItem.getItemId()==R.id.nav_logout){
//
//                }
//                return true;
//            }
//        });
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager= new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        classAdapter=new ClassAdapter(this,runningClassesList);
        setTollbar();
        classAdapter.setItemListener(new ClassAdapter.onItemClickListener() {
            @Override
            public void onClick(int position) {
                goToStudentActivity(position);
            }
        });
        recyclerView.setAdapter(classAdapter);
        nameOfStudent = getIntent().getStringExtra("nameOfStudent");
        nameofTeacher = getIntent().getStringExtra("nameOfTeacher");
        greetingTextOfTeacher.setText("Hi "  +nameofTeacher);
        loadData();


    }

    private void goToStudentActivity(int position) {
//        Toast.makeText(this, "Actual ClassID "+runningClassesList.get(position).getCid(), Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, StudentActivity.class);
        intent.putExtra("Title",runningClassesList.get(position).getClassName());
        intent.putExtra("subTitle",runningClassesList.get(position).getSubjectName());
        intent.putExtra("position",position);
        intent.putExtra("role","student");
        intent.putExtra("ClassID",runningClassesList.get(position).getCid());
        startActivity(intent);
    }

    private void setTollbar(){
        TextView title = toolbar.findViewById(R.id.toolbarTitle);
        TextView subTitle = toolbar.findViewById(R.id.toolbarSubtitle);
        ImageButton backButton = toolbar.findViewById(R.id.toolbarBackIcon);
        ImageButton saveButton = toolbar.findViewById(R.id.toolbarSaveIcon);
        title.setText("Attendance App");
        title.setTextSize(20);
        subTitle.setVisibility(View.GONE);
        backButton.setVisibility(View.GONE);
        saveButton.setVisibility(View.GONE);
    }

    private void loadData(){
//        Toast.makeText(this, "Loading Class Table", Toast.LENGTH_SHORT).show();
        Cursor cursor = dbHelper.getClassTable();
        runningClassesList.clear();
        while(cursor.moveToNext()){
            int cid =  cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.CID));
//            Toast.makeText(this, "CID from Class Table"+cid, Toast.LENGTH_SHORT).show();
            String className = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.CLASS_NAME_KEY));
            String subjectName = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.SUBJECT_NAME_KEY));

            runningClassesList.add(new ClassItems(className,subjectName,cid));
        }

    }
    private void updateBackgroundColor() {
        View rootView = findViewById(android.R.id.content);
        int color;
        if (darkModeSwitch.isChecked()) {
            Toast.makeText(this, "DarkMode On", Toast.LENGTH_SHORT).show();
            color =R.color.lightDark;
            darkModeSwitch.setBackgroundColor(ContextCompat.getColor(this,R.color.white));
        } else {
            Toast.makeText(this, "White Mode On", Toast.LENGTH_SHORT).show();
            color = R.color.white;
        }
        rootView.setBackgroundColor(ContextCompat.getColor(this, color));
    }

    private void showDialog(){
        MyDialog myDialog = new MyDialog();
        myDialog.show(getSupportFragmentManager(),MyDialog.CLASS_ADD_DIALOG);
        myDialog.setListener(new MyDialog.onClickListener() {
            @Override
            public void onClick(String text1, String text2,String optional) {
                AddClass(text1,text2,optional);
            }
        });
    }
    private void AddClass(String className,String subjectName,String optional){
        long cid = dbHelper.addClass(className,subjectName);
        runningClassesList.add(new ClassItems(className,subjectName,cid));
        classAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case 0:
                showUpdateDialog(item.getGroupId());//Item Position
                break;
            case 1:
                deleteClass(item.getGroupId());
        }
        return true;
    }

    private void showUpdateDialog(int position) {
        MyDialog myDialog= new MyDialog(runningClassesList.get(position).getClassName(),runningClassesList.get(position).getSubjectName());
        myDialog.show(getSupportFragmentManager(),MyDialog.CLASS_UPATE_DIALOG);
        myDialog.setListener(new MyDialog.onClickListener() {
            @Override
            public void onClick(String text1, String text2,String text3) {
                updateClass(position,text1,text2);
            }
        });
    }

    private void deleteClass(int position){
        dbHelper.deleteClass(runningClassesList.get(position).getCid());
        runningClassesList.remove(position);
        classAdapter.notifyItemRemoved(position);
    }
    private void updateClass(int position,String className,String subjectName){
        dbHelper.updateClass(runningClassesList.get(position).getCid(),className,subjectName);
        runningClassesList.get(position).setClassName(className);
        runningClassesList.get(position).setSubjectName(subjectName);
        classAdapter.notifyItemChanged(position);
    }
}