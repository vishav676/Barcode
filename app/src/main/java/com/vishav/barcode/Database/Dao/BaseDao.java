package com.vishav.barcode.Database.Dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.sqlite.db.SimpleSQLiteQuery;

@Dao
public abstract class BaseDao<T> {

    private String tableName;
    public BaseDao(String tableName)
    {
        this.tableName = tableName;
    }
    @Insert
    abstract void insert(Object T);

    public SimpleSQLiteQuery getAll()
    {
        SimpleSQLiteQuery query = new SimpleSQLiteQuery("SELECT * FROM " + tableName);
        return query;
    }

}
