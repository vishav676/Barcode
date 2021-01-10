package com.vishav.barcode.Fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vishav.barcode.R;
import com.vishav.barcode.Models.Ticket;
import com.vishav.barcode.Database.dbHelper;
import com.vishav.barcode.databinding.FragmentManualInsertBinding;

import java.util.Arrays;
import java.util.List;


public class manualInsert extends Fragment {

    private FragmentManualInsertBinding root;
    public manualInsert() {
        // Required empty public constructor
    }
    String tickets;
    dbHelper db;
    Context mContext;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        root = FragmentManualInsertBinding.inflate(inflater, container, false);

        root.submitManualBulk.setOnClickListener(view -> {
            tickets = root.etMultiTickets.getText().toString();
            if(!tickets.equals("")){
                try {
                    if(tickets.contains(",")){
                        saveTickets();
                    }else
                    {
                        saveTicketsTabs();
                    }
                    Toast.makeText(mContext, "Added", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {
                    Toast.makeText(mContext, "Please check the format", Toast.LENGTH_SHORT).show();
                }

            }else {
                root.etMultiTickets.setError("Please Enter Tickets");
            }
            root.etMultiTickets.setText("");
        });
        return root.getRoot();
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }
    public void saveTickets(){
        db = new dbHelper(mContext);
        String[] split = tickets.split(";");
        String[] header = split[0].trim().split("\\s*,\\s*");
        List<String> headers= Arrays.asList(header);
        int ticketNumPosition = headers.indexOf("Ticket Number");
        int infoPosition = headers.indexOf("info");
        int notePosition = headers.indexOf("Warning Note");
        int warningPosition = headers.indexOf("Warning");
        int customerPosition = headers.indexOf("Name");
        int eventPosition = headers.indexOf("Event");
        int useablePosition = headers.indexOf("Useable");

        for (int i=1;i<split.length;i++){
            String[] values = split[i].split(",");
            String number = values[ticketNumPosition].replace("\n", "").replace("\r", "");
            String info = values[infoPosition];
            String warningNote = values[notePosition];
            String warning = values[warningPosition];
            String customer = values[customerPosition];
            int event = Integer.parseInt(values[eventPosition].replaceAll("\\s+",""));
            int useable = Integer.parseInt(values[useablePosition].replaceAll("\\s+","").replace("\n", "").replace("\r", ""));
            Ticket ticket = new Ticket(number, customer,info,warningNote,useable,warning,event);
            db.insertTicket(ticket);
        }

    }
    public void saveTicketsTabs(){
        db = new dbHelper(mContext);
        String[] split = tickets.split("\n");
        String[] header = split[0].trim().split("\\s*\t\\s*");
        List<String> headers= Arrays.asList(header);
        int ticketNumPosition = headers.indexOf("Ticket Number");
        int infoPosition = headers.indexOf("info");
        int notePosition = headers.indexOf("Warning Note");
        int warningPosition = headers.indexOf("Warning");
        int customerPosition = headers.indexOf("Name");
        int eventPosition = headers.indexOf("Event");
        int useablePosition = headers.indexOf("Useable");

        for (int i=1;i<split.length;i++){
            String[] values = split[i].split("\t");
            String number = values[ticketNumPosition].replace("\n", "").replace("\r", "");
            String info = values[infoPosition];
            String warningNote = values[notePosition];
            String warning = values[warningPosition];
            String customer = values[customerPosition];
            int event = Integer.parseInt(values[eventPosition]);
            int useable = Integer.parseInt(values[useablePosition].replace("\r", ""));
            Ticket ticket = new Ticket(number, customer,info,warningNote,useable,warning,event);
            db.insertTicket(ticket);
        }

    }
}