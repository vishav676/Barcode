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
        AppDatabase.databaseWriteExecutor.execute(() ->{
            ticketTableDao.insert(ticketTable);
        });
    }

    public TicketTable getTicketInfo(String ticketNumber, String eventName)
    {
        return ticketTableDao.getTicketInfo(ticketNumber,eventName);
    }

    public void insertTicketToApi(TicketTable ticketTable)
    {
        Call<TicketTable> ticketCall = dataService.createTicket(ticketTable);
        ticketCall.enqueue(new Callback<TicketTable>() {
            @Override
            public void onResponse(Call<TicketTable> call, Response<TicketTable> response) {
                Log.i("RESPONSE_API", response.code() +"");
            }

            @Override
            public void onFailure(Call<TicketTable> call, Throwable t) {
                Log.i("RESPONSE_API", t.getMessage() +"");
            }
        });
    }

    public LiveData<List<TicketTable>> getAllTicketsListFromApi()
    {
        Call<List<TicketTable>> call = dataService.getTickets();
        call.enqueue(new Callback<List<TicketTable>>() {
            @Override
            public void onResponse(Call<List<TicketTable>> call, Response<List<TicketTable>> response) {
                allTicketsFromApi.setValue(response.body());
                List<TicketTable> list = response.body();
                insert(list);
                Log.i("RESPONSE_API_LIST", response.code() +"");
            }

            @Override
            public void onFailure(Call<List<TicketTable>> call, Throwable t) {
                Log.i("RESPONSE_API_LIST", t.getMessage() +"");

            }
        });
        return allTicketsFromApi;
    }

    public void insert(List<TicketTable> ticketTables)
    {
        ticketTableDao.insert(ticketTables);
    }

    public void updateTicketToApi(TicketTable ticket)
    {
        ticket.setTicketUseable(ticket.getTicketUseable()-1);
        Call<TicketTable> ticketCall = dataService.updateTicket(ticket.getId(), ticket);
        ticketCall.enqueue(new Callback<TicketTable>() {
            @Override
            public void onResponse(Call<TicketTable> call, Response<TicketTable> response) {
                updateTicketUseable(ticket.getTicketUseable(),ticket.getId());
            }

            @Override
            public void onFailure(Call<TicketTable> call, Throwable t) {

            }
        });

    }
}
