package com.vishav.barcode.Fragments;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.vishav.barcode.Database.DatabaseHelper;
import com.vishav.barcode.Models.Ticket;
import com.vishav.barcode.Models.TicketList;
import com.vishav.barcode.R;
import com.vishav.barcode.databinding.FragmentManualInsertBinding;

import java.time.LocalDateTime;
import java.util.Calendar;


public class manualInsert extends Fragment {

    private FragmentManualInsertBinding root;
    private EditText listName;
    public manualInsert() {
        // Required empty public constructor
    }
    String tickets;
    DatabaseHelper db;
    Context mContext;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        root = FragmentManualInsertBinding.inflate(inflater, container, false);

        listName = getActivity().findViewById(R.id.newListName);
        root.submitManualBulk.setOnClickListener(view -> {
            tickets = root.etMultiTickets.getText().toString();
            if(!tickets.equals("")){
                try {
                    if(tickets.contains(",")){
                        save(";",",");
                    }else
                    {
                        save("\n","\t");
                    }
                    listName.setText("");
                    Toast.makeText(mContext, "Added", Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {
                    Toast.makeText(mContext, "Please check the format", Toast.LENGTH_SHORT).show();
                }

            }else {
                root.etMultiTickets.setError("Please Enter Tickets");
            }
            root.etMultiTickets.setText("");
        });

        root.pasteBin.setOnClickListener(view -> pasteToEditText());
        return root.getRoot();
    }
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext=context;
    }

    private void pasteToEditText(){
        ClipboardManager clipboardManager = (ClipboardManager)mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        CharSequence pasteData = "";
        if(clipboardManager.hasPrimaryClip()) {
            if (clipboardManager.getPrimaryClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                ClipData.Item item = clipboardManager.getPrimaryClip().getItemAt(0);
                pasteData = item.getText();
                root.etMultiTickets.setText(pasteData);
            }
        }else
        {
            Toast.makeText(mContext, "Clipboard is Empty", Toast.LENGTH_SHORT).show();
        }
    }
    public void save(String regexHeader, String regexData){
        db = new DatabaseHelper(mContext);
        String[] split = tickets.split(regexHeader);
        if (TextUtils.isEmpty(listName.getText())){
            Toast.makeText(mContext, "Please Provide List Name", Toast.LENGTH_SHORT).show();
        }
        else{
            saveListToDb(split, regexData);
        }
    }

    public void saveListToDb(String[] split, String regexData){
        String newListName = listName.getText().toString();
        TicketList newTicketList = new TicketList(newListName, Calendar.getInstance().getTime().toString(),Calendar.getInstance().getTime().toString());
        int ticketListId = db.insertTicketList(newTicketList);
        if(ticketListId<0){
            throw new ArithmeticException();
        }
        for (String s : split) {
            String[] values = s.split(regexData);
            String number = values[0].replace("\n", "").replace("\r", "");
            String info = values[1];
            String warningNote = values[2];
            String warning = values[3];
            String customer = values[4];
            int useable = Integer.parseInt(values[5].replaceAll("\\s+", "").replace("\n", "").replace("\r", ""));
            Ticket ticket = new Ticket(number, customer, info, warningNote, useable, warning, ticketListId);
            db.insertTicket(ticket);
        }
    }
}