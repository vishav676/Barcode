package com.vishav.barcode.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.mlkit.vision.barcode.Barcode;
import com.vishav.barcode.Adapter.TicketAdapter;
import com.vishav.barcode.Database.Entities.CheckingTable;
import com.vishav.barcode.Database.Entities.ScanningTable;
import com.vishav.barcode.Database.Entities.TicketTable;
import com.vishav.barcode.R;
import com.vishav.barcode.TicketValidator;
import com.vishav.barcode.ViewModels.TicketTableVM;
import com.vishav.barcode.databinding.FragmentCheckingBinding;
import com.vishav.barcode.databinding.FragmentMannualCheckingBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MannualChecking extends Fragment {

    private FragmentMannualCheckingBinding binding;
    private TicketTableVM ticketTableVM;
    CardView cardView;
    CardView error_cardView;
    TextView tvName, tvNo, errorNum, issue, errorDetail;
    CheckingTable event;
    RecyclerView rv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMannualCheckingBinding.inflate(inflater, container, false);
        ticketTableVM = new ViewModelProvider(this).get(TicketTableVM.class);
        Button search = binding.searchBTN;
        EditText ticketNumber = binding.searchTicketET;

        cardView = binding.getRoot().findViewById(R.id.barcode_result);
        tvNo = cardView.findViewById(R.id.number);
        tvName = cardView.findViewById(R.id.tvname);

        error_cardView = binding.getRoot().findViewById(R.id.barcode_error);
        errorNum = error_cardView.findViewById(R.id.errorNum);
        issue = error_cardView.findViewById(R.id.issueTv);
        errorDetail = error_cardView.findViewById(R.id.tvErrorDetail);

        Bundle bundle = getArguments();
        if(bundle != null) {
            event = (CheckingTable) bundle.getSerializable("event");
        }
        search.setOnClickListener(v -> {
            if (TextUtils.isEmpty(ticketNumber.getText()) ) {
                Toast.makeText(getContext(), "Ticket No. is missing", Toast.LENGTH_SHORT).show();
            } else {
                TicketTable ticket = ticketTableVM.getTicketInfo(ticketNumber.getText().toString(),
                        event.getCheckingName());
                validateTicket(ticket);
            }
        });
        displayTickets();
        return binding.getRoot();
    }

    private void displayTickets() {
        rv = binding.tickets;
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rv.setLayoutManager(linearLayoutManager);
        List<TicketTable> tickets = ticketTableVM.getAllEventTickets(event.getId());
        TicketAdapter adapter = new TicketAdapter(tickets);
        rv.setAdapter(adapter);

        ItemTouchHelper.SimpleCallback callback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                TicketTable ticket = tickets.get(position);
                validateTicket(ticket);
                adapter.notifyDataSetChanged();
            }
        };
        new ItemTouchHelper(callback).attachToRecyclerView(rv);
    }
    private void validateTicket(TicketTable ticket) {
        TicketValidator validator = new TicketValidator(error_cardView,ticketTableVM,
                getActivity());
        rv.setVisibility(View.INVISIBLE);
        if (ticket != null && ticket.getTicketUseable() > 0) {
            validator.validTicket(ticket, cardView, tvName);
        } else if (ticket == null) {
            validator.inValidMessageTicketNotFound(binding.searchTicketET.getText().toString());
        } else if (ticket.getTicketUseable() <= 0) {
            validator.inValidMessageAllTriesUsed(ticket);
        } else if (ticketTableVM.getOneHistory(ticket.getTicketNumber()) != null) {
            validator.inValidMessageAlreadyUsed(ticket);
        }
        delay();
    }

    private void delay(){
        Timer time = new Timer();
        time.schedule(new TimerTask() {
            @Override
            public void run() {
                if(getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        rv.setVisibility(View.VISIBLE);
                    });
                }
            }
        },3000);
    }
}