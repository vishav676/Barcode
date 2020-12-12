package com.vishav.barcode;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toolbar;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class StartNewChecking extends AppCompatActivity {

    Button addButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_new_checking);

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        TabItem tickets = findViewById(R.id.tabTickets);
        TabItem cards = findViewById(R.id.tabCards);
        addButton = findViewById(R.id.addTickets);
        ViewPager2 viewPager = findViewById(R.id.viewPager);
        PageAdapter pageAdapter = new PageAdapter(new FragmentActivity(), tabLayout.getTabCount());
        viewPager.setAdapter(pageAdapter);
        new TabLayoutMediator(tabLayout, viewPager,(tab, position) -> tab.setText("Object"+ position + 1)).attach();
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddTicket.class);
                startActivity(intent);
            }
        });
    }
}