package com.vishav.barcode.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.vishav.barcode.Adapter.CheckingAdapter;
import com.vishav.barcode.ApiUtils;
import com.vishav.barcode.Database.Entities.CheckingTable;
import com.vishav.barcode.R;
import com.vishav.barcode.ScannerActivity;
import com.vishav.barcode.ViewModels.TicketTableVM;
import com.vishav.barcode.databinding.FragmentCheckingBinding;

import java.io.IOException;
import java.util.List;

public class CheckingFragment extends Fragment {


    RecyclerView recyclerView;
    Context mContext;
    FloatingActionButton checkingFAB;
    int checkingListId = -1;
    private CheckingAdapter adapter;
    private TicketTableVM ticketTableVM;
    List<CheckingTable> allEvents;
    SharedPreferences preferences;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext=context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        com.vishav.barcode.databinding.FragmentCheckingBinding binding = FragmentCheckingBinding.inflate(inflater, container, false);
        recyclerView = binding.checkingNameRecyclerView;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        preferences = getActivity().getPreferences(Context.MODE_PRIVATE);;
        try {
            new ApiUtils(getActivity().getApplication()).getCheckingTicketRelation();
        } catch (IOException e) {
            e.printStackTrace();
        }
        ticketTableVM = new ViewModelProvider(this).get(TicketTableVM.class);
        checkingFAB = binding.checkingFAB;
        ticketTableVM.getAllEvents().observe(getActivity(), checkingTables -> {
            if(checkingTables != null) {
                allEvents = checkingTables;
                adapter = new CheckingAdapter(checkingTables, mContext);
                recyclerView.setAdapter(adapter);
            }
            adapter.notifyDataSetChanged();
        });

        HomeFragment homeFragment = new HomeFragment();
        checkingFAB.setOnClickListener(view -> {
            int selectedPosition = adapter.getLastSelectedPosition();
            if(selectedPosition < 0)
            {
                Toast.makeText(getActivity(),"Please select an event to continue", Toast.LENGTH_SHORT).show();
            }
            else {
                CheckingTable selectedEvent = allEvents.get(selectedPosition);
                SharedPreferences.Editor prefEditor = preferences.edit();
                Gson gson = new Gson();
                String json = gson.toJson(selectedEvent);
                prefEditor.putString("Event", json);
                prefEditor.apply();

                Intent intent = new Intent(getActivity(), ScannerActivity.class);
                intent.putExtra("event", selectedEvent);
                startActivity(intent);
            }
        });
        return binding.getRoot();
    }

    void openFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
}