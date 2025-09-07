package com.example.attendanceapp;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class MyDialog extends DialogFragment {
    public static final String CLASS_ADD_DIALOG = "addClass";
    public  static  final String STUDENT_ADD_DIALOG="addStudent";
    public  static  final String CLASS_UPATE_DIALOG="updateClass";
    public static  final String STUDENT_UPDATE_DIALOG = "updateStudent";
    public static final String TEACHER_ADD_DIALOG = "addTeacher";
    onClickListener listener;
    private int roll;
    private String name;
    private String email;
    private String className,subjectName;

    public MyDialog(int rollNo, String name,String email) {
        this.roll = rollNo;
        this.email=email;
        this.name = name;
    }
    public MyDialog(String className,String subjectName){
        this.className=className;
        this.subjectName=subjectName;
    }

    public MyDialog(){}
    public interface onClickListener{
        public void onClick(String text1,String text2,String email);
    }

    public void setListener(onClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog=null;
        if(getTag().equals(CLASS_ADD_DIALOG)){
            dialog = getAddClassDialog();
        }
        if(getTag().equals(STUDENT_ADD_DIALOG)){
            dialog = getAddStudentDialog();
        }
        if(getTag().equals(CLASS_UPATE_DIALOG)){
            dialog = getUpdateClassDialog();
        }
        if(getTag().equals(STUDENT_UPDATE_DIALOG)){
            dialog = getStudentUpdateDialog();
            dialog.dismiss();
        }
        if(getTag().equals(TEACHER_ADD_DIALOG)){
            dialog = getTeacherAddDialog();
            dialog.dismiss();
        }
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        return dialog;
    }

    private Dialog getStudentUpdateDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        View view= LayoutInflater.from(getActivity()).inflate(R.layout.dialog,null);
        builder.setView(view);
        AlertDialog dialog=builder.create();
        TextView title = view.findViewById(R.id.Dialogtitle);
        title.setText("Update Student");
        TextInputEditText edtRoll = view.findViewById(R.id.edtClassName);
        edtRoll.setInputType(InputType.TYPE_CLASS_NUMBER);
        TextInputEditText edtName = view.findViewById(R.id.edtSubjectName);
        edtName.setText(name+"");
        edtRoll.setText(roll+"");
        TextInputLayout edtLayout1 = view.findViewById(R.id.textInputLayout1);
        TextInputLayout edtLayout2 = view.findViewById(R.id.textInputLayout2);
        edtLayout1.setHint("Roll");
        edtLayout2.setHint("Name");
        Button btnAdd = view.findViewById(R.id.btnAdd);

        Button btnCancel = view.findViewById(R.id.btnCancel);
        btnAdd.setText("Update");
        btnAdd.setOnClickListener(v->{
            String Roll = edtRoll.getText().toString();
            String Name = edtName.getText().toString();

            int rollNo = Integer.parseInt(Roll);
            listener.onClick(Roll,Name,email);
            dismiss();

        });
        btnCancel.setOnClickListener(v->dismiss());
        return builder.create();
    }

    private Dialog getUpdateClassDialog(){
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        View view= LayoutInflater.from(getActivity()).inflate(R.layout.dialog,null);
        builder.setView(view);
        AlertDialog dialog=builder.create();
        TextView title = view.findViewById(R.id.Dialogtitle);
        title.setText("Update Class");
        TextInputEditText edtSubjectName = view.findViewById(R.id.edtSubjectName);
        TextInputEditText edtClassName = view.findViewById(R.id.edtClassName);
        edtSubjectName.setText(subjectName);
        edtClassName.setText(className);
        TextInputEditText edtName = view.findViewById(R.id.edtSubjectName);
        TextInputLayout edtLayout1 = view.findViewById(R.id.textInputLayout1);
        TextInputLayout edtLayout2 = view.findViewById(R.id.textInputLayout2);
        edtLayout1.setHint("Class");
        edtLayout2.setHint("Subject");
        Button btnAdd = view.findViewById(R.id.btnAdd);
        Button btnCancel = view.findViewById(R.id.btnCancel);
        btnAdd.setText("Update");
        btnAdd.setOnClickListener(v->{
            String className = edtClassName.getText().toString();
            String subName = edtSubjectName.getText().toString();
            listener.onClick(className,subName,"");
            dismiss();
        });
        btnCancel.setOnClickListener(v->dismiss());
        return builder.create();
    }

    private Dialog getAddClassDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        View view= LayoutInflater.from(getActivity()).inflate(R.layout.dialog,null);
        builder.setView(view);
        AlertDialog dialog=builder.create();
        TextView title = view.findViewById(R.id.Dialogtitle);
        title.setText("Add New Class");
        TextInputEditText edtSubjectName = view.findViewById(R.id.edtSubjectName);
        TextInputEditText edtClassName = view.findViewById(R.id.edtClassName);
        TextInputEditText edtName = view.findViewById(R.id.edtSubjectName);
        TextInputLayout edtLayout1 = view.findViewById(R.id.textInputLayout1);
        TextInputLayout edtLayout2 = view.findViewById(R.id.textInputLayout2);
        edtLayout1.setHint("Class");
        edtLayout2.setHint("Subject");
        Button btnAdd = view.findViewById(R.id.btnAdd);
        Button btnCancel = view.findViewById(R.id.btnCancel);
        btnAdd.setOnClickListener(v->{
            String className = edtClassName.getText().toString();
            String subName = edtSubjectName.getText().toString();
            listener.onClick(className,subName,"");
            dismiss();
        });
        btnCancel.setOnClickListener(v->dismiss());
        return builder.create();
    }
    private Dialog getAddStudentDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();

        TextView title = view.findViewById(R.id.Dialogtitle);
        title.setText("Add New Student");

        TextInputEditText edtRoll = view.findViewById(R.id.edtClassName);
        TextInputEditText edtEmail = view.findViewById(R.id.edtEmail);
        TextInputEditText edtName = view.findViewById(R.id.edtSubjectName);
        edtRoll.setVisibility(View.GONE);

        TextInputLayout edtLayout1 = view.findViewById(R.id.textInputLayout1);
        TextInputLayout edtLayout2 = view.findViewById(R.id.textInputLayout2);
        TextInputLayout edtLayout3 = view.findViewById(R.id.textInputLayout3);

        edtEmail.setVisibility(View.VISIBLE);

        edtLayout1.setHint("Roll");
        edtLayout2.setHint("Name");
        edtLayout3.setHint("Email");

        Button btnAdd = view.findViewById(R.id.btnAdd);
        Button btnCancel = view.findViewById(R.id.btnCancel);

        btnAdd.setOnClickListener(v -> {
            String roll = edtRoll.getText().toString().trim();
            String name = edtName.getText().toString().trim();
            String email = edtEmail.getText().toString().trim();

            if (email.isEmpty()) {
                edtLayout3.setError("Email is required");
                return; // Prevent dialog submission
            } else {
                edtLayout3.setError(null); // Clear error
            }

            if (name.isEmpty()) name = "Unknown";
            if (roll.isEmpty()) roll = "1";

            int rollNo = Integer.parseInt(roll);
            listener.onClick(roll,name,email);

            edtName.setText("");
            edtEmail.setText("");
            edtRoll.setText(Integer.toString(rollNo + 1));
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        return dialog;
    }
    private Dialog getTeacherAddDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        View view= LayoutInflater.from(getActivity()).inflate(R.layout.dialog,null);
        builder.setView(view);
        AlertDialog dialog=builder.create();
        TextView title = view.findViewById(R.id.Dialogtitle);
        title.setText("Add New Teacher");
        TextInputEditText edtTeacherID = view.findViewById(R.id.edtClassName);
        TextInputEditText edtTeacherName = view.findViewById(R.id.edtSubjectName);
        TextInputLayout edtLayout1 = view.findViewById(R.id.textInputLayout1);
        TextInputLayout edtLayout2 = view.findViewById(R.id.textInputLayout2);
        edtLayout2.setHint("Name");
        edtLayout1.setFocusable(false);
        edtLayout1.setClickable(false);
        DBHelper dbHelper = new DBHelper(getActivity());
        long nextTeacherID = dbHelper.getNextTeacherID();
        Log.e("nextTeacherID",""+nextTeacherID);
        edtTeacherID.setText(String.valueOf(nextTeacherID));
        Button btnAdd = view.findViewById(R.id.btnAdd);
        Button btnCancel = view.findViewById(R.id.btnCancel);
        btnAdd.setOnClickListener(v->{
//            String TeacherID = edtTeacherID.getText().toString();
            String TeacherName = edtTeacherName.getText().toString();
//            int teacherId = Integer.parseInt(TeacherID);
            listener.onClick(String.valueOf(nextTeacherID),TeacherName,"fromTeacherPage");
            dismiss();
        });
        btnCancel.setOnClickListener(v->dismiss());
        return builder.create();
    }


}
