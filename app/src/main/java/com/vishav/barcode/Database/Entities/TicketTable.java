package com.vishav.barcode.Database.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
import com.vishav.barcode.Database.Entities.TicketListTable;

import java.util.Objects;

@Entity(tableName = "TicketTable",
foreignKeys = @ForeignKey(entity = TicketListTable.class,
parentColumns = "TicketListPrimaryId",
childColumns = "TicketListId"))
public class TicketTable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "TicketID", index = true)
    @SerializedName("id")
    private long id;

    @SerializedName("ticketNumber")
    @ColumnInfo(name = "TicketNumber")
    private String ticketNumber;

    @ColumnInfo(name = "TicketCustomerName")
    @SerializedName("ticketCustomerName")
    private String ticketCustomerName;

    @ColumnInfo(name = "TicketInfo")
    @SerializedName("ticketInfo")
    private String ticketInfo;

    @ColumnInfo(name = "TicketWarningNote")
    @SerializedName("ticketWarningNote")
    private String ticketWarningNote;

    @ColumnInfo(name = "TicketUseable")
    @SerializedName("ticketUseable")
    private int ticketUseable;

    @ColumnInfo(name = "TicketListId")
    @SerializedName("ticketListId")
    private long ticketListId;

    @ColumnInfo(name = "TicketWarning")
    @SerializedName("ticketWarning")
    private String ticketWarning;

    public TicketTable(String ticketNumber, String ticketCustomerName, String ticketInfo,
                       String ticketWarningNote, int ticketUseable, long ticketListId,
                       String ticketWarning) {
        this.ticketNumber = ticketNumber;
        this.ticketCustomerName = ticketCustomerName;
        this.ticketInfo = ticketInfo;
        this.ticketWarningNote = ticketWarningNote;
        this.ticketUseable = ticketUseable;
        this.ticketListId = ticketListId;
        this.ticketWarning = ticketWarning;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TicketTable that = (TicketTable) o;
        return ticketUseable == that.ticketUseable
                && ticketListId == that.ticketListId
                && ticketNumber.equals(that.ticketNumber)
                && ticketCustomerName.equals(that.ticketCustomerName)
                && ticketInfo.equals(that.ticketInfo)
                && ticketWarningNote.equals(that.ticketWarningNote)
                && ticketWarning.equals(that.ticketWarning);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ticketNumber, ticketCustomerName, ticketInfo,
                ticketWarningNote, ticketUseable, ticketListId,
                ticketWarning);
    }
}
