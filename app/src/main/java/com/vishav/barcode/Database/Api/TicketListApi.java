package com.vishav.barcode.Database.Api;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vishav.barcode.DataService;
import com.vishav.barcode.Database.Entities.TicketListTable;
import com.vishav.barcode.Database.Repo.TicketListRepo;
import com.vishav.barcode.RetrofitConnection;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TicketListApi {
    private DataService dataService;
    private MutableLiveData<List<TicketListTable>> allTicketsListFromApi = new MutableLiveData<>();
    private TicketListRepo repo;
    private boolean isInserted = false;
    public TicketListApi(Application application){
        repo = new TicketListRepo(application);
        dataService = RetrofitConnection.getRetroFitInstance().create(DataService.class);
    }

    public LiveData<List<TicketListTable>> getAllTicketsListFromApi()
    {
        Call<List<TicketListTable>> call = dataService.getTicketLists();
        call.enqueue(new Callback<List<TicketListTable>>() {
            @Override
            public void onResponse(Call<List<TicketListTable>> call, Response<List<TicketListTable>> response) {
                allTicketsListFromApi.setValue(response.body());
                List<TicketListTable> list = response.body();
                repo.insert(list);
                //repo.fetchTickets();
                Log.i("RESPONSE_API_LIST", response.code() +"");
            }

            @Override
            public void onFailure(Call<List<TicketListTable>> call, Throwable t) {
                Log.i("RESPONSE_API_LIST", t.getMessage() +"");

            }
        });
        return allTicketsListFromApi;
    }

    public long insertApi(TicketListTable ticketListTable) throws IOException {
        Call<TicketListTable> ticketListTableCall = dataService.createTicketList(ticketListTable);

        ticketListTableCall.enqueue(new Callback<TicketListTable>() {
            @Override
            public void onResponse(Call<TicketListTable> call, Response<TicketListTable> response) {
                response.body();
            }

            @Override
            public void onFailure(Call<TicketListTable> call, Throwable t) {

            }
        });

        while(true)
        {
            if(ticketListTableCall.isExecuted())
            {
                break;
            }
        }

        return repo.insert(ticketListTable);
    }
}
