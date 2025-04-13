package com.example.szonyeg_shop;

import android.content.AsyncQueryHandler;
import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.szonyeg_shop.ShopingItem;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Locale;

public class ShopingItemAdatpter extends RecyclerView.Adapter<ShopingItemAdatpter.ViewHolder> {

    private ArrayList<ShopingItem> mShopingItemData = new ArrayList<>();
    private ArrayList<ShopingItem> mShopingItemDataAll = new ArrayList<>();
    private Context mContext;
    private int lastPosition = -1;

    public ShopingItemAdatpter(Context context, ArrayList<ShopingItem> itemsData) {
        this.mShopingItemData = itemsData;
        this.mShopingItemDataAll = itemsData;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ShopingItemAdatpter.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.item_product, parent, false));
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ShopingItem currentItem = mShopingItemData.get(position);

        holder.bindTo(currentItem);
        setAnimation(holder.itemView, position);
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slie_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }


    @Override
    public int getItemCount() {
        return mShopingItemData.size();
    }

    public Filter getFilter() {
        return shopingFilter;
    }

    private Filter shopingFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<ShopingItem> filteredList = new ArrayList<>();
            FilterResults results = new FilterResults();

            if (charSequence == null || charSequence.length() == 0) {
                results.count = mShopingItemDataAll.size();
                results.values = mShopingItemDataAll;
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (ShopingItem item : mShopingItemDataAll) {
                    if (item.getName().toLowerCase().contains(filterPattern)) {
                        filteredList.add(item);
                    }
                }

                results.count = filteredList.size();
                results.values = filteredList;
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            mShopingItemData = (ArrayList) results.values;
            notifyDataSetChanged();
        }
    };


        static class ViewHolder extends RecyclerView.ViewHolder {

        // Member Variables for the TextViews
        TextView mName;
        TextView mPrice;
        ImageView mImage;




        ViewHolder(View itemView) {
            super(itemView);

            mName=itemView.findViewById(R.id.textViewProductName);
            mPrice=itemView.findViewById(R.id.textViewProductPrice);
            mImage=itemView.findViewById(R.id.imageViewProduct);


        }

        void bindTo(ShopingItem currentItem){
            mName.setText(currentItem.getName());
            mPrice.setText(currentItem.getPrice()+" Ft");
            mImage.setImageResource(currentItem.getImageResource());
            itemView.findViewById(R.id.buttonAddToCart).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    for (int i = 0; i < ShopActivity.cartList.size(); i++) {
                        if (currentItem.equals(ShopActivity.cartList.get(i))){
                            ShopActivity.cartList.get(i).increment();
                            return;
                        }
                    }
                    ShopActivity.cartList.add(currentItem);
                }
            });

        }
    }

}
