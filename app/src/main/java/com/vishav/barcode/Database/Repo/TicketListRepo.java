package com.vishav.barcode.Database.Repo;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.vishav.barcode.Database.AppDatabase;
import com.vishav.barcode.Database.Dao.TicketListDao;
import com.vishav.barcode.Database.Entities.TicketListTable;
import com.vishav.barcode.Database.Entities.TicketTable;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

import io.reactivex.rxjava3.core.Observable;

public class TicketListRepo {
    private TicketListDao ticketListDao;
    private LiveData<List<TicketListTable>> allTicketList;
    private LiveData<List<String>> allTicketListNames;

    public TicketListRepo(Application application)
    {
        AppDatabase db = AppDatabase.getDatabase(application);
        ticketListDao = db.ticketListDao();
        allTicketList = ticketListDao.getAll();
        allTicketListNames = ticketListDao.getTicketListNames();
    }

    public LiveData<List<TicketListTable>> getAllTicketList(){return allTicketList;}

    public LiveData<List<String>> getAllTicketListNames(){return allTicketListNames;}

    public List<TicketTable> getAllTicketsFromListID(long id)
    {
        return ticketListDao.getAllTicketsFromListID(id);
    }

    public long insert(TicketListTable ticketListTable)
    {
        return ticketListDao.insert(ticketListTable);
        /*AppDatabase.databaseWriteExecutor.execute(() -> {
            ticketListDao.insert(ticketListTable);
        });*/
    }
}
