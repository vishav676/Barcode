package com.vishav.barcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vishav.barcode.Fragments.HomeFragment;
import com.vishav.barcode.Interfaces.OnFragmentInteraction;
import com.vishav.barcode.databinding.ActivityMain2Binding;

import java.util.HashMap;

public class MainActivity2 extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, OnFragmentInteraction {
    private HashMap<String, String> history;
    BottomNavigationView bottomNavigationView;
    private ActivityMain2Binding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bottomNavigationView = binding.bottomNavigationBar;
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        openFragment(new HomeFragment());
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.navigation_home) {
            Toast.makeText(this, "HOME", Toast.LENGTH_SHORT).show();
            openFragment(new HomeFragment());
        } else if (itemId == R.id.navigation_dashboard) {
            Toast.makeText(this, "Dashboard", Toast.LENGTH_SHORT).show();
        } else if (itemId == R.id.navigation_notifications) {
            Toast.makeText(this, "Notifications", Toast.LENGTH_SHORT).show();
        }
        return true;
    }
    void openFragment(Fragment fragment){
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout, fragment);
        //fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.settings, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_history){
            Intent intent = new Intent(this, history.class);
            intent.putExtra("history_list", history);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onFragmentHistory(HashMap<String, String> s) {
        history = s;
    }
}