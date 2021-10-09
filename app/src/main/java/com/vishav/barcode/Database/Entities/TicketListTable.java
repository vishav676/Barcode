package com.vishav.barcode.Database.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Date;

@Entity(tableName = "TicketListTable")
public class TicketListTable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "TicketListPrimaryId", index = true)
    private long ticketListId;

    @ColumnInfo(name = "TicketListName")
    private String ticketListName;

    @ColumnInfo(name = "TicketListCreated")
    private String ticketListCreated;

    @ColumnInfo(name = "TicketListUpdated")
    private String ticketListUpdated;

    public TicketListTable(String ticketListName, String ticketListCreated, String ticketListUpdated) {
        this.ticketListName = ticketListName;
        this.ticketListCreated = ticketListCreated;
        this.ticketListUpdated = ticketListUpdated;
    }
    public TicketListTable(){}

    public long getTicketListId() {
        return ticketListId;
    }

    public void setTicketListId(long ticketListId) {
        this.ticketListId = ticketListId;
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
