package com.vishav.barcode.Database.Api;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vishav.barcode.DataService;
import com.vishav.barcode.Database.Entities.TicketListTable;
import com.vishav.barcode.Database.Entities.TicketTable;
import com.vishav.barcode.Database.Repo.TicketListRepo;
import com.vishav.barcode.Database.Repo.TicketTableRepo;
import com.vishav.barcode.RetrofitConnection;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TicketApi {

    private DataService dataService;
    private MutableLiveData<List<TicketTable>> allTickets = new MutableLiveData<>();
    private TicketTableRepo repo;
    public TicketApi(Application application){
        repo = new TicketTableRepo(application);
        dataService = RetrofitConnection.getRetroFitInstance().create(DataService.class);
    }

    public LiveData<List<TicketTable>> getAllTicketsListFromApi()
    {
        Call<List<TicketTable>> call = dataService.getTickets();
        call.enqueue(new Callback<List<TicketTable>>() {
            @Override
            public void onResponse(Call<List<TicketTable>> call, Response<List<TicketTable>> response) {
                allTickets.setValue(response.body());
                List<TicketTable> list = response.body();
                repo.insert(list);
                Log.i("RESPONSE_API_LIST", response.code() +"");
            }

            @Override
            public void onFailure(Call<List<TicketTable>> call, Throwable t) {
                Log.i("RESPONSE_API_LIST", t.getMessage() +"");

            }
        });
        return allTickets;
    }

    public void insert(TicketTable ticketTable) throws IOException {
        Call<TicketTable> ticketCall = dataService.createTicket(ticketTable);
        ticketCall.enqueue(new Callback<TicketTable>() {
            @Override
            public void onResponse(Call<TicketTable> call, Response<TicketTable> response) {
                repo.insert(ticketTable);
            }

            @Override
            public void onFailure(Call<TicketTable> call, Throwable t) {

            }
        });
        while(true)
        {
            if(ticketCall.isExecuted())
            {
                break;
            }
        }
    }

    public void updateTicket(TicketTable ticket)
    {
        ticket.setTicketUseable(ticket.getTicketUseable()-1);
        Call<TicketTable> ticketCall = dataService.updateTicket(ticket.getId(), ticket);
        ticketCall.enqueue(new Callback<TicketTable>() {
            @Override
            public void onResponse(Call<TicketTable> call, Response<TicketTable> response) {
                repo.updateTicketUseable(ticket.getTicketUseable(),ticket.getId());
            }

            @Override
            public void onFailure(Call<TicketTable> call, Throwable t) {

            }
        });

    }
}
