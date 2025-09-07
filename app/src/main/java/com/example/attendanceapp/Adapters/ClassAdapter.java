package com.example.attendanceapp.Adapters;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.attendanceapp.ModalClasses.ClassItems;
import com.example.attendanceapp.R;

import java.util.ArrayList;

public class ClassAdapter extends RecyclerView.Adapter<ClassAdapter.MyViewHolder>{
    ArrayList<ClassItems>runninClassesList;
    Context context;
    private onItemClickListener itemListener;
    public interface onItemClickListener {
        void onClick(int position);
    }

    public void setItemListener(onItemClickListener itemListener) {
        this.itemListener = itemListener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener{
        public TextView txtClassName;
        public TextView txtSubjectName;
        public TextView txtTakeAttendance;
        public MyViewHolder(@NonNull View itemView,onItemClickListener itemClickListener) {
            super(itemView);
            txtClassName = itemView.findViewById(R.id.viewClassName);
            txtSubjectName = itemView.findViewById(R.id.viewSubjectName);
            txtTakeAttendance = itemView.findViewById(R.id.txtTakeAttendance);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClickListener.onClick(getAdapterPosition());
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
    public ClassAdapter(Context context, ArrayList<ClassItems> runninClassesList) {
        this.runninClassesList = runninClassesList;
        this.context = context;

    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.class_view,parent,false);
        MyViewHolder myViewHolder= new MyViewHolder(view,itemListener);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.txtSubjectName.setText(runninClassesList.get(position).getClassName());
        holder.txtClassName.setText(runninClassesList.get(position).getSubjectName());
        holder.txtTakeAttendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemListener.onClick(holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return runninClassesList.size();
    }

}
