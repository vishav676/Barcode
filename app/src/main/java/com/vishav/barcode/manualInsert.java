package com.vishav.barcode;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;


public class manualInsert extends Fragment {

    public manualInsert() {
        // Required empty public constructor
    }

    EditText manual;
    Button submit;
    String tickets;
    dbHelper db;
    Context mContext;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_manual_insert, container, false);
        manual = root.findViewById(R.id.etMultiTickets);
        submit = root.findViewById(R.id.submitManualBulk);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tickets = manual.getText().toString();
                if(!tickets.equals("")){
                    saveTickets();
                }
            }
        });
        return root;
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }
    public void saveTickets(){
        db = new dbHelper(mContext);
        String[] split = tickets.split(";");
        String[] header = split[0].split(",");
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
            int event = Integer.parseInt(values[eventPosition]);
            int useable = Integer.parseInt(values[useablePosition]);
            Ticket ticket = new Ticket(number, customer,info,warningNote,useable,warning,event);
            db.insertTicket(ticket);
            Toast.makeText(mContext, "Added", Toast.LENGTH_SHORT).show();
        }
    }
}