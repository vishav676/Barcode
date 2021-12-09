package com.vishav.barcode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.vishav.barcode.Adapter.TicketAdapter;
import com.vishav.barcode.Database.Entities.TicketListTable;
import com.vishav.barcode.Database.Entities.TicketTable;
import com.vishav.barcode.ViewModels.TicketTableVM;
import com.vishav.barcode.databinding.ActivityLoginBinding;
import com.vishav.barcode.databinding.ActivityTicketsBinding;

import java.util.List;

public class TicketsActivity extends AppCompatActivity {

    private ActivityTicketsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTicketsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        TicketTableVM ticketTableVM = new ViewModelProvider(this).get(TicketTableVM.class);

        TicketListTable ticketListTable = (TicketListTable) intent.getSerializableExtra("TicketList");
        displayTickets(ticketTableVM.getAllTicketsFromListID(ticketListTable.getId()));
    }

    private void displayTickets(List<TicketTable> allTicketsFromListID) {
        RecyclerView rv = binding.ticketsRV;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv.setLayoutManager(linearLayoutManager);
        TicketAdapter adapter = new TicketAdapter(allTicketsFromListID);
        rv.setAdapter(adapter);
    }
}