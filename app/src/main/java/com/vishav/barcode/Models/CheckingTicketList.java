package com.vishav.barcode.Models;

public class CheckingTicketList {
    int ticketListId;
    int checkingListId;

    public CheckingTicketList(int ticketListId, int checkingListId){
        this.checkingListId =  checkingListId;
        this.ticketListId = ticketListId;
    }

    public int getTicketListId() {
        return ticketListId;
    }

    public int getCheckingListId() {
        return checkingListId;
    }
}
