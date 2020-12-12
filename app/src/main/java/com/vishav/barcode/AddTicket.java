package com.vishav.barcode;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddTicket extends AppCompatActivity implements View.OnClickListener{

    EditText ticketNumber, customer, info, warningNote, warning, eventId, useable;
    String ticketNumberValue, customerValue, infoValue, warningNoteValue, warningValue,eventIdValue;
    int useableValue;
    Button save;
    dbHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_ticket);

        db = new dbHelper(this);
        ticketNumber = findViewById(R.id.tvTicketNumber);
        customer = findViewById(R.id.tvCustomerName);
        info = findViewById(R.id.tvInfo);
        warningNote = findViewById(R.id.tvWarningNote);
        warning = findViewById(R.id.tvWarning);
        eventId = findViewById(R.id.tvEvent);
        save = findViewById(R.id.saveTicket);
        useable = findViewById(R.id.tvUseable);

        save.setOnClickListener(this);
    }
    public void getValues(){
        ticketNumberValue = ticketNumber.getText().toString();
        customerValue = customer.getText().toString();
        infoValue = info.getText().toString();
        warningNoteValue = warningNote.getText().toString();
        warningValue = warning.getText().toString();
        eventIdValue = eventId.getText().toString();
        useableValue = Integer.parseInt(useable.getText().toString());
        if(ticketNumberValue != null && customerValue != null && eventIdValue != null){
            Ticket ticket = new Ticket(ticketNumberValue,customerValue,infoValue,
                    warningNoteValue, useableValue, warningValue, Integer.parseInt(eventIdValue));
            saveTicket(ticket);
        }
        else {
            Toast.makeText(this, "Enter Required Details", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveTicket(Ticket ticket){
        db.insertTicket(ticket);
        Toast.makeText(this, "Added Successfully", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onClick(View view) {
        getValues();
    }
}