package com.vishav.barcode.Database.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.vishav.barcode.Database.Entities.ELBCardListTable;

@Entity(tableName = "ELBCardTable",
foreignKeys = @ForeignKey(entity = ELBCardListTable.class,
parentColumns = "ELBPrimaryId",
childColumns = "ELBCardListId"))
public class ELBCardTable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ELBCardID")
    private long elbCardId;

    @ColumnInfo(name = "ELBCardNumber")
    private String elbCardNumber;

    @ColumnInfo(name = "ELBCardCustomerName")
    private String elbCardCustomerName;

    @ColumnInfo(name = "ELBCardInfo")
    private String elbCardInfo;

    @ColumnInfo(name = "ELBCardWarningNote")
    private String elbCardWarningNote;

    @ColumnInfo(name = "ELBCardListId")
    private long elbCardListId;

    @ColumnInfo(name = "ELBCardWarning")
    private String elbCardWarning;

    public long getElbCardId() {
        return elbCardId;
    }

    public void setElbCardId(long elbCardId) {
        this.elbCardId = elbCardId;
    }

    public String getElbCardNumber() {
        return elbCardNumber;
    }

    public void setElbCardNumber(String elbCardNumber) {
        this.elbCardNumber = elbCardNumber;
    }

    public String getElbCardCustomerName() {
        return elbCardCustomerName;
    }

    public void setElbCardCustomerName(String elbCardCustomerName) {
        this.elbCardCustomerName = elbCardCustomerName;
    }

    public String getElbCardInfo() {
        return elbCardInfo;
    }

    public void setElbCardInfo(String elbCardInfo) {
        this.elbCardInfo = elbCardInfo;
    }

    public String getElbCardWarningNote() {
        return elbCardWarningNote;
    }

    public void setElbCardWarningNote(String elbCardWarningNote) {
        this.elbCardWarningNote = elbCardWarningNote;
    }

    public long getElbCardListId() {
        return elbCardListId;
    }

    public void setElbCardListId(long elbCardListId) {
        this.elbCardListId = elbCardListId;
    }

    public String getElbCardWarning() {
        return elbCardWarning;
    }

    public void setElbCardWarning(String elbCardWarning) {
        this.elbCardWarning = elbCardWarning;
    }
}
