package com.vishav.barcode;

import android.app.Application;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.vishav.barcode.Database.Entities.CheckingTable;
import com.vishav.barcode.Database.Entities.CheckingTicketListTableRelationship;
import com.vishav.barcode.Database.Entities.ScanningTable;
import com.vishav.barcode.Database.Entities.TicketListTable;
import com.vishav.barcode.Database.Entities.TicketTable;
import com.vishav.barcode.Database.Repo.CheckingTableRepo;
import com.vishav.barcode.Database.Repo.CheckingTicketListTableRepo;
import com.vishav.barcode.Database.Repo.ScanningTableRepo;
import com.vishav.barcode.Database.Repo.TicketListRepo;
import com.vishav.barcode.Database.Repo.TicketTableRepo;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiUtils {
    private TicketTableRepo ticketTableRepo;
    private CheckingTableRepo checkingTableRepo;
    private ScanningTableRepo scanningTableRepo;
    private CheckingTicketListTableRepo checkingTicketListTableRepo;
    private TicketListRepo ticketListRepo;
    private DataService dataService;
    private Application application;

    public ApiUtils(Application application)
    {
        this.application = application;
        ticketListRepo = new TicketListRepo(application);
        ticketTableRepo = new TicketTableRepo(application);
        checkingTableRepo = new CheckingTableRepo(application);
        scanningTableRepo = new ScanningTableRepo(application);
        checkingTicketListTableRepo = new CheckingTicketListTableRepo(application);
        dataService = RetrofitConnection.getRetroFitInstance().create(DataService.class);
    }

    public void fetchData() throws IOException {
        getTickets();
        getCheckings();
        getScannings();
        getTicketLists();
        //getCheckingTicketRelation();
    }

    private void getTickets() throws IOException {
        Call<List<TicketTable>> call = dataService.getTickets();

        call.enqueue(new Callback<List<TicketTable>>() {
            @Override
            public void onResponse(Call<List<TicketTable>> call, Response<List<TicketTable>> response) {
                ticketTableRepo.insert(response.body());
                displayMessage("Tickets");
                Log.i("RESPONSE_API_LIST", response.code() +"");
            }

            @Override
            public void onFailure(Call<List<TicketTable>> call, Throwable t) {
                Log.i("RESPONSE_API_LIST", t.getMessage() +"");

            }
        });
    }

    private void getTicketLists() throws IOException {
        Call<List<TicketListTable>> call = dataService.getTicketLists();
        call.enqueue(new Callback<List<TicketListTable>>() {
            @Override
            public void onResponse(Call<List<TicketListTable>> call, Response<List<TicketListTable>> response) {
                ticketListRepo.insert(response.body());
                displayMessage("Tickets Lists");
                Log.i("RESPONSE_API_LIST", response.code() +"");
            }

            @Override
            public void onFailure(Call<List<TicketListTable>> call, Throwable t) {
                Log.i("RESPONSE_API_LIST", t.getMessage() +"");

            }
        });
    }

    private void getScannings() throws IOException {
        Call<List<ScanningTable>> call = dataService.getScannings();

        call.enqueue(new Callback<List<ScanningTable>>() {
            @Override
            public void onResponse(Call<List<ScanningTable>> call, Response<List<ScanningTable>> response) {
                if(response.body() != null)
                    scanningTableRepo.insert(response.body());

            }

            @Override
            public void onFailure(Call<List<ScanningTable>> call, Throwable t) {

            }
        });
    }

    private void getCheckings() throws IOException {
        Call<List<CheckingTable>> call = dataService.getCheckings();
        call.enqueue(new Callback<List<CheckingTable>>() {
            @Override
            public void onResponse(Call<List<CheckingTable>> call, Response<List<CheckingTable>> response) {
                checkingTableRepo.insert(response.body());
                displayMessage("Checkings");

            }

            @Override
            public void onFailure(Call<List<CheckingTable>> call, Throwable t) {

            }
        });
    }

    public void getCheckingTicketRelation() throws IOException {
        Call<List<CheckingTicketListTableRelationship>> call = dataService.getCheckingTicketList();

        call.enqueue(new Callback<List<CheckingTicketListTableRelationship>>() {
            @Override
            public void onResponse(Call<List<CheckingTicketListTableRelationship>> call, Response<List<CheckingTicketListTableRelationship>> response) {
                checkingTicketListTableRepo.insert(response.body());

            }

            @Override
            public void onFailure(Call<List<CheckingTicketListTableRelationship>> call, Throwable t) {

            }
        });
    }

    private void displayMessage(String info)
    {
        Toast.makeText(application.getApplicationContext(),
                info + " Received",
                Toast.LENGTH_SHORT).show();

    }


}
