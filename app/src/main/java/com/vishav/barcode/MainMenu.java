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

public class MainMenu extends AppCompatActivity {

    ListView menu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        menu = findViewById(R.id.lvMainMenu);

        String[] menuItems = new String[]{
                "Easy Checking","Continue Checking", "Start New Checking","Checkings"
                ,"Tickets Lists", "Cards Lists", "Settings"
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,android.R.id.text1,menuItems);

        menu.setAdapter(adapter);

        menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if(position == 3)
                {
                    Intent i = new Intent(MainMenu.this,MainActivity2.class);
                    startActivity(i);

                }else if(position == 2){
                    Intent i = new Intent(MainMenu.this,StartNewChecking.class);
                    startActivity(i);
                }
            }
        });
    }
}
