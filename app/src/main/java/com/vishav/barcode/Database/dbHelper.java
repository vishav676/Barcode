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
    static final String ticketEvent = "TicketType";
    static final String ticketWarning = "Warning";
    static final String ticketWarningNote = "WarningNote";
    static final String ticketUseable = "Useable";

    public dbHelper( Context context) {
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

    public void insertTicket(Ticket ticket ){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ticketNumber, ticket.getTicketNumber());
        contentValues.put(ticketCustomerName, ticket.getCustomerName());
        contentValues.put(ticketInfo, ticket.getInfo());
        contentValues.put(ticketWarningNote, ticket.getWarningNote());
        contentValues.put(ticketUseable, ticket.getUseable());
        contentValues.put(ticketWarning, ticket.getWarning());
        contentValues.put(ticketEvent, ticket.getTicketEvent());
        db.insert(ticketTable,null, contentValues);
        db.close();
    }
    public Boolean searchTicket(String checkTicketNumber){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from " + ticketTable+ " where " + ticketNumber +" like '" + checkTicketNumber +"%'";

        Cursor cursor = db.rawQuery(query,null);
        if (cursor.getCount()<=0){
            cursor.close();
            return false;
        }
        cursor.close();
        db.close();
        return true;
    }
    public String getEventInfo(String ticketNo){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select "+ checkingName + " from " + checkingTable + " inner join "  + ticketTable +" on " + checkingID +" = " + ticketEvent +" where " + ticketNumber +" like '" + ticketNo +"%'";

        Cursor cursor = db.rawQuery(query,null);
        if (cursor != null){
            cursor.moveToFirst();
        }
        assert cursor != null;
        String result = cursor.getString(cursor.getColumnIndex(checkingName));
        cursor.close();
        db.close();
        return result;
    }

    public List<Event> getEventName(){
        List<Event> events = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from " + checkingTable;
        Cursor cursor = db.rawQuery(query,null);
        int count = cursor.getCount();
        while (cursor.moveToNext()){
            events.add(new Event(Integer.parseInt(cursor.getString(cursor.getColumnIndex(checkingID))),
                    cursor.getString(cursor.getColumnIndex(checkingName)),
                    cursor.getString(cursor.getColumnIndex(checkingTime)),
                    cursor.getString(cursor.getColumnIndex(checkingDate))));
        }
        cursor.close();
        db.close();
        return events;
    }

    public List<Ticket> getEventTickets(int id){
        List<Ticket> tickets = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from " + ticketTable + " inner join "  + checkingTable +" on " + checkingID +" = " + ticketEvent +" where " + checkingID + " = " + id;

        Cursor cursor = db.rawQuery(query,null);

        int count = cursor.getCount();
        while (cursor.moveToNext()){
            tickets.add(new Ticket(cursor.getString(cursor.getColumnIndex(ticketNumber)),
                    cursor.getString(cursor.getColumnIndex(ticketCustomerName)),
                    cursor.getString(cursor.getColumnIndex(ticketInfo)),
                    cursor.getString(cursor.getColumnIndex(ticketWarningNote)),
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex(ticketUseable))),
                    cursor.getString(cursor.getColumnIndex(ticketWarning)),
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex(ticketEvent)))));
        }
        cursor.close();
        db.close();
        return tickets;
    }

}

