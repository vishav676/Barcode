package com.vishav.barcode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.vishav.barcode.Database.Entities.TicketListTable;
import com.vishav.barcode.ViewModels.TicketTableVM;
import com.vishav.barcode.databinding.ActivityTicketListBinding;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TicketListActivity extends AppCompatActivity {

    private ActivityTicketListBinding binding;
    private TicketTableVM ticketTableVm;
    ArrayAdapter<String> adapter;
    ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTicketListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Context mContext = getApplicationContext();
        lv = binding.ticketListLV;
        ticketTableVm = new ViewModelProvider(this).get(TicketTableVM.class);
        ticketTableVm.getNames().observe(this, new Observer<List<TicketListTable>>() {
            @Override
            public void onChanged(List<TicketListTable> ticketListTables) {
                ArrayList<String> ticketListName = new ArrayList<>();
                for (TicketListTable list : ticketListTables) {
                    ticketListName.add(list.getTicketListName());
                }
                display(ticketListName);
            }
        });
    }
    private void display(ArrayList<String> ticketListNames)
    {
        adapter = new ArrayAdapter<String>(TicketListActivity.this,
                android.R.layout.simple_list_item_1, android.R.id.text1, ticketListNames);
        lv.setAdapter(adapter);
    }

}