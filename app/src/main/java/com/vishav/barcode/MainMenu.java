package com.vishav.barcode;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.vishav.barcode.Database.DatabaseHelper;
import com.vishav.barcode.databinding.ActivityMainMenuBinding;

public class MainMenu extends Fragment {

    ListView menu;
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

        String[] menuItems = new String[]{
                "Easy Checking","Continue Checking", "Start New Checking","Checkings"
                ,"Tickets Lists", "Cards Lists", "Settings","Add Bulk Tickets"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                android.R.layout.simple_list_item_1,android.R.id.text1,menuItems);

        menu.setAdapter(adapter);

        menu.setOnItemClickListener((adapterView, view, position, id) -> {
            if(position == 3)
            {
                Intent i = new Intent(getActivity(),MainActivity2.class);
                startActivity(i);

            }else if(position == 2){
                ((MainActivity)getActivity()).openFragment(new StartNewChecking());
            }else if(position == 7){
                ((MainActivity)getActivity()).openFragment(new bulkAdd());
            }
        });

        return binding.getRoot();
    }

}
