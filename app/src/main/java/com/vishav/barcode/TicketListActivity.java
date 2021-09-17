package com.vishav.barcode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.vishav.barcode.ViewModels.TicketTableVM;
import com.vishav.barcode.databinding.ActivityTicketListBinding;

import java.util.List;

public class TicketListActivity extends AppCompatActivity {

    private ActivityTicketListBinding binding;
    private TicketTableVM ticketTableVm;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityTicketListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Context mContext = getApplicationContext();
        ListView lv = binding.ticketListLV;
        ticketTableVm = new ViewModelProvider(this).get(TicketTableVM.class);
        //ArrayList<String> ticketListNames = populateString(ticketLists);
        //List<TicketList> ticketLists = db.getAllTicketLists();
        ticketTableVm.getAllTicketListName().observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                if(strings != null)
                {
                    adapter = new ArrayAdapter<String>(mContext,
                            android.R.layout.simple_list_item_1, android.R.id.text1, strings);
                    lv.setAdapter(adapter);
                }
                adapter.notifyDataSetChanged();
            }
        });

    }

   /* private ArrayList<String> populateString(LiveData<List<TicketListTable>> ticketLists)
    {
        ArrayList<String> ticketListNames = new ArrayList<String>();
        for(TicketListTable ticketList : ticketLists)
        {
            ticketListNames.add(ticketList.());
        }
        return ticketListNames;
    }*/

}