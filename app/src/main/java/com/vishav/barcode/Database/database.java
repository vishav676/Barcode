package com.vishav.barcode.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.vishav.barcode.Models.Event;
import com.vishav.barcode.Models.Ticket;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class database extends SQLiteOpenHelper{

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
    static final String ticketEvent = "TicketType";
    static final String ticketWarning = "Warning";
    static final String ticketWarningNote = "WarningNote";
    static final String ticketUseable = "Useable";

    public database(Context context) {
        super(context, dbName, null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + checkingTable +
                " (" +checkingID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                checkingName + " TEXT," +
                checkingTime + " TIMESTAMP," +
                checkingDate + " DATE)");

        sqLiteDatabase.execSQL("CREATE TABLE " + scanningTable +
                " (" +scanningID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                scanningCheckedManualy + " BOOLEAN," +
                scanningNote + " TEXT," +
                scanningIssue + " TEXT," +
                scanningStatus + " BOOLEAN,"+
                scanningCheckingID + " INTEGER,"+
                scanningTime + " TIMESTAMP, FOREIGN KEY (" + scanningCheckingID +
                ") REFERENCES "+ checkingTable + " ("+ checkingID +"));");

        sqLiteDatabase.execSQL("CREATE TABLE " + ticketTable +
                " (" +ticketId + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ticketNumber + " TEXT," +
                ticketCustomerName + " TEXT," +
                ticketInfo + " TEXT," +
                ticketWarningNote + " TEXT," +
                ticketUseable + " INTEGER," +
                ticketEvent + " INTEGER, "+
                ticketWarning + " TEXT," +
                        " FOREIGN KEY ("+ ticketEvent +") REFERENCES "+ checkingTable + " ("+checkingID+"));"
                );

        sqLiteDatabase.execSQL("INSERT INTO " + checkingTable+ "(checkingName, checkingTime, checkingDate ) VALUES ('Boat Party', '6:00 PM', '2 Oct 2020')");
        sqLiteDatabase.execSQL("INSERT INTO " + checkingTable+ "(checkingName, checkingTime, checkingDate ) VALUES ('Welcome Party', '6:00 PM', '8 Sept 2020')");
        sqLiteDatabase.execSQL("INSERT INTO " + checkingTable+ "(checkingName, checkingTime, checkingDate ) VALUES ('Morison Party', '6:00 PM', '30 Sept 2020')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("Drop TABLE IF EXISTS "+ scanningTable);
        sqLiteDatabase.execSQL("Drop TABLE IF EXISTS " + checkingTable);
        sqLiteDatabase.execSQL("Drop TABLE IF EXISTS " + ticketTable);
        onCreate(sqLiteDatabase);
    }

}

