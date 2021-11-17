package com.vishav.barcode.Fragments;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
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
    TextView tvName, tvType, tvNo, errorNum, issue, errorDetail;
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
        tvType = cardView.findViewById(R.id.tvType);

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
        TicketAdapter adapter = new TicketAdapter(ticketTableVM.getAllEventTickets(event.getId()));
        rv.setAdapter(adapter);

    }

    private void delay(CardView cardView) {
        Timer time = new Timer();
        time.schedule(new TimerTask() {
            @Override
            public void run() {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        cardView.setVisibility(View.INVISIBLE);
                        rv.setVisibility(View.VISIBLE);
                    });
                }
            }
        }, 3000);
    }

    private void validateTicket(TicketTable ticket) {
        if (ticket != null && ticket.getTicketUseable() > 0) {
            validTicket(ticket);
        } else if (ticket == null) {
            inValidMessageTicketNotFound();
        } else if (ticket.getTicketUseable() <= 0) {
            inValidMessageAllTriesUsed(ticket);
        } else if (ticketTableVM.getOneHistory(ticket.getTicketNumber()) != null) {
            inValidMessageAlreadyUsed(ticket);
        }
    }

    public void inValidMessageTicketNotFound() {
        error_cardView.setVisibility(View.VISIBLE);
        rv.setVisibility(View.INVISIBLE);
        issue.setText("Ticket Number not in the list");
        errorNum.setText(binding.searchTicketET.getText().toString());
        delay(error_cardView);
    }

    public void inValidMessageAlreadyUsed(TicketTable ticket) {
        error_cardView.setVisibility(View.VISIBLE);
        issue.setText("Already used");
        rv.setVisibility(View.INVISIBLE);
        errorNum.setText(ticket.getTicketNumber());
        delay(error_cardView);
        history("Failed",ticket,"Already Used");
    }

    public void inValidMessageAllTriesUsed(TicketTable ticket) {
        error_cardView.setVisibility(View.VISIBLE);
        rv.setVisibility(View.INVISIBLE);
        issue.setText("All Tries Used");
        errorNum.setText(ticket.getTicketNumber());
        delay(error_cardView);
        history("Failed",ticket,"All Tries Used");
    }

    private void validTicket(TicketTable barcode) {
        ticketTableVM.updateTicketUseable(barcode.getTicketUseable() - 1, barcode.getId());
        cardView.setVisibility(View.VISIBLE);
        rv.setVisibility(View.INVISIBLE);
        delay(cardView);
        tvName.setText(barcode.getTicketNumber());
        history("Success",barcode,"nem");
    }

    private void history(String status,TicketTable barcode,String issue) {
        TicketTable ticket = ticketTableVM.getOneTicket(barcode.getTicketNumber());
        int scanningTimesUsed = 1;
        CheckingTable event = ticketTableVM.getOneEvent(barcode.getTicketNumber());
        ScanningTable getHistory = ticketTableVM.getOneHistory(barcode.getTicketNumber());
        ticketTableVM.updateTicketToApi(barcode);
        if(getHistory != null)
        {
            scanningTimesUsed += getHistory.getScanningTimesUsed();
        }

        ScanningTable history = new ScanningTable(status, trackHistory(),
                true,
                issue,
                "no",
                scanningTimesUsed,
                event.getId(),
                ticket.getTicketNumber());

        ticketTableVM.insert(history);
    }

    private String trackHistory()
    {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        return sd.format(date);
    }
}