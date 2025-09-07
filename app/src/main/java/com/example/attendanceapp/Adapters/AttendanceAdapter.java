package com.example.attendanceapp.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.example.attendanceapp.ModalClasses.*;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendanceapp.R;
import com.example.attendanceapp.ModalClasses.AttendanceItems;

import java.util.List;

public class AttendanceAdapter extends RecyclerView.Adapter<AttendanceAdapter.ViewHolder> {

    private List<AttendanceItems> attendanceList;

    public AttendanceAdapter(List<AttendanceItems> attendanceList) {
        this.attendanceList = attendanceList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_attendance, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AttendanceItems model = attendanceList.get(position);

        holder.tvSubjectName.setText(model.getSubjectName());

        int percent = (int) ((model.getPresent() * 100.0) / model.getTotal());
        holder.progressAttendance.setProgress(percent);

        holder.tvAttendanceRatio.setText(model.getPresent() + "/" + model.getTotal());
    }

    @Override
    public int getItemCount() {
        return attendanceList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSubjectName, tvAttendanceRatio;
        ProgressBar progressAttendance;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvSubjectName = itemView.findViewById(R.id.tvSubjectName);
            tvAttendanceRatio = itemView.findViewById(R.id.tvAttendanceRatio);
            progressAttendance = itemView.findViewById(R.id.progressAttendance);
        }
    }
}
