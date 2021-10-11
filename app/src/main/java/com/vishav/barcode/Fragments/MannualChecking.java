package com.vishav.barcode.Fragments;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

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
    Spinner eventSpinner;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMannualCheckingBinding.inflate(inflater, container, false);
        ticketTableVM = new ViewModelProvider(this).get(TicketTableVM.class);
        Button search = binding.searchBTN;
        EditText ticketNumber = binding.searchTicketET;
        eventSpinner = binding.eventSpinner;
        populateSpinner();

        cardView = binding.getRoot().findViewById(R.id.barcode_result);
        tvNo = cardView.findViewById(R.id.number);
        tvName = cardView.findViewById(R.id.tvname);
        tvType = cardView.findViewById(R.id.tvType);

        error_cardView = binding.getRoot().findViewById(R.id.barcode_error);
        errorNum = error_cardView.findViewById(R.id.errorNum);
        issue = error_cardView.findViewById(R.id.issueTv);
        errorDetail = error_cardView.findViewById(R.id.tvErrorDetail);

        search.setOnClickListener(v -> {
            if (TextUtils.isEmpty(ticketNumber.getText()) ) {
                Toast.makeText(getContext(), "Ticket No. is missing", Toast.LENGTH_SHORT).show();
            } else {
                TicketTable ticket = ticketTableVM.getTicketInfo(ticketNumber.getText().toString(), eventSpinner.getSelectedItem().toString());
                validateTicket(ticket);
            }
        });

        return binding.getRoot();
    }

    private void delay(CardView cardView) {
        Timer time = new Timer();
        time.schedule(new TimerTask() {
            @Override
            public void run() {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        cardView.setVisibility(View.INVISIBLE);
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
        issue.setText("Ticket Number not in the list");
        errorNum.setText(binding.searchTicketET.getText().toString());
        delay(error_cardView);
    }

    public void inValidMessageAlreadyUsed(TicketTable ticket) {
        error_cardView.setVisibility(View.VISIBLE);
        issue.setText("Already used");
        errorNum.setText(ticket.getTicketNumber());
        delay(error_cardView);
    }

    public void inValidMessageAllTriesUsed(TicketTable ticket) {
        error_cardView.setVisibility(View.VISIBLE);
        issue.setText("All Tries Used");
        errorNum.setText(ticket.getTicketNumber());
        delay(error_cardView);
    }

    private void validTicket(TicketTable barcode) {
        ticketTableVM.updateTicketUseable(barcode.getTicketUseable() - 1, barcode.getId());
        cardView.setVisibility(View.VISIBLE);
        delay(cardView);
        tvName.setText(barcode.getTicketNumber());
        history(barcode);
    }

    private void history(TicketTable barcode) {
        TicketTable ticket = ticketTableVM.getOneTicket(barcode.getTicketNumber());
        CheckingTable event = ticketTableVM.getOneEvent(barcode.getTicketNumber());
        ScanningTable history = new ScanningTable("Success", trackHistory(),
                true,
                "Nem",
                "no",
                1,
                event.getId(),
                ticket.getTicketNumber());

        ticketTableVM.insert(history);
    }

    private String trackHistory() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat currentDate = new SimpleDateFormat("HH:mm:ss a",
                Locale.ENGLISH);
        return currentDate.format(date);
    }

    private void populateSpinner() {
        ticketTableVM.getAllEvents().observe(getActivity(), new Observer<List<CheckingTable>>() {
            @Override
            public void onChanged(List<CheckingTable> checkingTables) {
                if (checkingTables != null) {
                    List<String> eventNames = new ArrayList<>();
                    for (CheckingTable event :
                            checkingTables) {
                        eventNames.add(event.getCheckingName());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            getContext(), android.R.layout.simple_spinner_item, eventNames);

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    eventSpinner.setAdapter(adapter);
                }
            }
        });
    }

}