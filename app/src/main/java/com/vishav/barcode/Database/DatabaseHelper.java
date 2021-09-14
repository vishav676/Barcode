package com.vishav.barcode.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vishav.barcode.Models.CheckingTicketList;
import com.vishav.barcode.Models.Ticket;
import com.vishav.barcode.Models.TicketList;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends database{
    public DatabaseHelper(Context context){
        super(context);

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

    public List<TicketList> getAllTicketLists(){
        List<TicketList> events = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from " + TicketListTable;
        Cursor cursor = db.rawQuery(query,null);
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




    public List<Ticket> getTicketListById(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from " + ticketTable + " inner join "  + TicketListTable +" on " + TicketListPrimaryID +" = " + ticketListId +" where " + TicketListPrimaryID + " = " + id;

        Cursor cursor = db.rawQuery(query,null);

        List<Ticket> tickets = cursorToList(cursor);
        cursor.close();
        db.close();
        return tickets;
    }


/*    public List<String> getAllCheckingNames()
    {
        List<String> checking = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from " + checkingTable;
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            checking.add(cursor.getString(cursor.getColumnIndex(checkingName)));
        }
        cursor.close();
        db.close();
        return checking;
    }*/

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

    public int CheckingTicketListRelation(CheckingTicketList checkingTicketList){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CheckingListEventId, checkingTicketList.getCheckingListId());
        contentValues.put(CheckingTicketListId, checkingTicketList.getTicketListId());
        long id = db.insert(CheckingTicketListTableRelationship, null, contentValues);
        db.close();
        return (int)id;
    }
}
