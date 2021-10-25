package com.vishav.barcode.Database.Repo;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;

import com.vishav.barcode.DataService;
import com.vishav.barcode.Database.AppDatabase;
import com.vishav.barcode.Database.Dao.ScanningTableDao;
import com.vishav.barcode.Database.Entities.ScanningTable;
import com.vishav.barcode.Database.Entities.TicketListTable;
import com.vishav.barcode.RetrofitConnection;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ScanningTableRepo {
    private ScanningTableDao scanningTableDao;
    private LiveData<List<ScanningTable>> allHistory;
    private DataService dataService;

    public ScanningTableRepo(Application application)
    {
        AppDatabase db = AppDatabase.getDatabase(application);
        scanningTableDao = db.scanningTableDao();
        allHistory = scanningTableDao.getAllHistory();
        dataService = RetrofitConnection.getRetroFitInstance().create(DataService.class);

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
            scanningTableDao.insert(scanningTable);
    }

    public void insert(List<ScanningTable> scanningTables)
    {
        try {
            scanningTableDao.insert(scanningTables);
        }
        catch (Exception e)
        {
            Log.i("scanning exception", e.getMessage() +"");

        }
    }
}
