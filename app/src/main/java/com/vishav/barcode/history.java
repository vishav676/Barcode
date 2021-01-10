package com.vishav.barcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.vishav.barcode.Adapter.HashMapAdapter;
import com.vishav.barcode.databinding.ActivityHistoryBinding;

import java.util.HashMap;

public class history extends AppCompatActivity {

    private ActivityHistoryBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Intent intent = getIntent();
        HashMap<String,String> result = (HashMap<String, String>) intent.getSerializableExtra("history_list");

        if (result != null) {
            HashMapAdapter adapter = new HashMapAdapter(result);
            binding.lvHistory.setAdapter(adapter);
        }
        else
        {
            Toast.makeText(this,"No record found",Toast.LENGTH_SHORT).show();
        }

    }
}