package com.vishav.barcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.vishav.barcode.Adapter.PageAdapter2;

public class bulkAdd extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bulk_add);

        TabLayout tabLayout = findViewById(R.id.tabLayout2);
        ViewPager2 viewPager = findViewById(R.id.viewPager2);
        PageAdapter2 pageAdapter = new PageAdapter2(this);
        viewPager.setAdapter(pageAdapter);
        TabLayoutMediator mediator = new TabLayoutMediator(tabLayout, viewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                switch (position){
                    case 0:
                        tab.setText("Insert Manually");
                        break;
                    case 1:
                        tab.setText("Upload a file");
                        break;
                }
            }
        });
        mediator.attach();
    }
}