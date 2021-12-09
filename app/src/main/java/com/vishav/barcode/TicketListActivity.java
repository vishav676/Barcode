package com.vishav.barcode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.vishav.barcode.Database.Entities.TicketListTable;
import com.vishav.barcode.ViewModels.TicketTableVM;
import com.vishav.barcode.databinding.ActivityTicketListBinding;

import java.io.Serializable;
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
    List<TicketListTable> ticketList;
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
                ticketList = ticketListTables;
                display(ticketListName);
            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), TicketsActivity.class);
                intent.putExtra("TicketList", (Serializable) ticketList.get(i));
                startActivity(intent);
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