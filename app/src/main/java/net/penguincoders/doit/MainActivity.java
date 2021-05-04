package net.penguincoders.doit;


import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import net.penguincoders.doit.Model.Item;
import net.penguincoders.doit.Adapters.ItemAdapter;
import net.penguincoders.doit.Model.SubItem;
import net.penguincoders.doit.Utils.DatabaseHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static android.provider.AlarmClock.EXTRA_MESSAGE;

public class MainActivity extends AppCompatActivity{

     DatabaseHandler db;

    private RecyclerView rvItem;

    FloatingActionButton fab;


    List<SubItem> SybitemList;

    List<Item> itemList;

    AppCompatImageView imageView;

    ItemAdapter itemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();


        SybitemList = new ArrayList<>();
        itemList = new ArrayList<>();

        db = new DatabaseHandler(this);

        db.openDatabase();
        itemList = db.buildItemList(this);

        RecyclerView rvItem = findViewById(R.id.rv_item);
        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
         itemAdapter = new ItemAdapter(itemList, db, this);
        rvItem.setAdapter(itemAdapter);
        rvItem.setLayoutManager(layoutManager);


        //itemList = db.buildItemList();
        //Collections.reverse(itemList);
        //itemAdapter.setTasks(itemList);

        fab = findViewById(R.id.fab1);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("hist", "hist");
                AddNewItem AddNewItem = new AddNewItem();
                AddNewItem.setArguments(bundle);
                AddNewItem.newInstance(MainActivity.this);
                AddNewItem.show(getSupportFragmentManager(), AddNewTask.TAG);
            }
        });

        imageView = findViewById(R.id.history);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, historique.class);
                startActivity(intent);
                finish();
            }
        });

    }

    @Override
    public void onResume(){
        super.onResume();
        db.openDatabase();
        List<Item> itemList = new ArrayList<>();
        itemList = db.buildItemList(this);
        itemAdapter.setTasks(itemList);
        itemAdapter.notifyDataSetChanged();
    }




}