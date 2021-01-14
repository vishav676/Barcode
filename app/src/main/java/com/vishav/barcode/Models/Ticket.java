package com.vishav.barcode.Models;

import android.os.Parcelable;

import java.io.Serializable;

public class Ticket implements Serializable {
    private String ticketNumber;
    private String CustomerName;
    private String Info;
    private String warningNote;
    private int useable;
    private int ticketEvent;
    private String warning;

    public Ticket(String ticketNumber,String CustomerName,String Info,String warningNote, int useable, String warning,int ticketEvent){
        this.ticketNumber = ticketNumber;
        this.CustomerName = CustomerName;
        this.Info = Info;
        this.warningNote = warningNote;
        this.warning = warning;
        this.useable = useable;
        this.ticketEvent = ticketEvent;
    }
    public int getTicketEvent() {
        return ticketEvent;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public String getInfo() {
        return Info;
    }

    public String getWarningNote() {
        return warningNote;
    }

    public int getUseable() {
        return useable;
    }

    public String getWarning() {
        return warning;
    }
}
