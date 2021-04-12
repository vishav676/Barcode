package com.vishav.barcode.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.vishav.barcode.Adapter.CheckingAdapter;
import com.vishav.barcode.Database.DatabaseHelper;
import com.vishav.barcode.Models.Event;
import com.vishav.barcode.R;
import com.vishav.barcode.ScannerActivity;
import com.vishav.barcode.databinding.ActivityMainMenuBinding;
import com.vishav.barcode.databinding.FragmentCheckingBinding;
import com.vishav.barcode.databinding.FragmentHomeBinding;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class CheckingFragment extends Fragment {

    List<Event> checkingName;
    RecyclerView recyclerView;
    DatabaseHelper db;
    Context mContext;
    FloatingActionButton checkingFAB;
    int checkingListId = -1;
    private FragmentCheckingBinding binding;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext=context;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentCheckingBinding.inflate(inflater, container, false);
        recyclerView = binding.checkingNameRecyclerView;
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        db = new DatabaseHelper(mContext);
        checkingFAB = binding.checkingFAB;
        checkingName = db.getEvents();
        CheckingAdapter adapter = new CheckingAdapter(checkingName, mContext);
        recyclerView.setAdapter(adapter);
        HomeFragment homeFragment = new HomeFragment();
        checkingFAB.setOnClickListener(view -> {
            int selectedPosition = adapter.getLastSelectedPosition();
            Event selectedEvent = checkingName.get(selectedPosition);

            Intent intent = new Intent(getActivity(), ScannerActivity.class);
            intent.putExtra("event", selectedEvent);
            startActivity(intent);
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