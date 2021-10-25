package com.vishav.barcode.ViewModels;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vishav.barcode.DataService;
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
import com.vishav.barcode.RetrofitConnection;
import com.vishav.barcode.TicketListActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TicketTableVM extends AndroidViewModel {
    private TicketTableRepo ticketTableRepo;
    private TicketListRepo ticketListRepo;
    private ScanningTableRepo scanningTableRepo;
    private CheckingTableRepo checkingTableRepo;
    private CheckingTicketListTableRepo checkingTicketListTableRepo;

    private TicketApi ticketApi;
    private TicketListApi ticketListApi;
    private ScanningApi scanningApi;
    private CheckingApi checkingApi;
    private CheckingListApi checkingListApi;

    private final List<TicketTable> allTickets;
    private final LiveData<List<CheckingTable>> allEvents;
    private final LiveData<List<ScanningTable>> allHistory;
    private final LiveData<List<TicketListTable>> allTicketList;
    private final LiveData<List<CheckingTicketListTableRelationship>> allCheckingTicketList;

    private final MutableLiveData<List<TicketListTable>> allTicketListNames = new MutableLiveData<>();

    public TicketTableVM(@NonNull Application application) {
        super(application);
        ticketTableRepo = new TicketTableRepo(application);
        ticketListRepo = new TicketListRepo(application);
        scanningTableRepo = new ScanningTableRepo(application);
        checkingTableRepo = new CheckingTableRepo(application);
        checkingTicketListTableRepo = new CheckingTicketListTableRepo(application);

        ticketApi = new TicketApi(application);
        ticketListApi = new TicketListApi(application);
        checkingApi = new CheckingApi(application);
        scanningApi = new ScanningApi(application);
        checkingListApi = new CheckingListApi(application);

        allTicketList = ticketListRepo.getAllTicketList();
        allHistory = scanningTableRepo.getAllHistory();
        allEvents = checkingTableRepo.getAllEvents();
        allTickets = ticketTableRepo.getAllTickets();
       // allTicketListNames = ticketListRepo.getAllTicketListNames();
        allCheckingTicketList = checkingTicketListTableRepo.getAllEventTickets();
    }

    public List<TicketTable> getAllTickets()
    {
        return allTickets;
    }
    public LiveData<List<CheckingTable>> getAllEvents(){ return allEvents; }
    public LiveData<List<ScanningTable>> getAllHistory(){ return allHistory; }
    public LiveData<List<TicketListTable>> getAllTicketList(){return allTicketList;}
    public LiveData<List<CheckingTicketListTableRelationship>> getAllCheckingTicketList(){return allCheckingTicketList;}
    public List<TicketTable> getAllTicketsFromListID(long id){return ticketListRepo.getAllTicketsFromListID(id);}
    public List<TicketTable> getAllEventTickets(long id){return checkingTableRepo.getEventTickets(id);}

    public void insert(TicketTable ticketTable)
    {
        if(isConnected(getApplication())) {
            try {
                ticketApi.insert(ticketTable);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public long insert(CheckingTable event){
        if(isConnected(getApplication()))
            return checkingApi.insert(event);
        return 0;
    }

    public void insert(ScanningTable history){
        if(isConnected(getApplication()))
            scanningApi.insert(history);
    }

    public long insert(TicketListTable ticketList) throws IOException {
        if(isConnected(getApplication()))
            return ticketListApi.insertApi(ticketList);
        return -1;
    }

    public void insert(CheckingTicketListTableRelationship relationship){
        if(isConnected(getApplication()))
            checkingListApi.insert(relationship);
    }

    public void updateTicketUseable(int ticketUseable,long id){
        if(isConnected(getApplication()))
            ticketTableRepo.updateTicketUseable(ticketUseable, id);
    }

    public TicketTable getOneTicket(String ticketNumber)
    {
        return ticketTableRepo.getOneTicket(ticketNumber);
    }

    public ScanningTable getOneHistory(String ticketNumber)
    {
        return scanningTableRepo.getOneHistory(ticketNumber);
    }

    public CheckingTable getOneEvent(String ticketNumber)
    {
        return checkingTableRepo.getEventInfo(ticketNumber);
    }


    public TicketTable getTicketInfo(String ticketNumber, String evenName)
    {
        return ticketTableRepo.getTicketInfo(ticketNumber, evenName);
    }

    public LiveData<List<TicketListTable>> getNames()
    {
        return ticketListRepo.getAllTicketList();
    }

    public void updateTicketToApi(TicketTable ticketTable)
    {
        if(isConnected(getApplication()))
            ticketApi.updateTicket(ticketTable);
    }

    private Boolean isConnected(Application application)
    {
        ConnectivityManager connectivityManager = (ConnectivityManager)
                application.getSystemService(
                        Context.CONNECTIVITY_SERVICE
                );
        return connectivityManager.getActiveNetworkInfo() != null &&
                connectivityManager.getActiveNetworkInfo().isConnected();
    }
}
