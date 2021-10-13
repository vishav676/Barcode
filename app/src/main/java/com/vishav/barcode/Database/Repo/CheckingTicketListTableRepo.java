package com.vishav.barcode.Database.Repo;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.vishav.barcode.Database.AppDatabase;
import com.vishav.barcode.Database.Dao.CheckingTicketListTableDao;
import com.vishav.barcode.Database.Entities.CheckingTable;
import com.vishav.barcode.Database.Entities.CheckingTicketListTableRelationship;

import java.util.List;

public class CheckingTicketListTableRepo {

    private CheckingTicketListTableDao checkingTicketListTableDao;
    private LiveData<List<CheckingTicketListTableRelationship>> allEventTickets;

    public CheckingTicketListTableRepo(Application application)
    {
        AppDatabase db = AppDatabase.getDatabase(application);
        checkingTicketListTableDao = db.checkingTicketListTableDao();
        allEventTickets = checkingTicketListTableDao.getAll();
    }

    public LiveData<List<CheckingTicketListTableRelationship>> getAllEventTickets(){return allEventTickets;}

    public void insert(CheckingTicketListTableRelationship CheckingTicketListTableRelationship)
    {
        checkingTicketListTableDao.insert(CheckingTicketListTableRelationship);
    }
}
