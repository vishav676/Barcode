package com.vishav.barcode.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.vishav.barcode.Models.History;
import com.vishav.barcode.Models.Ticket;

import java.util.ArrayList;
import java.util.List;

public class HistoryRepo extends DatabaseHelper{
    public HistoryRepo(Context context) {
        super(context);
    }

    public History getOneHistory(Ticket ticket)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from " + scanningNote + " where " + scanningTicketNumber +" like '" + ticket.getTicketNumber() +"%'";
        Cursor cursor = db.rawQuery(query,null);
        History history;
        if (cursor.getCount()<=0){
            cursor.close();
            return null;
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
}
