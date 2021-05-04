package net.penguincoders.doit.Model;

public class SubItem {

    private int id, status, id_items;
    private String task;

    public SubItem(int id, int status, String task, int id_items) {
        this.id = id;
        this.status = status;
        this.task = task;
        this.id_items =id_items;
    }

    public SubItem() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public int getId_items() {
        return id_items;
    }

    public void setId_items(int id_items) {
        this.id_items = id_items;
    }
}
