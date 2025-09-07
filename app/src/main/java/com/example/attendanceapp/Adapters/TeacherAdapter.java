package com.example.attendanceapp.Adapters;


import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendanceapp.ModalClasses.StudentItems;
import com.example.attendanceapp.ModalClasses.TeacherItems;
import com.example.attendanceapp.R;

import java.util.ArrayList;

public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.TeacherViewHolder> {
    ArrayList<TeacherItems> teacherItemArrayList;
    Context context;
    private onItemClickListener itemListener;
    private CardView cardView;

    public interface onItemClickListener {
        void onClick(int position);
    }

    public void setItemListener(onItemClickListener itemListener) {
        this.itemListener = itemListener;
    }

    public static class TeacherViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        public TextView teacherName;
        public TextView teacherID;
        CardView cardView;
        private onItemClickListener itemClickListener;

        public TeacherViewHolder(@NonNull View itemView, onItemClickListener itemClickListener) {
            super(itemView);
            teacherName = itemView.findViewById(R.id.teacher_name);
            teacherID = itemView.findViewById(R.id.teacherID);
            cardView = itemView.findViewById(R.id.teacher_cardview);
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
//            menu.add(getAdapterPosition(),0,0,"EDIT");
            menu.add(getAdapterPosition(),0,0,"DELETE");
        }
    }

    public TeacherAdapter(Context context, ArrayList<TeacherItems> teachersItems) {
        this.teacherItemArrayList = teachersItems;
        this.context = context;
    }

    @NonNull
    @Override
    public TeacherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.teacher_view, parent, false);
        return new TeacherViewHolder(view, itemListener);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherViewHolder holder, int position) {
        holder.teacherID.setText(String.valueOf(teacherItemArrayList.get(position).getTeacherID()));
        Log.e("TeacherIDInAdapter",""+teacherItemArrayList.get(position).getTeacherID());
        holder.teacherName.setText(teacherItemArrayList.get(position).getTeacherName());
    }

    @Override
    public int getItemCount() {
        return teacherItemArrayList.size();
    }
}
