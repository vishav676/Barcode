package com.vishav.barcode.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.vishav.barcode.ApiUtils;
import com.vishav.barcode.MainActivity;
import com.vishav.barcode.ScannerActivity;
import com.vishav.barcode.TicketListActivity;
import com.vishav.barcode.bulkAdd;
import com.vishav.barcode.databinding.ActivityMainMenuBinding;

import java.io.IOException;

public class MainMenu extends Fragment {

    ListView menu;
    private MainMenu application;
    private ActivityMainMenuBinding binding;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainMenuBinding.inflate(getLayoutInflater());

        menu = binding.lvMainMenu;
        application = this;
        String[] menuItems = new String[]{
                "Easy Checking","Continue Checking", "Start New Checking","Checkings"
                ,"Tickets Lists", "Cards Lists", "Settings","Add Bulk Tickets"
        };

        binding.syncDb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ApiUtils apiUtils = new ApiUtils(getActivity().getApplication());
                try {
                    apiUtils.fetchData();
                } catch (IOException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1,android.R.id.text1,menuItems);

        menu.setAdapter(adapter);

        menu.setOnItemClickListener((adapterView, view, position, id) -> {
            if(position == 3)
            {
                /*Intent i = new Intent(getActivity(), ScannerActivity.class);
                startActivity(i);*/
                ((MainActivity)getActivity()).openFragment(new CheckingFragment());
            }else if(position == 1){
                ((MainActivity)getActivity()).openFragment(new StartNewChecking());
            }else if(position == 2){
                ((MainActivity)getActivity()).openFragment(new StartNewChecking());
            }else if(position == 7){
                ((MainActivity)getActivity()).openFragment(new bulkAdd());
            }else if(position == 4){
                Intent i = new Intent(getActivity(), TicketListActivity.class);
                startActivity(i);
            }
        });

        return binding.getRoot();
    }
}
