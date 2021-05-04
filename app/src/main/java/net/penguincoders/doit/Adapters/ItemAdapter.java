package net.penguincoders.doit.Adapters;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.penguincoders.doit.AddNewTask;
import net.penguincoders.doit.MainActivity;
import net.penguincoders.doit.Model.Item;
import net.penguincoders.doit.Model.SubItem;
import net.penguincoders.doit.R;
import net.penguincoders.doit.RecyclerItemTouchHelper;
import net.penguincoders.doit.Utils.DatabaseHandler;

import java.util.Collections;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {

    private RecyclerView.RecycledViewPool viewPool = new RecyclerView.RecycledViewPool();
    private List<Item> itemList;
    private DatabaseHandler db;
    private MainActivity activity;
    SubItemAdapter subItemAdapter;


    public ItemAdapter(List<Item> itemList, DatabaseHandler db, MainActivity activity) {
        this.itemList = itemList;
        this.db = db;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.layout_item, viewGroup, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder itemViewHolder, final int i) {
        db.openDatabase();
        final Item item = itemList.get(i);
        itemViewHolder.tvItemTitle.setText(item.getItemTitle());

        // Create layout manager with initial prefetch item count
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                itemViewHolder.rvSubItem.getContext(),
                LinearLayoutManager.VERTICAL,
                false
        );
        layoutManager.setInitialPrefetchItemCount(item.getSubItemList().size());

        // Create sub item view adapter
        subItemAdapter = new SubItemAdapter(db, activity);

        itemViewHolder.rvSubItem.setLayoutManager(layoutManager);
        itemViewHolder.rvSubItem.setAdapter(subItemAdapter);
        itemViewHolder.rvSubItem.setRecycledViewPool(viewPool);

        //method recycle
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new RecyclerItemTouchHelper(subItemAdapter));
        itemTouchHelper.attachToRecyclerView( itemViewHolder.rvSubItem);

        List<SubItem> items = db.getAllTasks_g(item.getId());
        Collections.reverse(items);

        subItemAdapter.setTasks(items);


        //onclick
        itemViewHolder.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putInt("id", item.getId());
                AddNewTask fragment = new AddNewTask();
                fragment.setArguments(bundle);
                fragment.newInstance(activity);
                fragment.show(activity.getSupportFragmentManager(), AddNewTask.TAG);
            }
        });


    }


    public SubItemAdapter getSubItemAdapter(){
        return subItemAdapter;
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void setTasks(List<Item> todoList) {
        this.itemList = todoList;
        notifyDataSetChanged();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView tvItemTitle;
        private RecyclerView rvSubItem;
        FloatingActionButton fab;

        ItemViewHolder(View itemView) {
            super(itemView);
            tvItemTitle = itemView.findViewById(R.id.tv_item_title);
            rvSubItem = itemView.findViewById(R.id.rv_sub_item);
            fab = itemView.findViewById(R.id.fab);
        }
    }
}
