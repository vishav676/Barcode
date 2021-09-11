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
    static final String checkingCardListId = "CheckingCardListID";
    static final String checkingScanningId = "CheckingScanningID";

    static final String scanningTable = "ScanningTable";
    static final String scanningID = "ScanningID";
    static final String scanningStatus = "ScanningStatus";
    static final String scanningTime = "ScanningTime";
    static final String scanningCheckedManualy = "ScanningManualy";
    static final String scanningIssue = "ScanningIssue";
    static final String scanningNote = "ScanningNote";
    static final String scanningTimesUsed = "ScanningUsed";
    static final String scanningCheckingID = "CheckingID";
    static final String scanningTicketNumber = "TicketNumber";

    static final String ticketTable = "TicketTable";
    static final String ticketId = "ticketID";
    static final String ticketNumber = "ticketNumber";
    static final String ticketCustomerName = "CustomerName";
    static final String ticketInfo = "OrderInfo";
    static final String ticketListId = "TicketListID";
    static final String ticketWarning = "Warning";
    static final String ticketWarningNote = "WarningNote";
    static final String ticketUseable = "Useable";


    static final String ELBCardTable = "ELBCardTable";
    static final String ELBCardID = "ELBCardID";
    static final String ELBCardNumber = "ELBCardNumber";
    static final String ELBCardCustomerName = "ELBCardCustomerName";
    static final String ELBCardInfo = "ELBCardOrderInfo";
    static final String ELBCardListId = "ELBCardListID";
    static final String ELBCardWarning = "ELBCardWarning";
    static final String ELBCardWarningNote = "ELBCardWarningNote";


    static final String TicketListTable = "TicketListTable";
    static final String TicketListPrimaryID = "ID";
    static final String TicketListName = "Name";
    static final String TicketListCreated = "Created";
    static final String TicketListUpdated = "Updated";

    static final String ELBCardListTable = "ELBListTable";
    static final String ELBPrimaryID = "ID";
    static final String ELBListName = "Name";
    static final String ListCreated = "Created";
    static final String ListUpdated = "Updated";

    static final String CheckingTicketListTableRelationship = "CheckingTicketListRelationshipTable";
    static final String CheckingTicketListPrimaryId = "PrimaryKey";
    static final String CheckingTicketListId = "checkinngTicketListId";
    static final String CheckingListEventId = "checkingListId";


    public database(Context context) {
        super(context, dbName, null, 22);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE " + checkingTable +
                " (" +checkingID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                checkingName + " TEXT," +
                checkingTime + " TIMESTAMP," +
                checkingDate + " DATE,"+
                checkingCardListId + " INTEGER,"+
                checkingScanningId + " INTEGER," +
                " FOREIGN KEY ("+ checkingCardListId +") REFERENCES "+ ELBCardListTable + " ("+ELBPrimaryID+"));");

        sqLiteDatabase.execSQL("CREATE TABLE " + scanningTable +
                " (" +scanningID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                scanningCheckedManualy + " BOOLEAN," +
                scanningNote + " TEXT," +
                scanningIssue + " TEXT," +
                scanningStatus + " BOOLEAN,"+
                scanningTimesUsed + " INTEGER," +
                scanningTicketNumber + " TEXT," +
                scanningCheckingID + " INTEGER,"+
                scanningTime + " TIMESTAMP, FOREIGN KEY (" + scanningCheckingID +
                ") REFERENCES "+ checkingTable + " ("+ checkingID +"));");

        sqLiteDatabase.execSQL("CREATE TABLE " + TicketListTable +
                " (" + TicketListPrimaryID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                TicketListName + " TEXT,"+
                TicketListCreated + " DATETIME,"+
                TicketListUpdated + " DATETIME );"
                );

        sqLiteDatabase.execSQL("CREATE TABLE " + ELBCardListTable +
                " (" + ELBPrimaryID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                ELBListName + " TEXT,"+
                ListCreated + " DATETIME,"+
                ListUpdated + " DATETIME );"
        );

        sqLiteDatabase.execSQL("CREATE TABLE " + CheckingTicketListTableRelationship +
                " (" + CheckingTicketListPrimaryId + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                CheckingListEventId + " INTEGER,"+
                CheckingTicketListId +" INTEGER," +
                        " FOREIGN KEY ("+ CheckingListEventId +") REFERENCES "+ checkingTable + " ("+checkingID+")," +
                " FOREIGN KEY ("+ CheckingTicketListId +") REFERENCES "+ TicketListTable + " ("+TicketListPrimaryID+"));"
        );


        sqLiteDatabase.execSQL("CREATE TABLE " + ticketTable +
                " (" +ticketId + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ticketNumber + " TEXT," +
                ticketCustomerName + " TEXT," +
                ticketInfo + " TEXT," +
                ticketWarningNote + " TEXT," +
                ticketUseable + " INTEGER," +
                ticketListId + " INTEGER," +
                ticketWarning + " TEXT," +
                        " FOREIGN KEY ("+ ticketListId +") REFERENCES "+ TicketListTable + " ("+TicketListPrimaryID+"));"
                );

        sqLiteDatabase.execSQL("CREATE TABLE " + ELBCardTable +
                " (" +ELBCardID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ELBCardNumber + " TEXT," +
                ELBCardCustomerName + " TEXT," +
                ELBCardInfo + " TEXT," +
                ELBCardWarningNote + " TEXT," +
                ELBCardListId + " INTEGER," +
                ELBCardWarning + " TEXT," +
                " FOREIGN KEY ("+ ELBCardListId +") REFERENCES "+ ELBCardListTable + " ("+ELBPrimaryID+"));"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("Drop TABLE IF EXISTS "+ scanningTable);
        sqLiteDatabase.execSQL("Drop TABLE IF EXISTS " + checkingTable);
        sqLiteDatabase.execSQL("Drop TABLE IF EXISTS " + ticketTable);
        sqLiteDatabase.execSQL("Drop TABLE IF EXISTS " + TicketListTable);
        sqLiteDatabase.execSQL("Drop TABLE IF EXISTS " + ELBCardListTable);
        sqLiteDatabase.execSQL("Drop TABLE IF EXISTS " + ELBCardTable);
        sqLiteDatabase.execSQL("Drop TABLE IF EXISTS " + CheckingTicketListTableRelationship);
        onCreate(sqLiteDatabase);
    }

}

