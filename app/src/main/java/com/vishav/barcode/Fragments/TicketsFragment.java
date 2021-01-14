package com.vishav.barcode.Fragments;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vishav.barcode.Adapter.GridAdapter;
import com.vishav.barcode.Database.dbHelper;
import com.vishav.barcode.MainActivity2;
import com.vishav.barcode.MainMenu;
import com.vishav.barcode.Models.Event;
import com.vishav.barcode.Models.Ticket;
import com.vishav.barcode.R;
import com.vishav.barcode.databinding.FragmentManualInsertBinding;
import com.vishav.barcode.databinding.FragmentTicketsBinding;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TicketsFragment extends Fragment {

    private FragmentTicketsBinding root;
    Context mContext;
    public TicketsFragment() {
        // Required empty public constructor
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }

    dbHelper db;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        List<Integer> eventIds = new ArrayList<>();

        root = FragmentTicketsBinding.inflate(inflater, container, false);
        RecyclerView ticketRecyclerView = root.rvTickets;
        db = new dbHelper(mContext);
        List<Event> events = db.getEventName();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        ticketRecyclerView.setLayoutManager(gridLayoutManager);

        GridAdapter gridAdapter = new GridAdapter(events, new GridAdapter.OnItemCheckListener() {
            @Override
            public void onItemCheck(int title) {
                eventIds.add(title);
            }

            @Override
            public void onItemUnCheck(int title) {
                eventIds.remove(Integer.valueOf(title));
            }
        });
        ticketRecyclerView.setAdapter(gridAdapter);
        root.startChecking.setOnClickListener(view -> {
            List<Ticket> tickets = selectedEventTickets(eventIds);
            Intent intent = new Intent(getActivity(), MainActivity2.class);
            intent.putExtra("ticketList", (Serializable) tickets);
            startActivity(intent);
        });
        return root.getRoot();
    }


    public List<Ticket> selectedEventTickets(List<Integer> eventIds){
        List<Ticket> tickets = new ArrayList<>();
        for (Integer eventId : eventIds){
            tickets.addAll(db.getEventTickets(eventId));
        }
        return tickets;
    }
}