package com.vishav.barcode.Database.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Date;

@Entity(tableName = "ELBCardListTable")
public class ELBCardListTable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ELBPrimaryId")
    private long elbPrimaryId;

    @ColumnInfo(name = "ELBListName")
    private String elbListName;

    @ColumnInfo(name = "ListCreated")
    private String listCreated;

    @ColumnInfo(name = "ListUpdated")
    private String listUpdated;

    public long getElbPrimaryId() {
        return elbPrimaryId;
    }

    public void setElbPrimaryId(long elbPrimaryId) {
        this.elbPrimaryId = elbPrimaryId;
    }

    public String getElbListName() {
        return elbListName;
    }

    public void setElbListName(String elbListName) {
        this.elbListName = elbListName;
    }

    public String getListCreated() {
        return listCreated;
    }

    public void setListCreated(String listCreated) {
        this.listCreated = listCreated;
    }

    public String getListUpdated() {
        return listUpdated;
    }

    public void setListUpdated(String listUpdated) {
        this.listUpdated = listUpdated;
    }
}
