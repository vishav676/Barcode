package com.vishav.barcode.Database.Repo;

import android.app.Application;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vishav.barcode.DataService;
import com.vishav.barcode.Database.AppDatabase;
import com.vishav.barcode.Database.Dao.TicketListDao;
import com.vishav.barcode.Database.Entities.TicketListTable;
import com.vishav.barcode.Database.Entities.TicketTable;
import com.vishav.barcode.RetrofitConnection;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicLong;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TicketListRepo {
    private TicketListDao ticketListDao;
    private LiveData<List<TicketListTable>> allTicketList;
    private LiveData<List<String>> allTicketListNames;
    private MutableLiveData<List<TicketListTable>> allTicketsList = new MutableLiveData<>();
    private DataService dataService;

    public TicketListRepo(Application application)
    {
        AppDatabase db = AppDatabase.getDatabase(application);
        ticketListDao = db.ticketListDao();
        allTicketList = ticketListDao.getAll();
        dataService = RetrofitConnection.getRetroFitInstance().create(DataService.class);
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

    public LiveData<List<TicketListTable>> getAllTicketsList()
    {
        Call<List<TicketListTable>> call = dataService.getTicketLists();
        call.enqueue(new Callback<List<TicketListTable>>() {
            @Override
            public void onResponse(Call<List<TicketListTable>> call, Response<List<TicketListTable>> response) {
                allTicketsList.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<TicketListTable>> call, Throwable t) {
                //Toast.makeText(TicketListActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
        return allTicketsList;
    }
}
