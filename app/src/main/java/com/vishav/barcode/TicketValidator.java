package com.vishav.barcode;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.vishav.barcode.Database.Entities.CheckingTable;
import com.vishav.barcode.Database.Entities.ScanningTable;
import com.vishav.barcode.Database.Entities.TicketTable;
import com.vishav.barcode.ViewModels.TicketTableVM;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class TicketValidator {

    CardView error_cardView;
    TicketTableVM ticketTableVM;
    TextView issue, errorNum,errorDetail;
    Activity activity;

    public TicketValidator(CardView error_cardView, TicketTableVM ticketTableVM,
                           Activity context)
    {
        this.error_cardView = error_cardView;
        this.ticketTableVM = ticketTableVM;
        errorNum = error_cardView.findViewById(R.id.errorNum);
        issue = error_cardView.findViewById(R.id.issueTv);
        this.activity = context;
        errorDetail = error_cardView.findViewById(R.id.tvErrorDetail);
    }


    public void inValidMessageTicketNotFound(String errorTicket) {
        error_cardView.setVisibility(View.VISIBLE);
        issue.setText("Ticket Number not in the list");
        errorNum.setText(errorTicket);
        delay(error_cardView);
    }

    public void inValidMessageAlreadyUsed(TicketTable ticket) {
        error_cardView.setVisibility(View.VISIBLE);
        issue.setText("Already used");
        errorNum.setText(ticket.getTicketNumber());
        delay(error_cardView);
        history("Failed",ticket,"Already Used");
    }

    public void inValidMessageAllTriesUsed(TicketTable ticket) {
        error_cardView.setVisibility(View.VISIBLE);
        issue.setText("All Tries Used");
        errorNum.setText(ticket.getTicketNumber());
        delay(error_cardView);
        history("Failed",ticket,"All Tries Used");
    }


    private void history(String status,TicketTable barcode,String issue) {
        TicketTable ticket = ticketTableVM.getOneTicket(barcode.getTicketNumber());
        int scanningTimesUsed = 1;
        CheckingTable event = ticketTableVM.getOneEvent(barcode.getTicketNumber());
        ScanningTable getHistory = ticketTableVM.getOneHistory(barcode.getTicketNumber());
        ticketTableVM.updateTicketToApi(barcode);
        if(getHistory != null)
        {
            scanningTimesUsed += getHistory.getScanningTimesUsed();
        }

        ScanningTable history = new ScanningTable(status, trackHistory(),
                true,
                issue,
                "no",
                scanningTimesUsed,
                event.getId(),
                ticket.getTicketNumber());

        ticketTableVM.insert(history);
    }

    private String trackHistory()
    {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        return sd.format(date);
    }

    private void delay(CardView cardView) {
        Timer time = new Timer();
        time.schedule(new TimerTask() {
            @Override
            public void run() {
                if (activity != null) {
                    activity.runOnUiThread(() -> {
                        cardView.setVisibility(View.INVISIBLE);
                    });
                }
            }
        }, 3000);
    }
    public void validTicket(TicketTable barcode, CardView cardView, TextView tvName) {
        ticketTableVM.updateTicketUseable(barcode.getTicketUseable() - 1, barcode.getId());
        cardView.setVisibility(View.VISIBLE);
        TextView tvNumber = cardView.findViewById(R.id.number);
        tvNumber.setText(barcode.getTicketNumber());
        delay(cardView);
        tvName.setText(barcode.getTicketCustomerName());
        history("Success",barcode,"nem");
    }
}
