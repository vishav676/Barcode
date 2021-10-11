package com.vishav.barcode.Database.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "CheckingTicketListTableRelationship",
foreignKeys = {
        @ForeignKey(entity = CheckingTable.class,
            parentColumns = "CheckingID",
            childColumns = "CheckingListEventId"),
        @ForeignKey(entity = TicketListTable.class,
            parentColumns = "TicketListPrimaryId",
            childColumns = "CheckingTicketListId")
        }
)
public class CheckingTicketListTableRelationship {

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "PrimaryKey", index = true)
    private long id;

    @ColumnInfo(name = "CheckingListEventId")
    private long checkingListEventId;

    @ColumnInfo(name = "CheckingTicketListId")
    private long checkingTicketListId;

    public CheckingTicketListTableRelationship(long checkingListEventId, long checkingTicketListId) {
        this.checkingListEventId = checkingListEventId;
        this.checkingTicketListId = checkingTicketListId;
    }



    public long getCheckingListEventId() {
        return checkingListEventId;
    }

    public void setCheckingListEventId(long checkingListEventId) {
        this.checkingListEventId = checkingListEventId;
    }

    public long getCheckingTicketListId() {
        return checkingTicketListId;
    }

    public void setCheckingTicketListId(long checkingTicketListId) {
        this.checkingTicketListId = checkingTicketListId;
    }
}
