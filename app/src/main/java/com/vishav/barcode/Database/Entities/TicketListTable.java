package com.vishav.barcode.Database.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.sql.Date;

@Entity(tableName = "TicketListTable")
public class TicketListTable {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    @ColumnInfo(name = "TicketListPrimaryId", index = true)
    private long id;

    @ColumnInfo(name = "TicketListName")
    @SerializedName("ticketListName")
    private String ticketListName;

    @ColumnInfo(name = "TicketListCreated")
    @SerializedName("ticketListCreated")
    private String ticketListCreated;

    @ColumnInfo(name = "TicketListUpdated")
    @SerializedName("ticketListUpdates")
    private String ticketListUpdated;

    public TicketListTable(long id,String ticketListName, String ticketListCreated, String ticketListUpdated) {
        this.id = id;
        this.ticketListName = ticketListName;
        this.ticketListCreated = ticketListCreated;
        this.ticketListUpdated = ticketListUpdated;
    }

    public TicketListTable(String ticketListName, String ticketListCreated, String ticketListUpdated) {
        this.ticketListName = ticketListName;
        this.ticketListCreated = ticketListCreated;
        this.ticketListUpdated = ticketListUpdated;
    }
    public TicketListTable(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTicketListName() {
        return ticketListName;
    }

    public void setTicketListName(String ticketListName) {
        this.ticketListName = ticketListName;
    }

    public String getTicketListCreated() {
        return ticketListCreated;
    }

    public void setTicketListCreated(String ticketListCreated) {
        this.ticketListCreated = ticketListCreated;
    }

    public String getTicketListUpdated() {
        return ticketListUpdated;
    }

    public void setTicketListUpdated(String ticketListUpdated) {
        this.ticketListUpdated = ticketListUpdated;
    }
}
