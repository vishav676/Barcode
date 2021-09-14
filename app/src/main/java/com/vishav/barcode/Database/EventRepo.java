package com.vishav.barcode.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vishav.barcode.Models.Event;
import com.vishav.barcode.Models.Ticket;

import java.util.ArrayList;
import java.util.List;

public class EventRepo extends  DatabaseHelper{
    public EventRepo(Context context) {
        super(context);
    }
    public List<Event> getEvents(){
        List<Event> events = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from " + checkingTable;
        Cursor cursor = db.rawQuery(query,null);
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
        String query = "Select * from " + ticketTable + " inner join "  + TicketListTable +" on " + ticketListId +" = " + TicketListPrimaryID +
                " inner join " + CheckingTicketListTableRelationship + " on " + TicketListPrimaryID + " = " + CheckingTicketListId +
                " inner join " + checkingTable + " on " + CheckingListEventId + " = " + checkingID +
                " where " + checkingID + " = " + id;

        Cursor cursor = db.rawQuery(query,null);

        List<Ticket> tickets = cursorToList(cursor);
        cursor.close();
        db.close();
        return tickets;
    }

    public String getEventInfo(String ticketNo){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select "+ checkingName + " from " + checkingTable + " inner join "  + CheckingTicketListTableRelationship +" on " + checkingID +" = " + CheckingListEventId +
                " inner join "  + TicketListTable +" on " + CheckingTicketListId +" = " + TicketListPrimaryID+
                " inner join "  + ticketTable +" on " + TicketListPrimaryID +" = " + ticketListId
                + " where " + ticketNumber +" like '" + ticketNo +"%'";

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

    public int insertEvent(Event event){
        long id = -1;
        if(!searchEvent(event.getName())) {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(checkingTime, event.getTime());
            contentValues.put(checkingName, event.getName());
            contentValues.put(checkingDate, event.getDate());
            id = db.insert(checkingTable, null, contentValues);
            db.close();
        }
        return (int)id;
    }

    public Boolean searchEvent(String eventName){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from " + checkingTable+ " where " + checkingName +" like '" + checkingTable +"%'";

        Cursor cursor = db.rawQuery(query,null);
        if (cursor.getCount()<=0){
            cursor.close();
            return false;
        }
        cursor.close();
        db.close();
        return true;
    }
}
