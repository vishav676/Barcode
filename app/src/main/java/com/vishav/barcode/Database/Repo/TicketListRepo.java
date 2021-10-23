package com.vishav.barcode.Database.Repo;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vishav.barcode.DataService;
import com.vishav.barcode.Database.AppDatabase;
import com.vishav.barcode.Database.Dao.TicketListDao;
import com.vishav.barcode.Database.Entities.TicketListTable;
import com.vishav.barcode.Database.Entities.TicketTable;
import com.vishav.barcode.RetrofitConnection;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TicketListRepo {
    private TicketListDao ticketListDao;
    private LiveData<List<TicketListTable>> allTicketList;
    private LiveData<List<String>> allTicketListNames;
    private DataService dataService;
    private Application application;

    public TicketListRepo(Application application)
    {
        this.application = application;
        AppDatabase db = AppDatabase.getDatabase(application);
        ticketListDao = db.ticketListDao();
        allTicketList = ticketListDao.getAll();
        dataService = RetrofitConnection.getRetroFitInstance().create(DataService.class);
        allTicketListNames = ticketListDao.getTicketListNames();
    }

    public LiveData<List<TicketListTable>> getAllTicketList(){return allTicketList;}


    public List<TicketTable> getAllTicketsFromListID(long id)
    {
        return ticketListDao.getAllTicketsFromListID(id);
    }

    public long insert(TicketListTable ticketListTable)
    {
        return ticketListDao.insert(ticketListTable);
    }


    public void insert(List<TicketListTable> ticketListTables)
    {
        ticketListDao.insert(ticketListTables);
    }

}
