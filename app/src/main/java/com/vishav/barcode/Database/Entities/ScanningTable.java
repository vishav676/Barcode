package com.vishav.barcode.Database.Entities;

import static androidx.room.ForeignKey.CASCADE;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;
import com.vishav.barcode.Database.Entities.CheckingTable;

import java.sql.Date;

@Entity(tableName = "ScanningTable",
foreignKeys = @ForeignKey(entity = CheckingTable.class,
parentColumns = "CheckingID",
childColumns = "ScanningCheckingID",
onDelete = CASCADE))
public class ScanningTable {

    @PrimaryKey(autoGenerate = true)
    @SerializedName("id")
    @ColumnInfo(name = "ScanningID", index = true)
    private long id;

    @ColumnInfo(name = "ScanningStatus")
    @SerializedName("scanningStatus")
    private String scanningStatus;

    @ColumnInfo(name = "scanningTime")
    @SerializedName("scanningTime")
    private String scanningTime;

    @ColumnInfo(name = "ScanningCheckedManually")
    @SerializedName("scanningCheckedMannualy")
    private Boolean scanningCheckedManually;

    @ColumnInfo(name = "ScanningIssue")
    @SerializedName("scanningIssue")
    private String scanningIssue;

    @ColumnInfo(name = "ScanningNote")
    @SerializedName("scanningNote")
    private String scanningNote;

    @ColumnInfo(name = "ScanningTimesUsed")
    @SerializedName("scanningTimesUsed")
    private int scanningTimesUsed;

    @ColumnInfo(name = "ScanningCheckingID")
    @SerializedName("scanningCheckingId")
    private long scanningCheckingID;

    @ColumnInfo(name = "ScanningTicketNumber")
    @SerializedName("scanningTicketNumber")
    private String scanningTicketNumber;

    public ScanningTable(String scanningStatus, String scanningTime, Boolean scanningCheckedManually,
                         String scanningIssue, String scanningNote, int scanningTimesUsed,
                         long scanningCheckingID, String scanningTicketNumber) {
        this.scanningStatus = scanningStatus;
        this.scanningTime = scanningTime;
        this.scanningCheckedManually = scanningCheckedManually;
        this.scanningIssue = scanningIssue;
        this.scanningNote = scanningNote;
        this.scanningTimesUsed = scanningTimesUsed;
        this.scanningCheckingID = scanningCheckingID;
        this.scanningTicketNumber = scanningTicketNumber;
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

    public Boolean getScanningCheckedManually() {
        return scanningCheckedManually;
    }

    public void setScanningCheckedManually(Boolean scanningCheckedMannualy) {
        this.scanningCheckedManually = scanningCheckedMannualy;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
