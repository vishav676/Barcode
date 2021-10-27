package com.vishav.barcode.Database;

import static com.vishav.barcode.TestUtil.getOrAwaitValue;

import android.content.Context;
import android.database.sqlite.SQLiteConstraintException;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.vishav.barcode.Database.Dao.CheckingTableDao;
import com.vishav.barcode.Database.Dao.CheckingTicketListTableDao;
import com.vishav.barcode.Database.Dao.ScanningTableDao;
import com.vishav.barcode.Database.Dao.TicketListDao;
import com.vishav.barcode.Database.Dao.TicketTableDao;
import com.vishav.barcode.Database.Entities.CheckingTable;
import com.vishav.barcode.Database.Entities.CheckingTicketListTableRelationship;
import com.vishav.barcode.Database.Entities.ScanningTable;
import com.vishav.barcode.Database.Entities.TicketListTable;
import com.vishav.barcode.Database.Entities.TicketTable;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

import kotlin.jvm.JvmField;

@RunWith(AndroidJUnit4.class)
public class AppDatabaseTest {
    private AppDatabase appDatabase;
    private TicketTableDao ticketTableDao;
    private CheckingTableDao checkingTableDao;
    private ScanningTableDao scanningTableDao;
    private TicketListDao ticketListDao;
    private CheckingTicketListTableDao checkingTicketListTableDao;
    private Context context;

    TicketTable ticket1 = new TicketTable("15000M",
            "Vishav"
            ,"Nem","Nem",2,
            1,"No");
    TicketTable ticket2 = new TicketTable("15000M",
            "Vishav"
            ,"Nem","Nem",2,
            1,"No");
    TicketTable ticket3 = new TicketTable("15000M",
            "Vishav"
            ,"Nem","Nem",2,
            1,"No");

    CheckingTable event = new CheckingTable("Dance Party",
            "","");

    List<TicketTable> tickets = new ArrayList<>();
    CheckingTicketListTableRelationship checkingTicketList =
            new CheckingTicketListTableRelationship(1,1);

    @Rule
    @JvmField
    public InstantTaskExecutorRule rule = new InstantTaskExecutorRule();

    @Before
    public void setUp()
    {
        context = ApplicationProvider.getApplicationContext();
        appDatabase = Room.inMemoryDatabaseBuilder(context,AppDatabase.class)
                    .allowMainThreadQueries().build();
        ticketTableDao = appDatabase.ticketTableDao();
        checkingTableDao = appDatabase.checkingTableDao();
        scanningTableDao = appDatabase.scanningTableDao();
        ticketListDao = appDatabase.ticketListDao();
        checkingTicketListTableDao = appDatabase.checkingTicketListTableDao();

        ticketListDao.insert(new TicketListTable("Boat Party",
                "",
                ""));

        tickets.add(ticket1);
        tickets.add(ticket2);
        tickets.add(ticket3);
        ticketTableDao.insert(tickets);


        checkingTableDao.insert(event);

        checkingTicketListTableDao.insert(checkingTicketList);



    }

    @After
    public void close(){
        appDatabase.close();
    }

    @Test
    public void insertAndRead() throws InterruptedException {
        CheckingTable checking = new CheckingTable("Diwali Party",
                "","");
        checkingTableDao.insert(checking);
        List<CheckingTable> checkings = getOrAwaitValue(checkingTableDao.getAll());
        Assert.assertTrue(checkings.contains(checking));
    }

    @Test(expected = SQLiteConstraintException.class)
    public void insertionError(){
        ScanningTable scanningTable = new ScanningTable("Success",
                "",false,"Nem",
                "Found",2,5,
                "15000M");
        scanningTableDao.insert(scanningTable);
    }

    @Test
    public void updateScanning()
    {

        TicketTable ticket = new TicketTable("15000M",
                "Vishav"
                ,"Nem","Nem",2,
                1,"No");
        ticketTableDao.insert(ticket);
        ticketTableDao.updateTicketUseable(0,1);
        List<TicketTable> ticketTables = ticketTableDao.getAll();
        TicketTable ticketInDb = ticketTables.stream().filter(x->x.getTicketNumber()
                .equals("15000M"))
                .findFirst()
                .orElse(ticket);
        Assert.assertEquals(0, ticketInDb.getTicketUseable());
    }


    @Test
    public void getAllTicketsFromListIDTest()
    {
        List<TicketTable> ticketTables = ticketListDao.getAllTicketsFromListID(1);
        Assert.assertEquals(ticketTables.size(),3);
    }

    @Test
    public void getEventInfoTest()
    {
        CheckingTable fetch =  checkingTableDao.getEventInfo(ticket1.getTicketNumber());
        Assert.assertEquals(event,fetch);
    }

    @Test
    public void getEventTicketsTest()
    {
        List<TicketTable> eventTickets = checkingTableDao.getEventTickets(1);
        Assert.assertEquals(tickets,eventTickets);
    }
}
