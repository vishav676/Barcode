package com.vishav.barcode.Fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vishav.barcode.Adapter.GridAdapter;
import com.vishav.barcode.Database.dbHelper;
import com.vishav.barcode.R;

import java.util.List;

public class TicketsFragment extends Fragment {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_tickets, container, false);
        RecyclerView ticketRecylerView = root.findViewById(R.id.rvTickets);
        db = new dbHelper(mContext);
        List<String> eventNames = db.getEventName();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        ticketRecylerView.setLayoutManager(gridLayoutManager);

        GridAdapter gridAdapter = new GridAdapter(eventNames);
        ticketRecylerView.setAdapter(gridAdapter);

        return root;
    }
}