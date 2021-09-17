package com.vishav.barcode.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vishav.barcode.Database.Entities.ScanningTable;
import com.vishav.barcode.R;

import java.util.List;

public class HashMapAdapter extends BaseAdapter {

    private final List<ScanningTable> data;

    public HashMapAdapter(List<ScanningTable> result) {
        data = result;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public ScanningTable getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final View result;

        if(view == null){
            result = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_layout,viewGroup,false);
        }
        else {
            result = view;
        }
        ScanningTable item = getItem(i);
        ((TextView)result.findViewById(R.id.serialTv)).setText(item.getScanningTicketNumber());
        ((TextView)result.findViewById(R.id.ticketTv)).setText(item.getScanningTime());
        return result;
    }
}
