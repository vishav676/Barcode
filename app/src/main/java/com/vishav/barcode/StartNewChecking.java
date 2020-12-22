package com.vishav.barcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.vishav.barcode.Adapter.PageAdapter;

public class StartNewChecking extends AppCompatActivity {

    Button addButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_new_checking);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        addButton = findViewById(R.id.addTickets);
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        PageAdapter pageAdapter = new PageAdapter(this);
        viewPager.setAdapter(pageAdapter);
        TabLayoutMediator mediator = new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setText("Tickets");
                        tab.setIcon(R.drawable.ticket);
                        break;
                    case 1:
                        tab.setText("Cards");
                        tab.setIcon(R.drawable.cards);
                        break;
                }
            }
        });
        mediator.attach();
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), bulkAdd.class);
                startActivity(intent);
            }
        });
    }
}