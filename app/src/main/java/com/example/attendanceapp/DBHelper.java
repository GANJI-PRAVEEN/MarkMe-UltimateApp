package com.example.attendanceapp;

import static javax.xml.transform.OutputKeys.VERSION;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.telephony.mbms.StreamingServiceInfo;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.attendanceapp.Adapters.AttendanceAdapter;
import com.example.attendanceapp.ModalClasses.AttendanceItems;

import java.sql.SQLInput;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Queue;

public class DBHelper extends SQLiteOpenHelper {
    public static final int VERSION=2;
    public static final String CLASS_TABLE_NAME="CLASS_TABLE";

    public static final String CID="_CID";
    public static final String CLASS_NAME_KEY="CLASS_NAME";
    public static final String SUBJECT_NAME_KEY="SUBJECT_NAME";

    //Creating classTable with columns of Class_Id,Class_Name,Subject_Name

    public static final String CREATE_CLASS_TABLE=
            "CREATE TABLE " + CLASS_TABLE_NAME + "(" +
                    CID + " INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    CLASS_NAME_KEY + " TEXT NOT NULL, " +
                    SUBJECT_NAME_KEY + " TEXT NOT NULL, " +
                    "UNIQUE (" + CLASS_NAME_KEY + "," + SUBJECT_NAME_KEY + ")"+
                    ");";
    public static final String DROP_CLASS_TABLE = "DROP TABLE IF EXISTS "+CLASS_TABLE_NAME;
    public static  final String SELECT_CLASS_TABLE = "SELECT * FROM "+CLASS_TABLE_NAME;


    public static final String TEACHER_ID = "TEACHER_ID";
    public static final String TEACHER_NAME = "TEACHER_NAME";
    public static final String TEACHER_TABLE_NAME = "TEACHER_TABLE";
    public static final String SECRET_TEACHER_ID = "SECRET_TEACHER_ID";



    //Teacher Table
    public static final String CREATE_TEACHER_TABLE =
            "CREATE TABLE " + TEACHER_TABLE_NAME + " (" +
                TEACHER_ID + " INTEGER PRIMARY KEY , " +
                TEACHER_NAME + " TEXT, " +
                SECRET_TEACHER_ID + " TEXT NOT NULL" +
            ");";

    public static final String DROP_TEACHER_TABLE = "DROP TABLE IF EXISTS "+TEACHER_TABLE_NAME;
    public static  final String SELECT_TEACHER_TABLE = "SELECT * FROM "+TEACHER_TABLE_NAME;

    //Student Table
    public static final String STUDENT_TABLE_NAME="STUDENT_TABLE";
    public static final String SID="_SID";
    public static final String STUDENT_NAME_KEY="STUDENT_NAME";
    public static final String STUDENT_ROLL_KEY="STUDENT_ROLL";
    public static final String STUDENT_EMAIL="EMAIL";

    public static final String CREATE_STUDENT_TABLE=
            "CREATE TABLE " + STUDENT_TABLE_NAME + "(" +
                    STUDENT_EMAIL + " TEXT NOT NULL, "+
                    SID + " INTEGER  NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                    CID + " INTEGER NOT NULL, "+
                    STUDENT_NAME_KEY + " TEXT NOT NULL," +
                    STUDENT_ROLL_KEY + " INTEGER NOT NULL, " +
                    "UNIQUE (" + STUDENT_ROLL_KEY + "), " +
                    "UNIQUE (" + STUDENT_EMAIL +" ),"+
                    " FOREIGN KEY ( "+CID+") REFERENCES "+CLASS_TABLE_NAME + "("+CID+")"+
                    ")";
    public static final String DROP_STUDENT_TABLE = "DROP TABLE IF EXISTS "+STUDENT_TABLE_NAME;
    public static  final String SELECT_STUDENT_TABLE = "SELECT * FROM "+STUDENT_TABLE_NAME;

    //Status Table

    public static final String STATUS_TABLE_NAME = "STATUS_TABLE";
    public static final String STATUS_ID = "_STATUS_ID";
    public static final String DATE_KEY = "STATUS_DATE";
    public static final String STATUS_KEY = "STATUS";
    public static final String CREATE_STATUS_TABLE=
            "CREATE TABLE "+STATUS_TABLE_NAME +
                    "( "+
                    STATUS_ID + " INTEGER NOT NULL, "+
                    DATE_KEY + " DATE NOT NULL, "+
                    CID + " INTEGER NOT NULL, "+
                    STATUS_KEY + " TEXT NOT NULL, "+
                    "FOREIGN KEY ("+STATUS_ID+") REFERENCES "+ STUDENT_TABLE_NAME+"( "+SID+"),"+
                    "FOREIGN KEY ("+CID+") REFERENCES "+ CLASS_TABLE_NAME+"( "+CID+")"+
                    ");";
    public static final String DROP_STATUS_TABLE = "DROP TABLE IF EXISTS "+STATUS_TABLE_NAME;
    public static final String SELECT_STATUS_TABLE = "SELECT * FROM "+STATUS_TABLE_NAME;
    public DBHelper(@Nullable Context context) {
        super(context,"Attendance.db",null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_CLASS_TABLE);
        db.execSQL(CREATE_STUDENT_TABLE);
        db.execSQL(CREATE_STATUS_TABLE);
        db.execSQL(CREATE_TEACHER_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_CLASS_TABLE);
        db.execSQL(DROP_STUDENT_TABLE);
        db.execSQL(DROP_STATUS_TABLE);
        db.execSQL(DROP_TEACHER_TABLE);
        onCreate(db);
    }
    public long addStudent(long cid,int roll,String studentName,String email){
        if(checkEmailAlreadyExists(email)){
            return -1;
        }
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values=new ContentValues();
        String sid = String.valueOf(roll);
        String secretID = "202500"+sid;
        values.put(STUDENT_EMAIL,email);
        values.put(STUDENT_NAME_KEY,studentName);
        values.put(STUDENT_ROLL_KEY,Integer.parseInt(secretID));
        values.put(CID,cid);
        long newIncreementedSID =  database.insert(STUDENT_TABLE_NAME,null,values);
        if(newIncreementedSID!=-1){
            int realSID = -1;
            Cursor cursor = database.rawQuery("SELECT _SID FROM STUDENT_TABLE WHERE EMAIL =? AND STUDENT_NAME = ?",new String[]{email,studentName});
            if(cursor!=null){
                cursor.moveToFirst();
                realSID = cursor.getInt(cursor.getColumnIndexOrThrow(SID));
            }
            Log.e("realSID ",""+realSID);
            String secretIdOfStudent;
            if(realSID>=10){
                secretIdOfStudent = "20250"+realSID;
            }
            else secretIdOfStudent = "202500"+realSID;
            ContentValues contentValues =  new ContentValues();
            contentValues.put(STUDENT_ROLL_KEY,Integer.parseInt(secretIdOfStudent));
            database.update(STUDENT_TABLE_NAME,contentValues,"_SID = ?",new String[]{String.valueOf(realSID)});
        }
        return newIncreementedSID;
    }
    public boolean checkEmailAlreadyExists(String email) {
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT * FROM " + STUDENT_TABLE_NAME + " WHERE " + STUDENT_EMAIL + "=?";
        Cursor result = database.rawQuery(query, new String[]{email});
        if (result.getCount() > 0) return true;
        return false;
    }

    public Cursor getStudentTable(){
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery(SELECT_STUDENT_TABLE,null);
    }
    public long addClass(String className,String subjectName){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values =  new ContentValues();
        values.put(CLASS_NAME_KEY,className);
        values.put(SUBJECT_NAME_KEY,subjectName);
        return database.insert(CLASS_TABLE_NAME,null,values);
    }
    public Cursor getClassTable(){
        SQLiteDatabase database= this.getReadableDatabase();
        return database.rawQuery(SELECT_CLASS_TABLE,null);
    }
    public int deleteClass(long cid){
        SQLiteDatabase database = this.getReadableDatabase();
        return database.delete(CLASS_TABLE_NAME,CID+"=?",new String[]{String.valueOf(cid)});
    }
    public long updateClass(long cid,String className,String subjectName){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values =  new ContentValues();
        values.put(CLASS_NAME_KEY,className);
        values.put(SUBJECT_NAME_KEY,subjectName);
        return database.update(CLASS_TABLE_NAME,values,CID+"=?",new String[]{String.valueOf(cid)});
    }
    public long updateStudent(long sid,String studentName,long cid){
        SQLiteDatabase database =this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STUDENT_NAME_KEY,studentName);
        values.put(CID,cid);
        return database.update(STUDENT_TABLE_NAME,values,SID+"=?",new String[]{String.valueOf(sid)});
    }
    public int deleteStudent(long sid){
        SQLiteDatabase database = this.getWritableDatabase();
        return database.delete(STUDENT_TABLE_NAME,SID+"=?",new String[]{String.valueOf(sid)});
    }
    public long addStatus(long statusId, String status, long classId, String statusDate) {
        Log.e("AddingStatus ","ClassID : "+classId);
        SQLiteDatabase database = this.getWritableDatabase();
        long result=0;

        // First, check if the same (statusId, statusDate) already exists
        Cursor cursor = database.query(
                STATUS_TABLE_NAME,
                null,
                STATUS_ID + "=? AND " + DATE_KEY + "=?"+" AND "+CID + "=?",
                new String[]{String.valueOf(statusId), statusDate,String.valueOf(classId)},
                null, null, null
        );

        if (cursor != null && cursor.moveToFirst()) {
            // 🔁 Record exists → Update it
            ContentValues updateValues = new ContentValues();
            updateValues.put(STATUS_KEY, status);
            Log.e("CID IN ADDSTATUS()"," "+classId);
            database.update(
                    STATUS_TABLE_NAME,
                    updateValues,
                    STATUS_ID + "=? AND " + DATE_KEY + "=? AND "+CID+ " =?",
                    new String[]{String.valueOf(statusId), statusDate,String.valueOf(classId)}
            );
        } else {
            Log.e("Adding","Status");
            ContentValues insertValues = new ContentValues();
            insertValues.put(STATUS_KEY, status);
            insertValues.put(STATUS_ID, statusId);
            insertValues.put(CID, classId);
            insertValues.put(DATE_KEY, statusDate);

           result = database.insert(STATUS_TABLE_NAME, null, insertValues);
        }

        if(cursor==null)return -1;
        else cursor.close();
        return result;
    }

    public long updateStatus(long sid, String date, String status) {
        if (sid < 0) return -1;

        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(STATUS_KEY, status);

        // Only update the row for this student AND this date
        String whereClause = STATUS_ID + "=? AND " + DATE_KEY + "=?";
        String[] whereArgs = { String.valueOf(sid), date };

        long rowsUpdated = database.update(STATUS_TABLE_NAME, values, whereClause, whereArgs);
        Log.e("UpdateStatus", "Rows updated: " + rowsUpdated);
        return rowsUpdated;
    }

    public String getStatus(long sid, String date, long classID) {
        String status = "A"; // default to Absent
        SQLiteDatabase database = this.getReadableDatabase();

        String whereClause = DATE_KEY + "= ? AND " + STATUS_ID + "= ? AND " + CID + "=?";
        String[] whereArgs = { date, String.valueOf(sid), String.valueOf(classID) };

        try (Cursor cursor = database.query(STATUS_TABLE_NAME,
                new String[]{STATUS_KEY},
                whereClause,
                whereArgs,
                null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                status = cursor.getString(cursor.getColumnIndexOrThrow(STATUS_KEY));
            }
        } catch (Exception e) {
            Log.e("Exception Occurred for getStatus", e.toString());
        }
        Log.e("STATUS ",status);
        return status;
    }

    public Cursor getDistinctMonths(long cid){
        SQLiteDatabase database = this.getReadableDatabase();
        return database.query(STATUS_TABLE_NAME,new String[]{DATE_KEY},CID+"="+cid,null,"substr(" + DATE_KEY + ",4,7)",null,null);  //01.12.2024 we want only 12.2024
    }
    public Boolean getSatusOfStudent(int sid){
        Boolean bool=false;
        String todayDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        SQLiteDatabase database = this.getReadableDatabase();
        String whereClause = DATE_KEY+"="+ todayDate+" AND "+STATUS_ID+"= "+sid;
        Cursor cursor = database.query(STATUS_TABLE_NAME,null,whereClause,null,null,null,null);
        while(cursor.moveToNext()){
            String status = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.STATUS_KEY));
            if(status=="P"){
                bool = true;
            }
        }
        return bool;
    }
    public long getClassID(String className, String subjectName) {
        if (className == null || subjectName == null) {
            Log.e("DatabaseProblem", "className or subjectName is null");
            return -1; // or handle the error as needed
        }

        SQLiteDatabase db = this.getReadableDatabase();
        long classId = -1; // Default value if not found

        String selection = CLASS_NAME_KEY + "=? AND " + SUBJECT_NAME_KEY + "=?";
        String[] selectionArgs = new String[]{className, subjectName};
        Cursor cursor=null;
        try{ cursor = db.query(CLASS_TABLE_NAME, new String[]{CID}, selection, selectionArgs, null, null, null);}
        catch(Exception e){
            Log.e("DatabaseProblem",e.toString());
        }

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int idIndex = cursor.getColumnIndex(CID);
                if (idIndex != -1) {
                    classId = cursor.getLong(idIndex);
                }
            }
            cursor.close();
        }

        return classId; // -1 means not found
    }
    public long addTeacher(String teacher_name){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues values=new ContentValues();
        long currentTeacherID = getNextTeacherID();
        Log.e("currentTeacherID",""+currentTeacherID);
        String secretTeacherID = "2025"+currentTeacherID;
        Log.e("SecretTeacherID ",secretTeacherID);
        values.put(TEACHER_NAME,teacher_name);
        values.put(TEACHER_ID,currentTeacherID);
        values.put(SECRET_TEACHER_ID,secretTeacherID);
        return database.insert(TEACHER_TABLE_NAME,null,values);
    }
    public long getNextTeacherID() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT MAX(TEACHER_ID) FROM TEACHER_TABLE", null);
        long nextID = 101; // default for first entry

        if (cursor.moveToFirst()) {
            if(!cursor.isNull(0)) {
                Log.e("it is going", cursor.toString());
                long maxID = cursor.getLong(0);
                nextID = maxID + 1;
            }
        }
        cursor.close();
        return nextID;
    }
    public Cursor getTeacherTable(){
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery(SELECT_TEACHER_TABLE,null);
    }
    public int deleteTeacher(long tid){
        SQLiteDatabase database = this.getWritableDatabase();
        return database.delete(TEACHER_TABLE_NAME,TEACHER_ID+"=?",new String[]{String.valueOf(tid)});
    }

    //While Student Logging checking credentials
    public Cursor checkStudentExistsInDB(String stdEmail,String stdPassword){
        Log.e("Email ",stdEmail);
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = getStudentTable();
        String query = " SELECT * FROM "+STUDENT_TABLE_NAME + " WHERE "+STUDENT_EMAIL + " = ? AND "+STUDENT_ROLL_KEY+" = ? ";
        Cursor cursor1 = database.rawQuery(query,new String[]{stdEmail,stdPassword});
        boolean stdExists = cursor1.getCount()>0;
        return cursor1;
    }
    public Cursor checkTeacherExistInDB(String secretPassword){
        Log.e("INDD",secretPassword);
        SQLiteDatabase database = getReadableDatabase();
        String query = "SELECT * FROM "+TEACHER_TABLE_NAME+" WHERE "+SECRET_TEACHER_ID +" = ?";
        Cursor cursor = database.rawQuery(query,new String[]{secretPassword});
        return cursor;
    }
    public Cursor getMonthlyAttendance(long statusId) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT c." + SUBJECT_NAME_KEY + ", " +
                "COALESCE(SUM(CASE WHEN s." + STATUS_KEY + " = 'P' THEN 1 ELSE 0 END), 0) AS PresentCount, " +
                "30 AS TotalClasses " +  // fixed total per month
                "FROM " + CLASS_TABLE_NAME + " c " +
                "LEFT JOIN " + STATUS_TABLE_NAME + " s " +
                "ON c." + CID + " = s." + CID + " " +
                "AND s." + STATUS_ID + " = ? " +   // student filter in JOIN
                "AND substr(s." + DATE_KEY + ", 4, 2) = substr(date('now'), 6, 2) " +   // same month
                "AND substr(s." + DATE_KEY + ", 7, 4) = substr(date('now'), 1, 4) " +   // same year
                "AND CAST(substr(s." + DATE_KEY + ", 1, 2) AS INTEGER) <= CAST(strftime('%d','now') AS INTEGER) " + // day <= today
                "GROUP BY c." + SUBJECT_NAME_KEY + ";";



        return db.rawQuery(query, new String[]{String.valueOf(statusId)});
    }
    public int getSecretIDOfStudent(long rollkey){
        SQLiteDatabase database = this.getReadableDatabase();
        Cursor cursor = database.rawQuery("SELECT STUDENT_ROLL FROM STUDENT_TABLE WHERE _SID = ?",new String[]{String.valueOf(rollkey)});
        int secretId = -1;
        if(cursor!=null) {
            if(cursor.moveToFirst()) {
                secretId = cursor.getInt(cursor.getColumnIndexOrThrow(STUDENT_ROLL_KEY));
            }else{
                Log.e("Sorry",cursor.toString());
            }
        }
        Log.e("NOIDFOUND",cursor.toString());
        return secretId;


    }
//    public List<AttendanceItems> loadAttendanceReport(long sid) {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        // Query for per subject attendance
//        String query = "SELECT c." + SUBJECT_NAME_KEY + " AS Subject, " +
//                "SUM(CASE WHEN a." + STATUS_KEY + " = 'P' THEN 1 ELSE 0 END) AS PresentCount, " +
//                "COUNT(*) AS TotalClasses " +
//                "FROM " + STATUS_TABLE_NAME + " a " +
//                "JOIN " + CLASS_TABLE_NAME + " c ON a." + CID + " = c." + CID + " " +  // <-- space added
//                "WHERE a." + SID + " = ? " +
//                "GROUP BY c." + SUBJECT_NAME_KEY;
//
//
//        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(sid)});
//
//        List<AttendanceItems> attendanceList = new ArrayList<>();
//        int totalPresent = 0, totalClasses = 0;
//
//        if (cursor.moveToFirst()) {
//            do {
//                String subject = cursor.getString(cursor.getColumnIndexOrThrow("Subject"));
//                int present = cursor.getInt(cursor.getColumnIndexOrThrow("PresentCount"));
//                int total = cursor.getInt(cursor.getColumnIndexOrThrow("TotalClasses"));
//
//                attendanceList.add(new AttendanceItems(subject, present, total));
//
//                totalPresent += present;
//                totalClasses += total;
//
//            } while (cursor.moveToNext());
//        }
//        cursor.close();
//        return attendanceList;
//    }

    public Cursor getStudentProfile(long studentID){
        SQLiteDatabase database = this.getReadableDatabase();
        String query = "SELECT * FROM "+STUDENT_TABLE_NAME+" WHERE "+SID+" =?";
        return database.rawQuery(query,new String[]{String.valueOf(studentID)});
    }



}
