package com.vishav.barcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.vishav.barcode.Adapter.HashMapAdapter;
import com.vishav.barcode.Database.DatabaseHelper;
import com.vishav.barcode.Models.History;
import com.vishav.barcode.databinding.ActivityHistoryBinding;

import java.util.HashMap;
import java.util.List;

public class history extends AppCompatActivity {

    private ActivityHistoryBinding binding;
    DatabaseHelper dbHelper;
    private List<History> histories;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dbHelper = new DatabaseHelper(this);
        histories = dbHelper.getAllHistory();

        if (histories != null) {
            HashMapAdapter adapter = new HashMapAdapter(histories);
            binding.lvHistory.setAdapter(adapter);
        }
        else
        {
            Toast.makeText(this,"No record found",Toast.LENGTH_SHORT).show();
        }

    }
}