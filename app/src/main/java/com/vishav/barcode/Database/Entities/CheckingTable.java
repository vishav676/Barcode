package com.vishav.barcode.Database.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.sql.Date;

@Entity(tableName = "CheckingTable")
public class CheckingTable implements Serializable {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    @ColumnInfo(name = "CheckingID", index = true)
    private long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @ColumnInfo(name = "CheckingName")
    @SerializedName("checkingName")
    private String checkingName;

    @ColumnInfo(name = "CheckingTime")
    @SerializedName("checkingTime")
    private String checkingTime;

    @ColumnInfo(name = "CheckingDate")
    @SerializedName("checkingDate")
    private String checkingDate;

    public CheckingTable(String checkingName, String checkingTime, String checkingDate) {
        this.checkingName = checkingName;
        this.checkingTime = checkingTime;
        this.checkingDate = checkingDate;
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

    public void setCheckingName(String checkingName) {
        this.checkingName = checkingName;
    }

    public void setCheckingTime(String checkingTime) {
        this.checkingTime = checkingTime;
    }

    public void setCheckingDate(String checkingDate) {
        this.checkingDate = checkingDate;
    }
}


