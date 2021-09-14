package com.vishav.barcode.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vishav.barcode.Models.Ticket;

import java.util.List;

public class TicketRepo extends DatabaseHelper  {

    public TicketRepo(Context context) {
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
    public List<Ticket> getAllTickets(){
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "Select * from " + ticketTable;
        Cursor cursor = db.rawQuery(query,null);
        List<Ticket> tickets = cursorToList(cursor);
        cursor.close();
        db.close();
        return tickets;
    }

    public void updateTicketUseable(Ticket ticket)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        int ticketUseable = ticket.getUseable() - 1;
        if(ticketUseable < 0) ticketUseable = 0;
        String updateQuery = "Update " + ticketTable + " SET  Useable = " + ticketUseable + " where ticketNumber=" + "'"+ ticket.getTicketNumber()+ "'";
        db.execSQL(updateQuery);
        db.close();
    }

    public Ticket getTicket(String num)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from " + ticketTable+ " where " + ticketNumber +" like '" + num +"%'";
        Ticket ticket;
        Cursor cursor = db.rawQuery(query,null);
        if (cursor.getCount()<=0){
            cursor.close();
            return null;
        }
        cursor.moveToFirst();
        ticket = new Ticket(cursor.getString(cursor.getColumnIndex(ticketNumber)),
                cursor.getString(cursor.getColumnIndex(ticketCustomerName)),
                cursor.getString(cursor.getColumnIndex(ticketInfo)),
                cursor.getString(cursor.getColumnIndex(ticketWarningNote)),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(ticketUseable))),
                cursor.getString(cursor.getColumnIndex(ticketWarning))
        );
        cursor.close();
        db.close();
        return ticket;
    }
}
