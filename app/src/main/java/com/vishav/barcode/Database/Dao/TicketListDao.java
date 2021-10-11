package com.vishav.barcode.Database.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.vishav.barcode.Database.Entities.TicketListTable;
import com.vishav.barcode.Database.Entities.TicketTable;

import java.util.List;

@Dao
public interface TicketListDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(TicketListTable ticketListTable);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<TicketListTable> ticketListTables);

    @Query("SELECT * FROM TicketListTable")
    LiveData<List<TicketListTable>> getAll();

    @Query("SELECT TicketListName from TicketListTable")
    LiveData<List<String>> getTicketListNames();

    @Query("SELECT * from TicketTable inner join TicketListTable on TicketListId = TicketListPrimaryId Where TicketListPrimaryId = :id")
    List<TicketTable> getAllTicketsFromListID(long id);
}
