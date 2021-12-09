package com.vishav.barcode.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.vishav.barcode.R;


public class CardsFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Toast.makeText(getContext(), "To be Implemented",Toast.LENGTH_SHORT).show();
        return inflater.inflate(R.layout.fragment_cards, container, false);
    }
}