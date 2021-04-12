package com.vishav.barcode.Models;

import java.io.Serializable;

public class Event implements Serializable {
    private String name;
    private String time;
    private String date;
    private int ID;

    public Event(int id,String name, String time, String date) {
        this.name = name;
        this.time = time;
        this.date = date;
        this.ID = id;
    }
    public Event(String name, String time, String date) {
        this.name = name;
        this.time = time;
        this.date = date;
    }


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }
}
