package com.vishav.barcode.Adapter;

import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vishav.barcode.Models.Event;
import com.vishav.barcode.R;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GridAdapter extends RecyclerView.Adapter<GridAdapter.RowViewHolder> {

    List<Event> titles;

    public interface OnItemCheckListener{
        void onItemCheck(int title);
        void onItemUnCheck(int title);
    }
    private OnItemCheckListener onItemCheckListener;
    public GridAdapter(List<Event> cursor, OnItemCheckListener onItemCheckListener){
        this.titles = cursor;
        this.onItemCheckListener = onItemCheckListener;
    }
    @NonNull
    @Override
    public RowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item,parent,false);
        return new RowViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RowViewHolder holder, int position) {
        holder.title.setText(titles.get(position).getName());
        holder.checkBox.setOnClickListener(view -> {
            boolean isChecked = holder.checkBox.isChecked();
            if(isChecked){
                onItemCheckListener.onItemCheck(titles.get(position).getId());
            }else{
                onItemCheckListener.onItemUnCheck(titles.get(position).getId());
            }
        });
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public static class RowViewHolder extends RecyclerView.ViewHolder {
        // init the item view's
        TextView title;
        CheckBox checkBox;
        public RowViewHolder(View itemView) {
            super(itemView);
            // get the reference of item view's
            title = (TextView) itemView.findViewById(R.id.tvTitle);
            checkBox = itemView.findViewById(R.id.checkbox);
        }

        public void setOnClickListener(View.OnClickListener onClickListener) {
            itemView.setOnClickListener(onClickListener);
        }
    }
}
