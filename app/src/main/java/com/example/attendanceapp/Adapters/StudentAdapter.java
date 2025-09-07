package com.example.attendanceapp.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendanceapp.DBHelper;
import com.example.attendanceapp.ModalClasses.StudentItems;
import com.example.attendanceapp.R;

import java.util.ArrayList;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.StudentViewHolder> {
    ArrayList<StudentItems> studentItemsArrayList;
    Context context;
    private onItemClickListener itemListener;
    private CardView cardView;

    public interface onItemClickListener {
        void onClick(int position);
    }

    public void setItemListener(onItemClickListener itemListener) {
        this.itemListener = itemListener;
    }

    public static class StudentViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        public TextView studentName;
        public TextView studentRoll;
        private TextView studentStatus;
        CardView cardView;
        private onItemClickListener itemClickListener;

        public StudentViewHolder(@NonNull View itemView, onItemClickListener itemClickListener) {
            super(itemView);
            studentName = itemView.findViewById(R.id.name);
            studentRoll = itemView.findViewById(R.id.roll);
            studentStatus = itemView.findViewById(R.id.status);
            cardView = itemView.findViewById(R.id.student_cardView);
            this.itemClickListener = itemClickListener;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onClick(getAdapterPosition());
                    }
                }
            });
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(getAdapterPosition(),0,0,"EDIT");
            menu.add(getAdapterPosition(),1,0,"DELETE");
        }
    }

    public StudentAdapter(Context context, ArrayList<StudentItems> studentItems) {
        this.studentItemsArrayList = studentItems;
        this.context = context;
    }

    @NonNull
    @Override
    public StudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_view, parent, false);
        return new StudentViewHolder(view, itemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull StudentViewHolder holder, int position) {
        holder.studentRoll.setText(String.valueOf(studentItemsArrayList.get(position).getSid()));
        holder.studentName.setText(studentItemsArrayList.get(position).getName());
        holder.studentStatus.setText(studentItemsArrayList.get(position).getStatus());
        holder.cardView.setBackgroundColor(getColor(position));
    }
    private int getColor(int position){
        String status = studentItemsArrayList.get(position).getStatus();
        if(status.equals("P")){
            return Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(context,R.color.present)));
        }
        else if(status.equals("A")){
            return Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(context,R.color.absent)));
        }
        return Color.parseColor("#"+Integer.toHexString(ContextCompat.getColor(context,R.color.white)));
    }

    @Override
    public int getItemCount() {
        return studentItemsArrayList.size();
    }
}
