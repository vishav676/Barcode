package com.vishav.barcode.Database.Repo;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.vishav.barcode.Database.AppDatabase;
import com.vishav.barcode.Database.Dao.CheckingTableDao;
import com.vishav.barcode.Database.Entities.CheckingTable;
import com.vishav.barcode.Database.Entities.TicketTable;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class CheckingTableRepo {
    private CheckingTableDao checkingTableDao;
    private LiveData<List<CheckingTable>> allEvents;

    public CheckingTableRepo(Application application)
    {
        AppDatabase db = AppDatabase.getDatabase(application);
        checkingTableDao = db.checkingTableDao();
        allEvents = checkingTableDao.getAll();
    }

    public LiveData<List<CheckingTable>> getAllEvents(){return allEvents;}

    public List<TicketTable> getEventTickets(long id){return checkingTableDao.getEventTickets(id);}

    public CheckingTable getEventInfo(String ticketNumber)
    {
        return checkingTableDao.getEventInfo(ticketNumber);
    }

    public long insert(CheckingTable checkingTable)
    {
        return checkingTableDao.insert(checkingTable);
    }
}
