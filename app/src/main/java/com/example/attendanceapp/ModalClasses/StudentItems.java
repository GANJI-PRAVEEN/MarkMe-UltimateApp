package com.example.attendanceapp.ModalClasses;

public class StudentItems {
    private int rollNo;
    private String name;
    private long sid;
    private String status;
    private long newClassID;
    private String email;

    public StudentItems(long sid,int rollNo, String name,String email,long newClassID) {
        this.rollNo = rollNo;
        this.name = name;
        this.sid = sid;
        this.email = email;
        this.newClassID = newClassID;
        status = "";
    }

    public int getRollNo() {
        return rollNo;
    }
    public String getEmail(){
        return email;
    }
    public long getNewClassID(){return newClassID;}

    public void setRollNo(int rollNo) {
        this.rollNo = rollNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status,long newClassID) {
        this.status = status;
        this.newClassID = newClassID;

    }

    public long getSid() {
        return sid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }
}
