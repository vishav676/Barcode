package com.vishav.barcode.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vishav.barcode.Models.Event;
import com.vishav.barcode.Models.Ticket;
import com.vishav.barcode.Models.TicketList;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends database{
    public DatabaseHelper(Context context){
        super(context);

    }
    public void insertTicket(Ticket ticket ){
        if(!searchTicket(ticket.getTicketNumber())) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(ticketNumber, ticket.getTicketNumber());
            contentValues.put(ticketCustomerName, ticket.getCustomerName());
            contentValues.put(ticketInfo, ticket.getInfo());
            contentValues.put(ticketWarningNote, ticket.getWarningNote());
            contentValues.put(ticketUseable, ticket.getUseable());
            contentValues.put(ticketWarning, ticket.getWarning());
            contentValues.put(ticketListId, ticket.getTicketListId());
            db.insert(ticketTable, null, contentValues);
            db.close();
        }
    }

    public int insertTicketList(TicketList ticketList ){
        long ticketListId = -1;
        if(searchTicketList(ticketList.getTicketListName()) < 0) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(TicketListName, ticketList.getTicketListName());
            contentValues.put(TicketListCreated, ticketList.getCreatedAt());
            contentValues.put(TicketListUpdated, ticketList.getUpdatedAt());
            ticketListId = db.insert(TicketListTable, null, contentValues);
            db.close();
        }
        return (int)ticketListId;
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

    public int searchTicketList(String ticketListName){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from " + TicketListTable+ " where " + TicketListName +" like '" + ticketListName +"%'";
        int ticketListId;

        Cursor cursor = db.rawQuery(query,null);
        if (cursor.getCount()<=0){
            cursor.close();
            return -1;
        }
        ticketListId = cursor.getInt(cursor.getColumnIndex(TicketListPrimaryID));
        cursor.close();
        db.close();
        return ticketListId;
    }
    public String getEventInfo(String ticketNo){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select "+ checkingName + " from " + checkingTable + " inner join "  + ticketTable +" on " + checkingID +" = " + ticketListId +" where " + ticketNumber +" like '" + ticketNo +"%'";

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

    public List<TicketList> getAllTicketLists(){
        List<TicketList> events = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from " + TicketListTable;
        Cursor cursor = db.rawQuery(query,null);
        int count = cursor.getCount();
        while (cursor.moveToNext()){
            events.add(new TicketList
                    (
                        cursor.getString(cursor.getColumnIndex(TicketListPrimaryID)),
                        cursor.getString(cursor.getColumnIndex(TicketListName)),
                        cursor.getString(cursor.getColumnIndex(TicketListCreated)),
                        cursor.getString(cursor.getColumnIndex(TicketListUpdated))
                    ));
        }
        cursor.close();
        db.close();
        return events;
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
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from " + ticketTable + " inner join "  + checkingTable +" on " + checkingID +" = " + ticketListId +" where " + checkingID + " = " + id;

        Cursor cursor = db.rawQuery(query,null);

        List<Ticket> tickets = cursorToList(cursor);
        cursor.close();
        db.close();
        return tickets;
    }

    public List<Ticket> getTicketListById(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from " + ticketTable + " inner join "  + TicketListTable +" on " + TicketListPrimaryID +" = " + ticketListId +" where " + TicketListPrimaryID + " = " + id;

        Cursor cursor = db.rawQuery(query,null);

        List<Ticket> tickets = cursorToList(cursor);
        cursor.close();
        db.close();
        return tickets;
    }

    public List<Ticket> getAllTickets(){
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "Select * from " + ticketTable;
        Cursor cursor = db.rawQuery(query,null);
        List<Ticket> tickets = cursorToList(cursor);
        cursor.close();
        db.close();
        return tickets;

    }

    public List<Ticket> cursorToList(Cursor cursor){
        List<Ticket> tickets = new ArrayList<>();
        while (cursor.moveToNext()){
            tickets.add(new Ticket(cursor.getString(cursor.getColumnIndex(ticketNumber)),
                    cursor.getString(cursor.getColumnIndex(ticketCustomerName)),
                    cursor.getString(cursor.getColumnIndex(ticketInfo)),
                    cursor.getString(cursor.getColumnIndex(ticketWarningNote)),
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex(ticketUseable))),
                    cursor.getString(cursor.getColumnIndex(ticketWarning)),
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex(ticketListId)))));
        }
        return tickets;
    }
}
