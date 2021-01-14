package com.vishav.barcode.Models;

public class Event {
    private int id;
    private String name;
    private String time;
    private String date;

    public Event(int id, String name, String time, String date) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.date = date;
    }

    public int getId() {
        return id;
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
