package com.vishav.barcode.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.vishav.barcode.databinding.FragmentCheckingBinding;
import com.vishav.barcode.databinding.FragmentMannualCheckingBinding;

public class MannualChecking extends Fragment {

    private FragmentMannualCheckingBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMannualCheckingBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
}