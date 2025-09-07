
package com.example.attendanceapp.ModalClasses;


public class AttendanceItems {
    private String subjectName;
    private int present;
    private int total;

    public AttendanceItems(String subjectName, int present, int total) {
        this.subjectName = subjectName;
        this.present = present;
        this.total = total;
    }

    public String getSubjectName() { return subjectName; }
    public int getPresent() { return present; }
    public int getTotal() { return total; }
}
