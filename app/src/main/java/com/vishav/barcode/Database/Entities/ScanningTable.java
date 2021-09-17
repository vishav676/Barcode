package com.vishav.barcode.Database.Entities;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.vishav.barcode.Database.Entities.CheckingTable;

import java.sql.Date;

@Entity(tableName = "ScanningTable",
foreignKeys = @ForeignKey(entity = CheckingTable.class,
parentColumns = "CheckingID",
childColumns = "ScanningCheckingID",
onDelete = CASCADE))
public class ScanningTable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "ScanningID")
    private long scanningID;

    @ColumnInfo(name = "ScanningStatus")
    private String scanningStatus;

    @ColumnInfo(name = "scanningTime")
    private String scanningTime;

    @ColumnInfo(name = "ScanningCheckedManually")
    private Boolean scanningCheckedManually;

    @ColumnInfo(name = "ScanningIssue")
    private String scanningIssue;

    @ColumnInfo(name = "ScanningNote")
    private String scanningNote;

    @ColumnInfo(name = "ScanningTimesUsed")
    private int scanningTimesUsed;

    @ColumnInfo(name = "ScanningCheckingID")
    private long scanningCheckingID;

    @ColumnInfo(name = "ScanningTicketNumber")
    private String scanningTicketNumber;

    public ScanningTable(String scanningStatus, String scanningTime, Boolean scanningCheckedManually, String scanningIssue, String scanningNote, int scanningTimesUsed, long scanningCheckingID, String scanningTicketNumber) {
        this.scanningStatus = scanningStatus;
        this.scanningTime = scanningTime;
        this.scanningCheckedManually = scanningCheckedManually;
        this.scanningIssue = scanningIssue;
        this.scanningNote = scanningNote;
        this.scanningTimesUsed = scanningTimesUsed;
        this.scanningCheckingID = scanningCheckingID;
        this.scanningTicketNumber = scanningTicketNumber;
    }

    public long getScanningID() {
        return scanningID;
    }

    public void setScanningID(long scanningID) {
        this.scanningID = scanningID;
    }

    public String getScanningStatus() {
        return scanningStatus;
    }

    public void setScanningStatus(String scanningStatus) {
        this.scanningStatus = scanningStatus;
    }

    public String getScanningTime() {
        return scanningTime;
    }

    public void setScanningTime(String scanningTime) {
        this.scanningTime = scanningTime;
    }

    public Boolean getScanningCheckedManually() {
        return scanningCheckedManually;
    }

    public void setScanningCheckedManually(Boolean scanningCheckedManually) {
        this.scanningCheckedManually = scanningCheckedManually;
    }

    public String getScanningIssue() {
        return scanningIssue;
    }

    public void setScanningIssue(String scanningIssue) {
        this.scanningIssue = scanningIssue;
    }

    public String getScanningNote() {
        return scanningNote;
    }

    public void setScanningNote(String scanningNote) {
        this.scanningNote = scanningNote;
    }

    public int getScanningTimesUsed() {
        return scanningTimesUsed;
    }

    public void setScanningTimesUsed(int scanningTimesUsed) {
        this.scanningTimesUsed = scanningTimesUsed;
    }

    public long getScanningCheckingID() {
        return scanningCheckingID;
    }

    public void setScanningCheckingID(long scanningCheckingID) {
        this.scanningCheckingID = scanningCheckingID;
    }

    public String getScanningTicketNumber() {
        return scanningTicketNumber;
    }

    public void setScanningTicketNumber(String scanningTicketNumber) {
        this.scanningTicketNumber = scanningTicketNumber;
    }
}
