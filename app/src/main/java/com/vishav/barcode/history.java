package com.vishav.barcode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import com.vishav.barcode.Adapter.HashMapAdapter;

import java.util.HashMap;

public class history extends AppCompatActivity {

    ListView historyListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        Intent intent = getIntent();
        HashMap<String,String> result = (HashMap<String, String>) intent.getSerializableExtra("history_list");
        historyListView = (ListView)findViewById(R.id.lvHistory);

        if (result != null) {
            HashMapAdapter adapter = new HashMapAdapter(result);
            historyListView.setAdapter(adapter);
        }
        else
        {
            Toast.makeText(this,"No record found",Toast.LENGTH_SHORT).show();
        }

    }
}