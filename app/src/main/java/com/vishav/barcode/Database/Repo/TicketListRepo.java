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
    private MutableLiveData<List<TicketListTable>> allTicketsListFromApi = new MutableLiveData<>();
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
    }

    public LiveData<List<TicketListTable>> getAllTicketsListFromApi()
    {
        Call<List<TicketListTable>> call = dataService.getTicketLists();
        call.enqueue(new Callback<List<TicketListTable>>() {
            @Override
            public void onResponse(Call<List<TicketListTable>> call, Response<List<TicketListTable>> response) {
                allTicketsListFromApi.setValue(response.body());
                List<TicketListTable> list = response.body();
                saveDataToDatabaseFromApi(list);
            }

            @Override
            public void onFailure(Call<List<TicketListTable>> call, Throwable t) {

            }
        });
        return allTicketsListFromApi;
    }

    public void insertApi(TicketListTable ticketListTable)
    {
        Call<TicketListTable> ticketListTableCall = dataService.createTicketList(ticketListTable);
        ticketListTableCall.enqueue(new Callback<TicketListTable>() {
            @Override
            public void onResponse(Call<TicketListTable> call, Response<TicketListTable> response) {
                Log.i("RESPONSE_API", response.code() +"");
            }

            @Override
            public void onFailure(Call<TicketListTable> call, Throwable t) {

            }
        });
    }

    private void saveDataToDatabaseFromApi(List<TicketListTable> ticketListTables)
    {
        ticketListDao.insert(ticketListTables);
    }

}
