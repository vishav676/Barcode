package com.vishav.barcode.Fragments;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vishav.barcode.Adapter.GridAdapter;
import com.vishav.barcode.Database.Entities.CheckingTable;
import com.vishav.barcode.Database.Entities.CheckingTicketListTableRelationship;
import com.vishav.barcode.Database.Entities.TicketListTable;
import com.vishav.barcode.Database.Entities.TicketTable;
import com.vishav.barcode.R;
import com.vishav.barcode.ScannerActivity;
import com.vishav.barcode.ViewModels.TicketTableVM;
import com.vishav.barcode.databinding.FragmentTicketsBinding;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TicketsFragment extends Fragment {

    private FragmentTicketsBinding root;
    Context mContext;
    EditText checkingListNameEt;

    ImageView datePickerView;
    DatePickerDialog.OnDateSetListener onDateSetListener;
    String date;
    RecyclerView ticketRecyclerView;
    private TicketTableVM ticketTableVm;
    List<Long> ticketListIds = new ArrayList<>();
    int eventId;
    long newEventId;

    public TicketsFragment() {
        // Required empty public constructor
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = FragmentTicketsBinding.inflate(inflater, container, false);
        ticketRecyclerView = root.rvTickets;
        ticketTableVm = new ViewModelProvider(this).get(TicketTableVM.class);
        checkingListNameEt = getActivity().findViewById(R.id.checkingName);
        datePickerView = getActivity().findViewById(R.id.datePickerView);


        displayTicketLists();
        root.startChecking.setOnClickListener(view -> {
            if(TextUtils.isEmpty(checkingListNameEt.getText())){
                Toast.makeText(mContext,"Enter Checking Name", Toast.LENGTH_SHORT).show();
            }
            else if(ticketListIds.size()>0) {
                CheckingTable event = new CheckingTable(checkingListNameEt.getText().toString(),date,date);
                newEventId = ticketTableVm.insert(event);
                event.setId(newEventId);;
                List<TicketTable> tickets = selectedEventTickets(ticketListIds);
                Intent intent = new Intent(getActivity(), ScannerActivity.class);
                intent.putExtra("event", (Serializable) event);
                startActivity(intent);
            }
            else
            {
                Toast.makeText(mContext, "Select at least one Ticket List", Toast.LENGTH_SHORT).show();
            }
        });


        datePickerView.setOnClickListener(view -> {
            selectDate();
        });

        onDateSetListener = (datePicker, year, month, day) -> {
            month = month+1;
            date = month + "/" + day + "/" + year;
        };
        return root.getRoot();
    }

    public void displayTicketLists(){
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        ticketRecyclerView.setLayoutManager(gridLayoutManager);

        ticketTableVm.getAllTicketList().observe(getActivity(), new Observer<List<TicketListTable>>() {
            @Override
            public void onChanged(List<TicketListTable> ticketListTables) {
                GridAdapter gridAdapter = new GridAdapter(ticketListTables, new GridAdapter.OnItemCheckListener() {
                    @Override
                    public void onItemCheck(long title) {
                        ticketListIds.add(title);
                    }

                    @Override
                    public void onItemUnCheck(long title) {
                        ticketListIds.remove(title);
                    }
                });
                ticketRecyclerView.setAdapter(gridAdapter);
                gridAdapter.notifyDataSetChanged();
            }
        });

    }

    public void selectDate(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getActivity(), android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                onDateSetListener,
                year,month,day);
        datePickerDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable((Color.TRANSPARENT))
        );
        datePickerDialog.show();
    }

    public List<TicketTable> selectedEventTickets(List<Long> ticketListIds){
        List<TicketTable> tickets = new ArrayList<>();
        for (Long ticketListId : ticketListIds){
            tickets.addAll(ticketTableVm.getAllTicketsFromListID(ticketListId));
            CheckingTicketListTableRelationship checkingTicketListTableRelationship = new CheckingTicketListTableRelationship(ticketListId,newEventId);
            ticketTableVm.insert(checkingTicketListTableRelationship);
        }
        return tickets;
    }
}