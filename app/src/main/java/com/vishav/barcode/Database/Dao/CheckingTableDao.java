package com.vishav.barcode.Database.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.vishav.barcode.Database.Entities.CheckingTable;
import com.vishav.barcode.Database.Entities.TicketListTable;
import com.vishav.barcode.Database.Entities.TicketTable;

import java.util.List;

@Dao
public interface CheckingTableDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(CheckingTable checkingTable);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(List<CheckingTable> checkingTables);

    @Query("SELECT * FROM CheckingTable")
    LiveData<List<CheckingTable>> getAll();

    @Query("SELECT * FROM CheckingTable Where CheckingName = :eventName")
    CheckingTable searchEvent(String eventName);

    @Query("SELECT * From TicketTable inner join TicketListTable on  TicketListId = TicketListPrimaryId " +
            "inner join CheckingTicketListTableRelationship on TicketListPrimaryId = CheckingTicketListId " +
            "inner join CheckingTable on CheckingListEventId = CheckingID " +
            "Where CheckingID = :eventID")
    List<TicketTable> getEventTickets(long eventID);

    @Query("SELECT * from CheckingTable inner join CheckingTicketListTableRelationship on CheckingID = CheckingListEventID " +
            "inner join TicketListTable on CheckingTicketListId = TicketListPrimaryId " +
            "inner join TicketTable on TicketListPrimaryId = TicketListId " +
            "Where TicketNumber = :ticketNumber")
    CheckingTable getEventInfo(String ticketNumber);

}
