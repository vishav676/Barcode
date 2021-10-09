package com.vishav.barcode.Database.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.sql.Date;

@Entity(tableName = "CheckingTable")
public class CheckingTable implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @NonNull
    @ColumnInfo(name = "CheckingID", index = true)
    private long checkingId;

    @ColumnInfo(name = "CheckingName")
    private String checkingName;

    @ColumnInfo(name = "CheckingTime")
    private String checkingTime;

    @ColumnInfo(name = "CheckingDate")
    private String checkingDate;

    @ColumnInfo(name = "CheckingCardListId")
    private long checkingCardListId;

    @ColumnInfo(name = "CheckingScanningId")
    private long checkingScanningId;

    public CheckingTable(String checkingName, String checkingTime, String checkingDate) {
        this.checkingName = checkingName;
        this.checkingTime = checkingTime;
        this.checkingDate = checkingDate;
    }

    public long getCheckingId() {
        return checkingId;
    }

    public String getCheckingName() {
        return checkingName;
    }

    public String getCheckingTime() {
        return checkingTime;
    }

    public String getCheckingDate() {
        return checkingDate;
    }

    public long getCheckingCardListId() {
        return checkingCardListId;
    }

    public long getCheckingScanningId() {
        return checkingScanningId;
    }

    public void setCheckingId(long checkingId) {
        this.checkingId = checkingId;
    }

    public void setCheckingName(String checkingName) {
        this.checkingName = checkingName;
    }

    public void setCheckingTime(String checkingTime) {
        this.checkingTime = checkingTime;
    }

    public void setCheckingDate(String checkingDate) {
        this.checkingDate = checkingDate;
    }

    public void setCheckingCardListId(long checkingCardListId) {
        this.checkingCardListId = checkingCardListId;
    }

    public void setCheckingScanningId(long checkingScanningId) {
        this.checkingScanningId = checkingScanningId;
    }
}


