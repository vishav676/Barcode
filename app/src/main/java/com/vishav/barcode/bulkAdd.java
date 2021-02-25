package com.vishav.barcode;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.vishav.barcode.Adapter.PageAdapter2;
import com.vishav.barcode.databinding.ActivityBulkAddBinding;
import com.vishav.barcode.databinding.ActivityMainMenuBinding;

public class bulkAdd extends Fragment {

    private ActivityBulkAddBinding binding;
    private EditText listName;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        binding = ActivityBulkAddBinding.inflate(getLayoutInflater());
        listName = binding.newListName;

        PageAdapter2 pageAdapter = new PageAdapter2(getActivity());
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

        return binding.getRoot();
    }
}