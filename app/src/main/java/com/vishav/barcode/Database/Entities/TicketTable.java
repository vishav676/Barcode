package com.vishav.barcode.Database.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.vishav.barcode.Database.Entities.TicketListTable;

@Entity(tableName = "TicketTable",
foreignKeys = @ForeignKey(entity = TicketListTable.class,
parentColumns = "TicketListPrimaryId",
childColumns = "TicketListId"))
public class TicketTable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "TicketID")
    private long ticketId;

    @ColumnInfo(name = "TicketNumber")
    private String ticketNumber;

    @ColumnInfo(name = "TicketCustomerName")
    private String ticketCustomerName;

    @ColumnInfo(name = "TicketInfo")
    private String ticketInfo;

    @ColumnInfo(name = "TicketWarningNote")
    private String ticketWarningNote;

    @ColumnInfo(name = "TicketUseable")
    private int ticketUseable;

    @ColumnInfo(name = "TicketListId")
    private long ticketListId;

    @ColumnInfo(name = "TicketWarning")
    private String ticketWarning;

    public TicketTable(String ticketNumber, String ticketCustomerName, String ticketInfo, String ticketWarningNote, int ticketUseable, long ticketListId, String ticketWarning) {
        this.ticketNumber = ticketNumber;
        this.ticketCustomerName = ticketCustomerName;
        this.ticketInfo = ticketInfo;
        this.ticketWarningNote = ticketWarningNote;
        this.ticketUseable = ticketUseable;
        this.ticketListId = ticketListId;
        this.ticketWarning = ticketWarning;
    }

    public long getTicketId() {
        return ticketId;
    }

    public void setTicketId(long ticketId) {
        this.ticketId = ticketId;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public String getTicketCustomerName() {
        return ticketCustomerName;
    }

    public void setTicketCustomerName(String ticketCustomerName) {
        this.ticketCustomerName = ticketCustomerName;
    }

    public String getTicketInfo() {
        return ticketInfo;
    }

    public void setTicketInfo(String ticketInfo) {
        this.ticketInfo = ticketInfo;
    }

    public String getTicketWarningNote() {
        return ticketWarningNote;
    }

    public void setTicketWarningNote(String ticketWarningNote) {
        this.ticketWarningNote = ticketWarningNote;
    }

    public int getTicketUseable() {
        return ticketUseable;
    }

    public void setTicketUseable(int ticketUseable) {
        this.ticketUseable = ticketUseable;
    }

    public long getTicketListId() {
        return ticketListId;
    }

    public void setTicketListId(long ticketListId) {
        this.ticketListId = ticketListId;
    }

    public String getTicketWarning() {
        return ticketWarning;
    }

    public void setTicketWarning(String ticketWarning) {
        this.ticketWarning = ticketWarning;
    }
}
