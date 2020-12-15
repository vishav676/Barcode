package com.vishav.barcode;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


public class manualInsert extends Fragment {

    public manualInsert() {
        // Required empty public constructor
    }

    EditText manual;
    Button submit;
    String tickets;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_manual_insert, container, false);
        manual = root.findViewById(R.id.etMultiTickets);
        submit = root.findViewById(R.id.submitManualBulk);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tickets = manual.getText().toString();
                if(!tickets.equals("")){
                    saveTickets();
                }
            }
        });
        return root;
    }

    public void saveTickets(){
        String[] split = tickets.split("\n");
        String[] header = split[0].split(",");
        for (int i=1;i<split.length;i++){
            String[] values = split[i].split(",");
        }
    }

}