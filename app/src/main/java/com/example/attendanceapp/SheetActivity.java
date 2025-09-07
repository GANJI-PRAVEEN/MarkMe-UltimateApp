package com.example.attendanceapp;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.inputmethodservice.Keyboard;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.TintableBackgroundView;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

//import org.apache.poi.ss.usermodel.*;
//import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.Calendar;

public class    SheetActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private String subjectName;
    private TextView txtSubjectName;
    private long CID;
    private Button btnExport;
    private int DAYS_IN_MONTH;
    private ImageButton toolbackIcon,toolbarSaveIcon;
    private TextView title,subTitle;
    public static final String WRITE_EXTERNAL_STORAGE = "android.permission.WRITE_EXTERNAL_STORAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
//            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                    != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(
//                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                        1
//                );
//            }
//        }


        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sheet);
        subjectName = getIntent().getStringExtra("subjectName");
        toolbar = findViewById(R.id.toolbar);
        toolbackIcon = findViewById(R.id.toolbarBackIcon);
        toolbarSaveIcon = findViewById(R.id.toolbarSaveIcon);
        toolbarSaveIcon.setVisibility(View.GONE);
        subTitle = findViewById(R.id.toolbarSubtitle);
        toolbackIcon.setOnClickListener((v)->onBackPressed());
        TextView toolBarTitle = findViewById(R.id.toolbarTitle);
        toolBarTitle.setText("Attendance Sheet");
        TextView toolBarSubtitle = findViewById(R.id.toolbarSubtitle);
        toolBarSubtitle.setText("Progress");
        btnExport = findViewById(R.id.btnExportToExcel);
        ImageButton saveImageButton = findViewById(R.id.toolbarSaveIcon);
        saveImageButton.setVisibility(View.GONE);
        TextView subTitle  = findViewById(R.id.toolbarSubtitle);
        subTitle.setVisibility(View.GONE);
        txtSubjectName = findViewById(R.id.subjectName);
        txtSubjectName.setText(subjectName);
        btnExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[] rollArray=getIntent().getIntArrayExtra("rollArray");
                long []idArray = getIntent().getLongArrayExtra("idArray");
                String[] nameArray = getIntent().getStringArrayExtra("nameArray");
                String month = getIntent().getStringExtra("month");
                CID= getIntent().getLongExtra("CID",-1);
                exportToExcel(rollArray,idArray,nameArray,month,CID);
            }
        });
        showTable();

    }

    private void showTable(){
//        Toast.makeText(this, "SheetActivity YEsss", Toast.LENGTH_SHORT).show();
        DBHelper dbHelper =new DBHelper(this);
        TableLayout tableLayout = findViewById(R.id.tableLayout);
        int[] rollArray=getIntent().getIntArrayExtra("rollArray");
        long []idArray = getIntent().getLongArrayExtra("idArray");
        String[] nameArray = getIntent().getStringArrayExtra("nameArray");
        String month = getIntent().getStringExtra("month");
        CID= getIntent().getLongExtra("CID",-1);
        Log.e("CID IN ACTUAL SHEET",""+CID);
        DAYS_IN_MONTH =  getDaysInMonth(month);

        //Table Creation with rows and colums
        //rows = no of month
        //column = no of days in a month


        //row setUp
        int rowSize = idArray.length;
        Log.e("IDARRAYSIZE ",""+rowSize);
        TableRow[] rows = new TableRow[rowSize+1];
        TextView[] roll_tvs = new TextView[rowSize+1];
        TextView[] name_tvs = new TextView[rowSize+1];
        TextView[][]status_tvs  = new TextView[rowSize+1][DAYS_IN_MONTH+1];

        for (int i = 0; i <=rowSize; i++) {
            roll_tvs[i] = new TextView(this);
            name_tvs[i] = new TextView(this);
            for (int j = 0; j <DAYS_IN_MONTH+1; j++) {
                status_tvs[i][j] =new TextView(this);
            }
        }

        //header
        roll_tvs[0].setText("Roll");
        roll_tvs[0].setTypeface(roll_tvs[0].getTypeface(), Typeface.BOLD);
        name_tvs[0].setTypeface(name_tvs[0].getTypeface(), Typeface.BOLD);
        name_tvs[0].setText("Name");
        for (int i = 0; i < DAYS_IN_MONTH+1; i++) {
            status_tvs[0][i].setText(String.valueOf(i));
            status_tvs[0][i].setTypeface(status_tvs[0][i].getTypeface(), Typeface.BOLD);
        }

        for (int i = 0; i <rowSize; i++) {
            roll_tvs[i+1].setText(String.valueOf(rollArray[i]));
            name_tvs[i+1].setText(String.valueOf(nameArray[i]));

            for (int j = 1; j < DAYS_IN_MONTH+1; j++) {
                String day = String.valueOf(j);
                if(day.length()==1)day = "0"+day;
                String date = day+"."+month;
                String status = dbHelper.getStatus(idArray[i],date,CID);
                Log.e("Name of Student",""+nameArray[i]);
                Log.e("status ",status);

                status_tvs[i+1][j].setText(status);
            }
        }

        for (int i = 0; i <=rowSize; i++) {
            rows[i]=new TableRow(this);
            roll_tvs[i].setPadding(16,16,16,16);
            name_tvs[i].setPadding(16,16,16,16);

            rows[i].addView(roll_tvs[i]);
            rows[i].addView(name_tvs[i]);

            if(i%2==0){
                rows[i].setBackgroundColor(Color.parseColor("#EEEEEE"));
            }
            else rows[i].setBackgroundColor(Color.parseColor("#E4E4E4"));

            for (int j = 0; j < DAYS_IN_MONTH+1; j++) {
                rows[i].addView(status_tvs[i][j]);
                status_tvs[i][j].setPadding(16,16,16,16);
            }
            tableLayout.addView(rows[i]);
        }
        tableLayout.setShowDividers(TableLayout.SHOW_DIVIDER_MIDDLE);;
    }

    private int getDaysInMonth(String month) {
        int monthIndex = Integer.valueOf(month.substring(0,1));
        int yearIndex  = Integer.valueOf(month.substring(4));
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH,monthIndex);
        calendar.set(Calendar.YEAR,yearIndex);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);  //return no of days in a month
    }
    private void exportToExcel(int[] rollArray, long[] idArray, String[] nameArray, String month, long CID) {
        try {
            Toast.makeText(this, "Exporting", Toast.LENGTH_SHORT).show();
            StringBuilder data = new StringBuilder();

            data.append(",,,,,").append(subjectName).append(" - "+month).append(",,,,,").append("\n\n");


            data.append("Roll,Name");
            for (int d = 1; d <= 30; d++) {
                data.append(",").append(d);
            }
            data.append("\n");

            DBHelper dbHelper = new DBHelper(this);
            for (int i = 0; i < idArray.length; i++) {
                data.append(rollArray[i]).append(",");
                data.append(nameArray[i]);

                for (int d = 1; d <= 30; d++) {
                    String day = (d < 10 ? "0" + d : String.valueOf(d));
                    String date = day + "." + month;
                    String status = dbHelper.getStatus(idArray[i], date, CID);
                    data.append(",").append(status);
                }
                data.append("\n");
            }

            // ✅ Use timestamp to make unique filename
            String fileName = "Attendance_" + month + "_" + System.currentTimeMillis() + ".csv";

            File file = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    fileName
            );

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data.toString().getBytes());
            fos.close();

            // ✅ Update MediaStore so file appears instantly in MyFiles
            MediaScannerConnection.scanFile(this, new String[]{file.getAbsolutePath()}, null, null);

            Toast.makeText(this, "Saved to Downloads as " + fileName, Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }






}