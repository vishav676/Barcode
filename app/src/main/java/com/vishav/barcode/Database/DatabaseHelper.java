package com.vishav.barcode.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.StringRes;

import com.vishav.barcode.Models.CheckingTicketList;
import com.vishav.barcode.Models.Event;
import com.vishav.barcode.Models.History;
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

    public void insertHistory(History history)
    {
        if(history != null)
        {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(scanningNote,history.getNote());
            contentValues.put(scanningIssue,history.getIssue());
            contentValues.put(scanningStatus,history.getStatus());
            contentValues.put(scanningTime,history.getTime());
            contentValues.put(scanningCheckingID,history.getCheckingID());
            contentValues.put(scanningCheckedManualy, history.getManualy());
            contentValues.put(scanningTimesUsed, history.getTimesUsed());
            contentValues.put(scanningTicketNumber, history.getTicketID());
            db.insert(scanningTable,null,contentValues);
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
        String query = "Select "+ checkingName + " from " + checkingTable + " inner join "  + CheckingTicketListTableRelationship +" on " + checkingID +" = " + CheckingListEventId +
                " inner join "  + TicketListTable +" on " + CheckingTicketListId +" = " + TicketListPrimaryID+
                " inner join "  + ticketTable +" on " + TicketListPrimaryID +" = " + ticketListId
                + " where " + ticketNumber +" like '" + ticketNo +"%'";

        Cursor cursor = db.rawQuery(query,null);
        if (cursor != null){
            cursor.moveToFirst();
            int count = cursor.getColumnCount();
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

    public List<Event> getEvents(){
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

    public List<History> getAllHistory()
    {
        List<History> histories = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String query = "Select * from " + scanningTable;
        Cursor cursor = db.rawQuery(query,null);
        cursor.moveToFirst();
        while(cursor.moveToNext())
        {
            histories.add(new History(
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex(scanningID))),
                    cursor.getString(cursor.getColumnIndex(scanningStatus)),
                    cursor.getString(cursor.getColumnIndex(scanningTime)),
                    cursor.getString(cursor.getColumnIndex(scanningIssue)),
                    cursor.getString(cursor.getColumnIndex(scanningNote)),
                    Boolean.parseBoolean(cursor.getString(cursor.getColumnIndex(scanningCheckedManualy))),
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex(scanningCheckingID))),
                    cursor.getString(cursor.getColumnIndex(scanningTicketNumber)),
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex(scanningTimesUsed))
                    )));
        }
        cursor.close();
        db.close();
        return histories;
    }


    public List<String> getAllCheckingNames()
    {
        List<String> checkings = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from " + checkingTable;
        Cursor cursor = db.rawQuery(query,null);
        int count = cursor.getCount();
        while (cursor.moveToNext()){
            checkings.add(cursor.getString(cursor.getColumnIndex(checkingName)));
        }
        cursor.close();
        db.close();
        return checkings;
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

    public int CheckingTicketListRelation(CheckingTicketList checkingTicketList){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(CheckingListEventId, checkingTicketList.getCheckingListId());
        contentValues.put(CheckingTicketListId, checkingTicketList.getTicketListId());
        long id = db.insert(CheckingTicketListTableRelationship, null, contentValues);
        db.close();
        return (int)id;
    }

    public int insertEvent(Event event){
        long id = -1;
        if(!searchTicket(event.getName())) {
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

    public Ticket getTicket(String num)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from " + ticketTable+ " where " + ticketNumber +" like '" + num +"%'";
        Ticket ticket = null;
        Cursor cursor = db.rawQuery(query,null);
        if (cursor.getCount()<=0){
            cursor.close();
            return ticket;
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

    public void updateTicketUseable(Ticket ticket)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        int ticketUseable = ticket.getUseable() - 1;
        if(ticketUseable < 0) ticketUseable = 0;
        String updateQuery = "Update " + ticketTable + " SET  Useable = " + ticketUseable + " where ticketNumber=" + "'"+ ticket.getTicketNumber()+ "'";
        db.execSQL(updateQuery);
        db.close();
    }

    public History getOneHistory(Ticket ticket)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from " + scanningNote + " where " + scanningTicketNumber +" like '" + ticket.getTicketNumber() +"%'";
        Cursor cursor = db.rawQuery(query,null);
        History history = null;
        if (cursor.getCount()<=0){
            cursor.close();
            return history;
        }
        cursor.moveToFirst();
        history = new History(cursor.getString(cursor.getColumnIndex(scanningStatus)),
                cursor.getString(cursor.getColumnIndex(scanningTime)),
                cursor.getString(cursor.getColumnIndex(scanningIssue)),
                cursor.getString(cursor.getColumnIndex(scanningNote)),
                Boolean.valueOf(cursor.getString(cursor.getColumnIndex(scanningCheckedManualy))),
                Integer.parseInt(cursor.getString(cursor.getColumnIndex(scanningCheckingID))),
                cursor.getString(cursor.getColumnIndex(scanningTicketNumber) )
        );
        cursor.close();
        db.close();
        return history;
    }


}
