package net.penguincoders.doit.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import net.penguincoders.doit.MainActivity;
import net.penguincoders.doit.Model.Item;
import net.penguincoders.doit.Model.SubItem;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int VERSION = 2;
    private static final String NAME = "toDoListDatabase";

    private static final String group_TABLE = "items";
    private static final String ID1 = "id";
    private static final String itemTitle = "itemTitle";
    private static final String CREATE_items_TABLE = "CREATE TABLE " + group_TABLE + "(" + ID1 + " INTEGER PRIMARY KEY AUTOINCREMENT, " + itemTitle + " TEXT )";


    private static final String TODO_TABLE = "todo";
    private static final String ID = "id";
    private static final String TASK = "task";
    private static final String STATUS = "status";
    private static final String id_items = "id_items";
    private static final String CREATE_TODO_TABLE = "CREATE TABLE " + TODO_TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + TASK + " TEXT, "
            + STATUS + " INTEGER, "+id_items+" INTEGER, FOREIGN KEY("+id_items+") REFERENCES "+group_TABLE+"("+ID1+") )";



    private SQLiteDatabase db;

    public DatabaseHandler(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_items_TABLE);
        db.execSQL(CREATE_TODO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TODO_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + group_TABLE);
        // Create tables again
        onCreate(db);
    }

    public void openDatabase() {

        db = this.getWritableDatabase();
    }

    public void insertTask(SubItem task){
        ContentValues cv = new ContentValues();
        cv.put(id_items, task.getId_items());
        cv.put(TASK, task.getTask());
        cv.put(STATUS, 0);
        db.insert(TODO_TABLE, null, cv);
    }

    public void insertItem(String task){
        ContentValues cv = new ContentValues();
        cv.put(itemTitle, task);
        db.insert(group_TABLE, null, cv);
    }

    public List<SubItem> getAllTasks(){
        List<SubItem> taskList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try{
            cur = db.query(TODO_TABLE, null, null, null, null, null, null, null);
            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        SubItem task = new SubItem();
                        task.setId(cur.getInt(cur.getColumnIndex(ID)));
                        task.setTask(cur.getString(cur.getColumnIndex(TASK)));
                        task.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                        taskList.add(task);
                    }
                    while(cur.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            assert cur != null;
            cur.close();
        }
        return taskList;
    }

    public List<SubItem> getAllHistorique(){
        List<SubItem> taskList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try{
            cur = db.query(TODO_TABLE, null, STATUS + " = '1'", null, null, null, null, String.valueOf(10));
            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        SubItem task = new SubItem();
                        task.setId(cur.getInt(cur.getColumnIndex(ID)));
                        task.setTask(cur.getString(cur.getColumnIndex(TASK)));
                        task.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                        taskList.add(task);
                    }
                    while(cur.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            assert cur != null;
            cur.close();
        }
        return taskList;
    }

    public List<SubItem> getAllTasks_g(int pose){
        List<SubItem> taskList = new ArrayList<>();
        Cursor cur = null;
        db.beginTransaction();
        try{
            cur = db.query(TODO_TABLE, null, id_items + " = '" + pose + "'", null, null, null, null, null);
            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        SubItem task = new SubItem();
                        task.setId(cur.getInt(cur.getColumnIndex(ID)));
                        task.setTask(cur.getString(cur.getColumnIndex(TASK)));
                        task.setStatus(cur.getInt(cur.getColumnIndex(STATUS)));
                        taskList.add(task);
                    }
                    while(cur.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            assert cur != null;
            cur.close();
        }
        return taskList;
    }

    public List<Item> buildItemList(MainActivity main) {
        List<Item> itemList = new ArrayList<>();

        db.beginTransaction();
        Cursor cursor = null;

        try {
            cursor = db.query(group_TABLE, null, null, null, null, null, null, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    //--------------------------------get tasks data
                    Item item = new Item(cursor.getInt(0), cursor.getString(1), getAllTasks_g(cursor.getInt(0)));
                    itemList.add(item);
                } while (cursor.moveToNext());


            }
        }
        finally {
            db.endTransaction();
            assert cursor != null;
            cursor.close();
        }
        return itemList;
    }

    public void updateStatus(int id, int status){
        ContentValues cv = new ContentValues();
        cv.put(STATUS, status);
        db.update(TODO_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void updateTask(int id, String task) {
        ContentValues cv = new ContentValues();
        cv.put(TASK, task);
        db.update(TODO_TABLE, cv, ID + "= ?", new String[] {String.valueOf(id)});
    }

    public void deleteTask(int id){
        db.delete(TODO_TABLE, ID + "= ?", new String[] {String.valueOf(id)});
    }
}
