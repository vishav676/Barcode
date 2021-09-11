package com.vishav.barcode.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vishav.barcode.Models.Event;
import com.vishav.barcode.R;

import java.util.List;

public class CheckingAdapter extends RecyclerView.Adapter<CheckingAdapter.ViewHolder> {

    private List<Event> checkingNames;
    private Context context;
    private int lastSelectedPosition = -1;

    public CheckingAdapter(List<Event> checkingNames, Context ctx)
    {
        this.checkingNames = checkingNames;
        this.context = ctx;
    }
    @NonNull
    @Override
    public CheckingAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.checking_layout, parent, false);
        CheckingAdapter.ViewHolder viewHolder = new CheckingAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CheckingAdapter.ViewHolder holder, int position) {
        holder.checkingName.setText(checkingNames.get(position).getName());
        holder.button.setChecked(position == lastSelectedPosition);
    }

    public int getLastSelectedPosition(){
        return lastSelectedPosition;
    }

    @Override
    public int getItemCount() {
        return checkingNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        public RadioButton button;
        public TextView checkingName;

        public ViewHolder(View view)
        {
            super(view);
            button = (RadioButton) view.findViewById(R.id.checkingNameLayout);
            checkingName = (TextView)view.findViewById(R.id.checkingNameTV);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lastSelectedPosition = getAdapterPosition();
                    notifyDataSetChanged();
                }
            });
        }
    }
}
