package com.vishav.barcode;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class dbHelper extends SQLiteOpenHelper{

    static final String dbName = "TicketDB";
    static final String checkingTable = "CheckingList";
    static final String checkingID = "CheckingID";
    static final String checkingName = "CheckingName";
    static final String checkingTime = "CheckingTime";
    static final String checkingDate = "CheckingDate";

    static final String scanningTable = "ScanningTable";
    static final String scanningID = "ScanningID";
    static final String scanningStatus = "ScanningStatus";
    static final String scanningTime = "ScanningTime";
    static final String scanningCheckedManualy = "ScanningManualy";
    static final String scanningIssue = "ScanningIssue";
    static final String scanningNote = "ScanningNote";
    static final String scanningCheckingID = "CheckingID";


    static final String ticketTable = "TicketTable";
    static final String ticketId = "ticketID";
    static final String ticketNumber = "ticketNumber";
    static final String ticketCustomerName = "CustomerName";
    static final String ticketInfo = "OrderInfo";
    static final String ticketWarning = "Warning";
    static final String ticketWarningNote = "WarningNote";
    static final String ticketUseable = "Useable";

    public dbHelper( Context context) {
        super(context, dbName, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + checkingTable +
                " (" +checkingID + "INTEGER PRIMARY KEY, " +
                checkingName + " TEXT," +
                checkingTime + " TIMESTAMP," +
                checkingDate + " DATE)");

        sqLiteDatabase.execSQL("CREATE TABLE " + scanningTable +
                " (" +scanningID + "INTEGER PRIMARY KEY, " +
                scanningCheckedManualy + "BOOLEAN" +
                scanningNote + " TEXT," +
                scanningIssue + " TEXT," +
                scanningStatus + " BOOLEAN,"+
                scanningTime + " TIMESTAMP, FOREIGN KEY (" + scanningCheckingID +
                ") REFERENCES "+ checkingTable + " ("+ checkingID +"));");

        sqLiteDatabase.execSQL("CREATE TABLE " + ticketTable +
                " (" +ticketId + "INTEGER PRIMARY KEY, " +
                ticketNumber + " TEXT," +
                ticketCustomerName + " TEXT," +
                ticketInfo + " TEXT," +
                ticketWarningNote + " TEXT," +
                ticketUseable + " INTEGER," +
                ticketWarning + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

