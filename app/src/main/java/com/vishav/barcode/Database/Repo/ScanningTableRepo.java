package com.vishav.barcode.Database.Repo;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.vishav.barcode.Database.AppDatabase;
import com.vishav.barcode.Database.Dao.ScanningTableDao;
import com.vishav.barcode.Database.Entities.ScanningTable;
import com.vishav.barcode.Database.Entities.TicketListTable;

import java.util.List;

public class ScanningTableRepo {
    private ScanningTableDao scanningTableDao;
    private LiveData<List<ScanningTable>> allHistory;

    public ScanningTableRepo(Application application)
    {
        AppDatabase db = AppDatabase.getDatabase(application);
        scanningTableDao = db.scanningTableDao();
        allHistory = scanningTableDao.getAllHistory();
    }
    public LiveData<List<ScanningTable>> getAllHistory(){
        return allHistory;
    }

    public ScanningTable getOneHistory(String ticketNumber)
    {
        return scanningTableDao.getHistory(ticketNumber);
    }

    public void insert(ScanningTable scanningTable)
    {
        AppDatabase.databaseWriteExecutor.execute(() ->{
            scanningTableDao.insert(scanningTable);
        });
    }

    public void insert(List<ScanningTable> scanningTables)
    {
        scanningTableDao.insert(scanningTables);
    }
}
