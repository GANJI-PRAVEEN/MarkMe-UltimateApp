package com.example.attendanceapp.ModalClasses;

import android.content.Context;

public class ClassItems {
    private String ClassName,SubjectName;
    private long cid;
    private Context context;
    public ClassItems(Context context){
        this.context = context;
    }

    public ClassItems(String className, String subjectName, long cid) {
        ClassName = className;
        SubjectName = subjectName;
        this.cid = cid;
    }

    public ClassItems(String className, String subjectName) {
        ClassName = className;
        SubjectName = subjectName;
    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String className) {
        ClassName = className;
    }

    public String getSubjectName() {
        return SubjectName;
    }

    public void setSubjectName(String subjectName) {
        SubjectName = subjectName;
    }

    public long getCid() {
        return cid;
    }

    public void setCid(long cid) {
        this.cid = cid;
    }
}
