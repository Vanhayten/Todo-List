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
import net.penguincoders.doit.historique;

import java.util.List;

public class HistoriqueItemAdapter extends RecyclerView.Adapter<HistoriqueItemAdapter.SubItemViewHolder> {


    private List<SubItem> subItemList;
    private DatabaseHandler db;
    private historique activity;

    public HistoriqueItemAdapter( DatabaseHandler db, historique activity) {
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

        final SubItem item = subItemList.get(i);
        subItemViewHolder.tvSubItemTitle.setText(item.getTask());
        subItemViewHolder.tvSubItemTitle.setChecked(toBoolean(item.getStatus()));
        subItemViewHolder.tvSubItemTitle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    db.updateStatus(item.getId(), 1);
                } else {
                    db.updateStatus(item.getId(), 0);
                }
            }
        });
    }

    private boolean toBoolean(int n) {
        return n != 0;
    }

    @Override
    public int getItemCount() {
        return subItemList.size();
    }

    public void setTasks(List<SubItem> todoList) {
        this.subItemList = todoList;
        notifyDataSetChanged();
    }


    class SubItemViewHolder extends RecyclerView.ViewHolder {
        CheckBox tvSubItemTitle;

        SubItemViewHolder(View itemView) {
            super(itemView);
            tvSubItemTitle = itemView.findViewById(R.id.todoCheckBox);
        }
    }
}
