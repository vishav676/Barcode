package com.vishav.barcode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Toast;

import com.vishav.barcode.Adapter.HashMapAdapter;
import com.vishav.barcode.Database.DatabaseHelper;
import com.vishav.barcode.Database.Entities.ScanningTable;
import com.vishav.barcode.ViewModels.TicketTableVM;
import com.vishav.barcode.databinding.ActivityHistoryBinding;

import java.util.List;

public class history extends AppCompatActivity {

    private ActivityHistoryBinding binding;
    DatabaseHelper dbHelper;
    HashMapAdapter adapter;
    private TicketTableVM ticketTableVM;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dbHelper = new DatabaseHelper(this);
        ticketTableVM = new ViewModelProvider(this).get(TicketTableVM.class);

        ticketTableVM.getAllHistory().observe(this, new Observer<List<ScanningTable>>() {
            @Override
            public void onChanged(List<ScanningTable> histories) {
                if (histories != null) {
                    adapter = new HashMapAdapter(histories);
                    binding.lvHistory.setAdapter(adapter);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"No record found",Toast.LENGTH_SHORT).show();
                }
                adapter.notifyDataSetChanged();
            }
        });

    }
}