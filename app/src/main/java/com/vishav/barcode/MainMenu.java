package com.vishav.barcode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.vishav.barcode.databinding.ActivityMainMenuBinding;

public class MainMenu extends AppCompatActivity {

    ListView menu;
    private ActivityMainMenuBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainMenuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        menu = binding.lvMainMenu;

        String[] menuItems = new String[]{
                "Easy Checking","Continue Checking", "Start New Checking","Checkings"
                ,"Tickets Lists", "Cards Lists", "Settings","Add Bulk Tickets"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,android.R.id.text1,menuItems);

        menu.setAdapter(adapter);

        menu.setOnItemClickListener((adapterView, view, position, id) -> {
            if(position == 3)
            {
                Intent i = new Intent(MainMenu.this,MainActivity2.class);
                startActivity(i);

            }else if(position == 2){
                Intent i = new Intent(MainMenu.this,StartNewChecking.class);
                startActivity(i);
            }else if(position == 7){
                Intent i = new Intent(MainMenu.this,bulkAdd.class);
                startActivity(i);
            }
        });
    }
}
