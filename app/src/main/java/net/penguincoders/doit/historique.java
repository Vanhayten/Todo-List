package net.penguincoders.doit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import net.penguincoders.doit.Adapters.HistoriqueItemAdapter;
import net.penguincoders.doit.Adapters.SubItemAdapter;
import net.penguincoders.doit.Model.SubItem;
import net.penguincoders.doit.Utils.DatabaseHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class historique extends AppCompatActivity {

    private DatabaseHandler db;
    private RecyclerView tasksRecyclerView;
    private HistoriqueItemAdapter tasksAdapter;
    private List<SubItem> taskList;
    AppCompatImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historique);
        Objects.requireNonNull(getSupportActionBar()).hide();

        db = new DatabaseHandler(this);
        db.openDatabase();


        tasksRecyclerView = findViewById(R.id.tasksRecyclerHistorique);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksAdapter = new HistoriqueItemAdapter(db,historique.this);
        tasksRecyclerView.setAdapter(tasksAdapter);


        taskList = db.getAllHistorique();
        Collections.reverse(taskList);

        tasksAdapter.setTasks(taskList);


        imageView = findViewById(R.id.history);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(historique.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}