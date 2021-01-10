package com.vishav.barcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.vishav.barcode.Adapter.PageAdapter2;
import com.vishav.barcode.databinding.ActivityBulkAddBinding;

public class bulkAdd extends AppCompatActivity {

    private ActivityBulkAddBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBulkAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        PageAdapter2 pageAdapter = new PageAdapter2(this);
        binding.viewPager2.setAdapter(pageAdapter);
        TabLayoutMediator mediator = new TabLayoutMediator(binding.tabLayout2, binding.viewPager2, (tab, position) -> {
            switch (position){
                case 0:
                    tab.setText("Insert Manually");
                    break;
                case 1:
                    tab.setText("Upload a file");
                    break;
            }
        });
        mediator.attach();
    }
}