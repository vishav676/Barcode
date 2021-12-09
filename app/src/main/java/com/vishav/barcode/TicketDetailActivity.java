package com.vishav.barcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.vishav.barcode.Database.Entities.TicketTable;
import com.vishav.barcode.databinding.ActivityLoginBinding;
import com.vishav.barcode.databinding.ActivityTicketDetailBinding;

public class TicketDetailActivity extends AppCompatActivity {

    private ActivityTicketDetailBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTicketDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Intent intent = getIntent();
        TicketTable ticket = (TicketTable) intent.getSerializableExtra("ticket");


        if(ticket == null)
        {
            finish();
        }
        else
        {
            binding.customerName.setText(ticket.getTicketCustomerName());
            binding.ticketInfo.setText(ticket.getTicketInfo());
            binding.ticketNumber.setText(ticket.getTicketNumber());
            binding.ticketWarning.setText(ticket.getTicketWarning());
            binding.ticketUseable.setText(Integer.toString(ticket.getTicketUseable()));
        }
    }
}