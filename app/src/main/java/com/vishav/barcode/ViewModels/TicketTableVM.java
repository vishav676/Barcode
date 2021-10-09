package com.vishav.barcode.ViewModels;

import android.app.Application;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.vishav.barcode.DataService;
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
    public LiveData<List<TicketListTable>> getAllTicketListName(){return allTicketListNames;}
    public List<TicketTable> getAllTicketsFromListID(long id){return ticketListRepo.getAllTicketsFromListID(id);}
    public List<TicketTable> getAllEventTickets(long id){return checkingTableRepo.getEventTickets(id);}

    public void insert(TicketTable ticketTable)
    {
        ticketTableRepo.insert(ticketTable);
    }

    public long insert(CheckingTable event){return checkingTableRepo.insert(event);}

    public void insert(ScanningTable history){scanningTableRepo.insert(history);}

    public long insert(TicketListTable ticketList){ return ticketListRepo.insert(ticketList);}

    public void insert(CheckingTicketListTableRelationship relationship){checkingTicketListTableRepo.insert(relationship);}

    public void updateTicketUseable(int ticketUseable,long id){ticketTableRepo.updateTicketUseable(ticketUseable, id);}

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
        /*DataService service = RetrofitConnection.getRetroFitInstance().create(DataService.class);
        Call<List<TicketListTable>> call = service.getTicketLists();
        call.enqueue(new Callback<List<TicketListTable>>() {
            @Override
            public void onResponse(Call<List<TicketListTable>> call, Response<List<TicketListTable>> response) {
                List<TicketListTable> ticketLists = response.body();
                //Toast.makeText(TicketListActivity.this, "" + response.code()  ,Toast.LENGTH_SHORT).show();
                allTicketListNames.setValue(response.body());

               // display(ticketListName);

            }

            @Override
            public void onFailure(Call<List<TicketListTable>> call, Throwable t) {
                //Toast.makeText(TicketListActivity.this, "Something Went Wrong", Toast.LENGTH_SHORT).show();
            }
        });
        return allTicketListNames;*/
        return ticketListRepo.getAllTicketsList();
    }
}
