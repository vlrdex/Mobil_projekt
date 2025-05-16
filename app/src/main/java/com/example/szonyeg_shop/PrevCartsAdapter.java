package com.example.szonyeg_shop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PrevCartsAdapter extends RecyclerView.Adapter<PrevCartsAdapter.ViewHolder> {

    private List<PrevCarts> itemData;
    private Context mContext;

    public PrevCartsAdapter( Context mContext ,List<PrevCarts> itemData) {
        this.itemData = itemData;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public PrevCartsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PrevCartsAdapter.ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.list_item_prev_cart, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PrevCarts currentItem=itemData.get(position);
        holder.bindTo(currentItem);
    }

    @Override
    public int getItemCount() {
        return itemData.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView date;
        private TextView total;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.date=itemView.findViewById(R.id.textViewOrderDate);
            this.total=itemView.findViewById(R.id.textViewOrderTotal);
        }


        void bindTo(PrevCarts currentItem){
            date.setText(currentItem.getDate().toString());
            total.setText(currentItem.getTotal()+" Ft");
        }
    }
}
