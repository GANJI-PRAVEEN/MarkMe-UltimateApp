package com.example.attendanceapp.ModalClasses;


public class TeacherItems {
    private long teacherID;
    private String teacherName;




    public TeacherItems(long teacherID, String teacherName) {
        this.teacherID = teacherID;
        this.teacherName = teacherName;
    }

    public long getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(long teacherID) {
        this.teacherID = teacherID;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }
}


