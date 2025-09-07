package com.example.attendanceapp;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Date;

public class SheetListActivity2 extends AppCompatActivity {
    private ListView sheetList;
    private Toolbar toolbar;
    private ArrayAdapter arrayAdapter;
    private ArrayList<String> listItems = new ArrayList();
    private long cid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sheet_list2);
        sheetList = findViewById(R.id.sheetList);
        toolbar = findViewById(R.id.toolbar);
        TextView toolBarTitle = findViewById(R.id.toolbarTitle);
        toolBarTitle.setText("Attendance Sheet List");
        ImageButton saveImageButton = findViewById(R.id.toolbarSaveIcon);
        saveImageButton.setVisibility(View.GONE);
        TextView subTitle  = findViewById(R.id.toolbarSubtitle);
        subTitle.setVisibility(View.GONE);
        cid = getIntent().getLongExtra("cid",-1);
//        Toast.makeText(this, "at SheetActivity "+cid, Toast.LENGTH_SHORT).show();
        loadListItems();
//        Toast.makeText(this, "Size"+listItems.size(), Toast.LENGTH_SHORT).show();
        try {
            arrayAdapter = new ArrayAdapter(this, R.layout.sheet_list, R.id.textViewDateList, listItems);
            sheetList.setAdapter(arrayAdapter);
//            Toast.makeText(this, "Exception Raledhu", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            Toast.makeText(this, "Exception occured "+e.toString(), Toast.LENGTH_SHORT).show();
        }
        sheetList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                openSheetDisplayActivity(position);
            }
        });
    }

    private void openSheetDisplayActivity(int position) {
        Toast.makeText(this, "Displaying sheet", Toast.LENGTH_SHORT).show();
        int[] rollArray=getIntent().getIntArrayExtra("rollArray");
        long []idArray = getIntent().getLongArrayExtra("idArray");
        String[] nameArray = getIntent().getStringArrayExtra("nameArray");
        String subjectName  = getIntent().getStringExtra("subjectName");
        Intent intent = new Intent(this,SheetActivity.class);
        intent.putExtra("idArray",idArray);
        intent.putExtra("rollArray",rollArray);
        intent.putExtra("nameArray",nameArray);
        intent.putExtra("month",listItems.get(position));
        intent.putExtra("subjectName",subjectName);
        intent.putExtra("CID",cid);
        startActivity(intent);
    }

    private void loadListItems() {
//        Toast.makeText(this, "Loading Items at", Toast.LENGTH_SHORT).show();
        Cursor cursor = new DBHelper(this).getDistinctMonths(cid);
        while (cursor.moveToNext()){
//            Toast.makeText(this, "Cursor will Take", Toast.LENGTH_SHORT).show();
            String date = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.DATE_KEY));
            listItems.add(date.substring(3));  //01.12.2024 we want only 12.2024
        }
    }
}