package com.vishav.barcode.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vishav.barcode.Database.Entities.TicketTable;
import com.vishav.barcode.R;

import java.util.List;

public class TicketAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<TicketTable> tickets;
    private final int HIDE_MENU = 1;
    private final int SHOW_MENU = 2;

    public TicketAdapter(List<TicketTable> tickets)
    {
        this.tickets = tickets;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType == SHOW_MENU){
            view= LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.swipe_layout,parent,false);
            return new MenuViewHolder(view);
        }else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.one_ticket, parent, false);
            return new ItemViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if(holder instanceof ItemViewHolder) {
            ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
            TicketTable ticketTable = tickets.get(position);
            itemViewHolder.ticketNumber.setText(ticketTable.getTicketNumber());
            itemViewHolder.ticketName.setText(ticketTable.getTicketCustomerName());
            itemViewHolder.useable.setText("Useable: " + ticketTable.getTicketUseable());
        }
    }

    @Override
    public int getItemCount() {
        return tickets.size();
    }

    public class MenuViewHolder extends RecyclerView.ViewHolder{
        public ImageView check, delete;
        public MenuViewHolder(@NonNull View itemView) {
            super(itemView);
            check = itemView.findViewById(R.id.checkTicket);
            delete = itemView.findViewById(R.id.deleteTicket);
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView ticketName, ticketNumber, useable;
        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            ticketName = itemView.findViewById(R.id.customerName);
            ticketNumber = itemView.findViewById(R.id.ticketNumber);
            useable = itemView.findViewById(R.id.useable);
        }
    }
}
