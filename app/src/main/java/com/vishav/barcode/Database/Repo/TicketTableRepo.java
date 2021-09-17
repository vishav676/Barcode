package com.vishav.barcode.Database.Repo;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.vishav.barcode.Database.AppDatabase;
import com.vishav.barcode.Database.Dao.TicketTableDao;
import com.vishav.barcode.Database.Entities.TicketTable;

import java.util.List;

public class TicketTableRepo {
    private TicketTableDao ticketTableDao;
    private List<TicketTable> allTickets;

    public TicketTableRepo(Application application)
    {
        AppDatabase db = AppDatabase.getDatabase(application);
        ticketTableDao = db.ticketTableDao();
        allTickets = ticketTableDao.getAll();
    }

    public List<TicketTable> getAllTickets(){
        return allTickets;
    }

    public void updateTicketUseable(int ticketUseable, long id){
        AppDatabase.databaseWriteExecutor.execute(() ->
                ticketTableDao.updateTicketUseable(ticketUseable, id));
    }

    public TicketTable getOneTicket(String ticketNumber)
    {
        return ticketTableDao.searchTicket(ticketNumber);
    }

    public void insert(TicketTable ticketTable)
    {
        AppDatabase.databaseWriteExecutor.execute(() ->{
            ticketTableDao.insert(ticketTable);
        });
    }
}
