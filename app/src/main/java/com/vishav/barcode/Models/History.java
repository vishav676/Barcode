package com.vishav.barcode.Models;

public class History {
    private int ID;
    private String status;
    private String time;
    private String Issue;
    private String note;
    private Boolean manualy;
    private String ticketNum;
    private int checkingID;
    private int timesUsed;

    public History(int ID, String status, String time, String issue, String note, Boolean manualy, int checkingID, String ticketNum, int timesUsed) {
        this.ID = ID;
        this.status = status;
        this.time = time;
        this.Issue = issue;
        this.note = note;
        this.manualy = manualy;
        this.checkingID = checkingID;
        this.ticketNum = ticketNum;
        this.timesUsed = timesUsed;
    }

    public History(String status, String time, String issue, String note, Boolean manualy, int checkingID, String ticketNum) {
        this.status = status;
        this.time = time;
        Issue = issue;
        this.note = note;
        this.manualy = manualy;
        this.checkingID = checkingID;
        this.ticketNum = ticketNum;
        this.timesUsed = 1;
    }

    public int getID() {
        return ID;
    }

    public String getStatus() {
        return status;
    }

    public String getTime() {
        return time;
    }

    public String getIssue() {
        return Issue;
    }

    public String getNote() {
        return note;
    }

    public Boolean getManualy() {
        return manualy;
    }

    public int getCheckingID() {
        return checkingID;
    }

    public String getTicketID()
    {
        return ticketNum;
    }

    public void setTimesUsed()
    {
        this.timesUsed += 1;
    }

    public String getTimesUsed()
    {
        return String.valueOf(this.timesUsed);
    }
}
