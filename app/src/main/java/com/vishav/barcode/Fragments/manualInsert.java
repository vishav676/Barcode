package com.vishav.barcode.Fragments;

import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.vishav.barcode.Database.Entities.TicketListTable;
import com.vishav.barcode.Database.Entities.TicketTable;
import com.vishav.barcode.ViewModels.TicketTableVM;
import com.vishav.barcode.R;
import com.vishav.barcode.databinding.FragmentManualInsertBinding;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import retrofit2.Call;


public class manualInsert extends Fragment {

    private FragmentManualInsertBinding root;
    private EditText listName;
    public manualInsert() {
        // Required empty public constructor
    }
    String tickets;
    Context mContext;
    private TicketTableVM ticketTableVm;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        root = FragmentManualInsertBinding.inflate(inflater, container, false);

        ticketTableVm = new ViewModelProvider(this).get(TicketTableVM.class);

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
                    Toast.makeText(mContext, "Please check the format" + e.getMessage(), Toast.LENGTH_SHORT).show();
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
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        TicketListTable newTicketListTable = new TicketListTable();
        newTicketListTable.setTicketListName(newListName);

        newTicketListTable.setTicketListCreated(sd.format(Calendar.getInstance().getTime()));
        newTicketListTable.setTicketListUpdated(sd.format(Calendar.getInstance().getTime()));

        long ticketTableListId = 0;
        try {
            ticketTableListId = ticketTableVm.insert(newTicketListTable);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(ticketTableListId<=0){
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
            TicketTable ticketTable = new TicketTable(number, customer, info, warningNote, useable, ticketTableListId,warning);
            ticketTableVm.insert(ticketTable);
        }
    }
}