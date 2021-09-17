package com.vishav.barcode.Database.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.vishav.barcode.Database.Entities.TicketTable;

import java.util.List;

@Dao
public interface TicketTableDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(TicketTable ticketTable);

    @Query("SELECT * FROM TicketTable Where TicketNumber = :tickNumber")
    TicketTable searchTicket(String tickNumber);

    @Query("SELECT * FROM TicketTable")
    List<TicketTable> getAll();

    @Query("UPDATE TicketTable SET TicketUseable = :ticketUseable Where TicketID = :id")
    void updateTicketUseable(int ticketUseable, long id);
}
