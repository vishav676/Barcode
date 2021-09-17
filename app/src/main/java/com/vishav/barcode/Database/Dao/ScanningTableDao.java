package com.vishav.barcode.Database.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.vishav.barcode.Database.Entities.ScanningTable;

import java.util.List;

@Dao
public interface ScanningTableDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(ScanningTable history);

    @Query("SELECT * FROM ScanningTable Where ScanningTicketNumber = :ticketNumber")
    ScanningTable getHistory(String ticketNumber);

    @Query("SELECT * FROM ScanningTable")
    LiveData<List<ScanningTable>> getAllHistory();
}
