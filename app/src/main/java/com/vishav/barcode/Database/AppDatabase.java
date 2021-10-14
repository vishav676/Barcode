package com.vishav.barcode.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.vishav.barcode.Database.Dao.CheckingTableDao;
import com.vishav.barcode.Database.Dao.CheckingTicketListTableDao;
import com.vishav.barcode.Database.Dao.ScanningTableDao;
import com.vishav.barcode.Database.Dao.TicketListDao;
import com.vishav.barcode.Database.Dao.TicketTableDao;
import com.vishav.barcode.Database.Entities.CheckingTable;
import com.vishav.barcode.Database.Entities.CheckingTicketListTableRelationship;
import com.vishav.barcode.Database.Entities.ELBCardListTable;
import com.vishav.barcode.Database.Entities.ELBCardTable;
import com.vishav.barcode.Database.Entities.ScanningTable;
import com.vishav.barcode.Database.Entities.TicketListTable;
import com.vishav.barcode.Database.Entities.TicketTable;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {CheckingTable.class,
        CheckingTicketListTableRelationship.class,
        ELBCardListTable.class,
        ELBCardTable.class,
        ScanningTable.class,
        TicketListTable.class,
        TicketTable.class}, version = 10)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TicketTableDao ticketTableDao();
    public abstract CheckingTableDao checkingTableDao();
    public abstract ScanningTableDao scanningTableDao();
    public abstract CheckingTicketListTableDao checkingTicketListTableDao();
    public abstract TicketListDao ticketListDao();
    private static volatile AppDatabase INSTANCE;

    public static final ExecutorService databaseWriteExecutor = Executors.newFixedThreadPool(4);

    public static AppDatabase getDatabase(final Context context){
        if(INSTANCE == null)
        {
            synchronized (AppDatabase.class)
            {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "app_database")
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
