package com.vishav.barcode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.vishav.barcode.Database.Entities.CheckingTable;
import com.vishav.barcode.Database.Entities.TicketTable;
import com.vishav.barcode.Fragments.HomeFragment;
import com.vishav.barcode.Fragments.MannualChecking;
import com.vishav.barcode.ViewModels.TicketTableVM;
import com.vishav.barcode.databinding.ActivityMain2Binding;

import java.io.Serializable;
import java.util.List;

public class ScannerActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView bottomNavigationView;
    private ActivityMain2Binding binding;
    private TicketTableVM ticketTableVm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMain2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bottomNavigationView = binding.bottomNavigationBar;
        ticketTableVm = new ViewModelProvider(this).get(TicketTableVM.class);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        Intent intent = getIntent();
        CheckingTable event = (CheckingTable) intent.getSerializableExtra("event");
        List<TicketTable> ticketList = (List<TicketTable>) intent.getSerializableExtra("ticketList");
        HomeFragment homeFragment = new HomeFragment();
        if(event == null){
            ticketList = ticketTableVm.getAllTickets();
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable("ticketList", (Serializable) ticketList);
        bundle.putSerializable("event", (Serializable) event);
        homeFragment.setArguments(bundle);
        openFragment(homeFragment);
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
            Toast.makeText(this, "Mannual", Toast.LENGTH_SHORT).show();
            openFragment(new MannualChecking());
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
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}