package com.vishav.barcode.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.vishav.barcode.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class HashMapAdapter extends BaseAdapter {

    private final ArrayList data;

    public HashMapAdapter(HashMap<String,String> result) {
        data = new ArrayList();
        data.addAll(result.entrySet());
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public HashMap.Entry getItem(int i) {
        return (HashMap.Entry) data.get(i);
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
        HashMap.Entry<String,String> item = getItem(i);
        ((TextView)result.findViewById(R.id.serialTv)).setText(item.getKey());
        ((TextView)result.findViewById(R.id.ticketTv)).setText(item.getValue());
        return result;
    }
}
