package com.vishav.barcode.Database.Dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.vishav.barcode.Database.Entities.CheckingTicketListTableRelationship;

import java.util.List;

@Dao
public interface CheckingTicketListTableDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(CheckingTicketListTableRelationship checkingTicketListTableRelationship);

    @Query("SELECT * From CheckingTicketListTableRelationship")
    LiveData<List<CheckingTicketListTableRelationship>> getAll();
}
