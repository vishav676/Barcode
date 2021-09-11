package com.vishav.barcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.common.util.ArrayUtils;
import com.vishav.barcode.Database.DatabaseHelper;
import com.vishav.barcode.Models.TicketList;
import com.vishav.barcode.databinding.ActivityTicketListBinding;

import java.util.ArrayList;
import java.util.List;

public class TicketListActivity extends AppCompatActivity {

    private DatabaseHelper db;
    private ActivityTicketListBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTicketListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Context mContext = getApplicationContext();
        db = new DatabaseHelper(mContext);
        ListView lv = binding.ticketListLV;

        List<TicketList> ticketLists = db.getAllTicketLists();
        ArrayList<String> ticketListNames = populateString(ticketLists);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext,
                android.R.layout.simple_list_item_1, android.R.id.text1, ticketListNames);

        lv.setAdapter(adapter);
    }

    private ArrayList<String> populateString(List<TicketList> ticketLists)
    {
        ArrayList<String> ticketListNames = new ArrayList<String>();
        for(TicketList ticketList : ticketLists)
        {
            ticketListNames.add(ticketList.getTicketListName());
        }
        return ticketListNames;
    }

}