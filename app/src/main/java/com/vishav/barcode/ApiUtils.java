package com.vishav.barcode;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.vishav.barcode.Database.Api.CheckingApi;
import com.vishav.barcode.Database.Api.CheckingListApi;
import com.vishav.barcode.Database.Api.ScanningApi;
import com.vishav.barcode.Database.Api.TicketApi;
import com.vishav.barcode.Database.Api.TicketListApi;
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
    private TicketApi ticketTableRepo;
    private CheckingApi checkingTableRepo;
    private ScanningApi scanningTableRepo;
    private CheckingListApi checkingTicketListTableRepo;
    private TicketListApi ticketListRepo;
    private DataService dataService;
    private boolean RESULT = false;
    private Application application;

    public ApiUtils(Application application)
    {
        this.application = application;
        ticketListRepo = new TicketListApi(application);
        ticketTableRepo = new TicketApi(application);
        checkingTableRepo = new CheckingApi(application);
        scanningTableRepo = new ScanningApi(application);
        checkingTicketListTableRepo = new CheckingListApi(application);
        dataService = RetrofitConnection.getRetroFitInstance().create(DataService.class);
    }

    public void fetchData() throws IOException, InterruptedException {
        getTicketLists();
        getCheckings();
        //getCheckingTicketRelation();
    }

    private void getTickets() throws IOException {
        ticketTableRepo.getAllTicketsListFromApi();

    }

    private void getTicketLists() throws IOException, InterruptedException {
        if(ticketListRepo.getAllTicketsListFromApi() != null){
            getTickets();
        }
    }

    private void getScannings() throws IOException {
        scanningTableRepo.getScanning();
    }

    private void getCheckings() throws IOException, InterruptedException {
        if(checkingTableRepo.getAllCheckings()!=null)
        {
            getScannings();
        }
    }

    public void getCheckingTicketRelation() throws IOException {
        checkingTicketListTableRepo.getAllCheckingsList();
    }

    private void displayMessage(String info)
    {
        Toast.makeText(application.getApplicationContext(),
                info + " Received",
                Toast.LENGTH_SHORT).show();

    }
}
