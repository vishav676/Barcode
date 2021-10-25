package com.vishav.barcode.Database.Repo;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vishav.barcode.DataService;
import com.vishav.barcode.Database.AppDatabase;
import com.vishav.barcode.Database.Dao.TicketTableDao;
import com.vishav.barcode.Database.Entities.TicketListTable;
import com.vishav.barcode.Database.Entities.TicketTable;
import com.vishav.barcode.RetrofitConnection;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TicketTableRepo {
    private TicketTableDao ticketTableDao;
    private List<TicketTable> allTickets;
    private DataService dataService;
    private MutableLiveData<List<TicketTable>> allTicketsFromApi = new MutableLiveData<>();


    public TicketTableRepo(Application application)
    {
        AppDatabase db = AppDatabase.getDatabase(application);
        ticketTableDao = db.ticketTableDao();
        allTickets = ticketTableDao.getAll();
        dataService = RetrofitConnection.getRetroFitInstance().create(DataService.class);
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
        ticketTableDao.insert(ticketTable);
    }

    public TicketTable getTicketInfo(String ticketNumber, String eventName)
    {
        return ticketTableDao.getTicketInfo(ticketNumber,eventName);
    }

    public void insert(List<TicketTable> ticketTables)
    {
        ticketTableDao.insert(ticketTables);
    }

}
