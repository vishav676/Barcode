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
import com.vishav.barcode.databinding.ActivityStartNewCheckingBinding;

public class StartNewChecking extends AppCompatActivity {

    Button addButton;
    private ActivityStartNewCheckingBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStartNewCheckingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        TabLayout tabLayout = binding.tabLayout;
        addButton = binding.addTickets;
        ViewPager2 viewPager = binding.viewPager;
        PageAdapter pageAdapter = new PageAdapter(this);
        viewPager.setAdapter(pageAdapter);
        TabLayoutMediator mediator = new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> {
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
        });
        mediator.attach();
        addButton.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), bulkAdd.class);
            startActivity(intent);
        });
    }
}