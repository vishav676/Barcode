package com.vishav.barcode.Fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.vishav.barcode.Adapter.PageAdapter;
import com.vishav.barcode.MainActivity;
import com.vishav.barcode.R;
import com.vishav.barcode.bulkAdd;
import com.vishav.barcode.databinding.ActivityStartNewCheckingBinding;

public class StartNewChecking extends Fragment {

    Button addButton;
    private ActivityStartNewCheckingBinding binding;
    EditText newEvent;
    ImageView calendarIcon;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStartNewCheckingBinding.inflate(getLayoutInflater());
        TabLayout tabLayout = binding.tabLayout;
        addButton = binding.addTickets;
        ViewPager2 viewPager = binding.viewPager;
        newEvent = binding.checkingName;
        calendarIcon = binding.datePickerView;
        PageAdapter pageAdapter = new PageAdapter(getActivity());
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
            ((MainActivity)getActivity()).openFragment(new bulkAdd());
        });

        return binding.getRoot();
    }
}