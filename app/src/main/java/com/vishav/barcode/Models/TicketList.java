package com.vishav.barcode.Models;

public class TicketList {
    private String ticketListName;
    private String createdAt;
    private String updatedAt;
    private String ticketListId;

    public TicketList(String ticketListId, String ticketListName, String createdAt, String updatedAt) {
        this.ticketListId = ticketListId;
        this.ticketListName = ticketListName;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int getTicketListId() {
        return Integer.parseInt(ticketListId);
    }
    public String getTicketListName() {
        return ticketListName;
    }

    public String getCreatedAt() {
        return createdAt;
    }


    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
