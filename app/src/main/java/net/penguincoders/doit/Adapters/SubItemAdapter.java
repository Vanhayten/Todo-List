package net.penguincoders.doit.Adapters;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.penguincoders.doit.AddNewTask;
import net.penguincoders.doit.MainActivity;
import net.penguincoders.doit.Model.SubItem;
import net.penguincoders.doit.R;
import net.penguincoders.doit.Utils.DatabaseHandler;

import java.util.List;

public class SubItemAdapter extends RecyclerView.Adapter<SubItemAdapter.SubItemViewHolder> {


    private List<SubItem> subItemList;
    private DatabaseHandler db;
    private MainActivity activity;

    public SubItemAdapter(DatabaseHandler db, MainActivity activity) {
        this.db = db;
        this.activity = activity;
    }

    @NonNull
    @Override
    public SubItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_sub_item, viewGroup, false);
        return new SubItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SubItemViewHolder subItemViewHolder, int i) {
        db.openDatabase();

        final SubItem subItem = subItemList.get(i);
        subItemViewHolder.tvSubItemTitle.setText(subItem.getTask());
        subItemViewHolder.tvSubItemTitle.setChecked(toBoolean(subItem.getStatus()));
        subItemViewHolder.tvSubItemTitle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    db.updateStatus(subItem.getId(), 1);
                    subItemViewHolder.tvSubItemTitle.setTextColor(getContext().getResources().getColor(R.color.dim_foreground_holo_dark));
                } else {
                    db.updateStatus(subItem.getId(), 0);
                    subItemViewHolder.tvSubItemTitle.setTextColor(getContext().getResources().getColor(R.color.colorAccent));
                }
            }
        });


        if(subItem.getStatus() !=0 ){
            subItemViewHolder.tvSubItemTitle.setTextColor(getContext().getResources().getColor(R.color.dim_foreground_holo_dark));
        }else{
            subItemViewHolder.tvSubItemTitle.setTextColor(getContext().getResources().getColor(R.color.colorAccent));
        }



    }

    private boolean toBoolean(int n) {
        return n != 0;
    }

    @Override
    public int getItemCount() {
        return subItemList.size();
    }

    public Context getContext() {
        return activity;
    }

    public void setTasks(List<SubItem> todoList) {
        this.subItemList = todoList;
        notifyDataSetChanged();
    }

    public void deleteItem(int position) {
        SubItem item = subItemList.get(position);
        db.deleteTask(item.getId());
        subItemList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position) {
        SubItem item = subItemList.get(position);
        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());
        AddNewTask fragment = new AddNewTask();
        fragment.setArguments(bundle);
        fragment.show(activity.getSupportFragmentManager(), AddNewTask.TAG);
    }


    class SubItemViewHolder extends RecyclerView.ViewHolder {
        CheckBox tvSubItemTitle;


        SubItemViewHolder(View itemView) {
            super(itemView);
            tvSubItemTitle = itemView.findViewById(R.id.todoCheckBox);
        }
    }
}
