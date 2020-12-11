package com.vishav.barcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartNewChecking extends AppCompatActivity {

    Button addButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_new_checking);

        addButton = findViewById(R.id.addTickets);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddTicket.class);
                startActivity(intent);
            }
        });
    }
}