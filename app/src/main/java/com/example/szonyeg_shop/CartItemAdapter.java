package com.example.szonyeg_shop;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CartItemAdapter extends RecyclerView.Adapter<CartItemAdapter.ViewHolder> {

    private ArrayList<ShopingItem> mShopingItemData = new ArrayList<>();
    private ArrayList<ShopingItem> mShopingItemDataAll = new ArrayList<>();
    private Context mContext;
    private CartActivity cartActivity;
    private int lastPosition = -1;

    public CartItemAdapter(Context context, ArrayList<ShopingItem> itemsData,CartActivity cartActivity) {
        this.mShopingItemData = itemsData;
        this.mShopingItemDataAll = itemsData;
        this.mContext = context;
        this.cartActivity=cartActivity;
    }

    @NonNull
    @Override
    public CartItemAdapter.ViewHolder onCreateViewHolder(
            @NonNull ViewGroup parent, int viewType) {
        return new CartItemAdapter.ViewHolder(LayoutInflater.from(mContext)
                .inflate(R.layout.item_cart, parent, false),this,cartActivity);
    }



    @Override
    public void onBindViewHolder(@NonNull CartItemAdapter.ViewHolder holder, int position) {
        ShopingItem currentItem = mShopingItemData.get(position);

        holder.bindTo(currentItem);
    }


    @Override
    public int getItemCount() {
        return mShopingItemData.size();
    }




    static class ViewHolder extends RecyclerView.ViewHolder {

        // Member Variables for the TextViews
        private TextView mName;
        private TextView mPrice;
        private ImageView mImage;
        private TextView mAmaunt;
        private CartItemAdapter mAdapter;
        private CartActivity cartActivity;



        ViewHolder(View itemView,CartItemAdapter cartItemAdapter,CartActivity cartActivity) {
            super(itemView);
            mName=itemView.findViewById(R.id.textViewCartItemName);
            mPrice=itemView.findViewById(R.id.textViewCartItemPrice);
            mImage=itemView.findViewById(R.id.imageViewCartItem);
            mAmaunt=itemView.findViewById(R.id.textViewCartItemQuantity);
            mAdapter=cartItemAdapter;
            this.cartActivity=cartActivity;

        }

        void bindTo(ShopingItem currentItem){
            mName.setText(currentItem.getName());
            mPrice.setText(currentItem.getPrice());
            mImage.setImageResource(currentItem.getImageResource());
            mAmaunt.setText(currentItem.getAmount().toString());

            itemView.findViewById(R.id.buttonAdd).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentItem.increment();
                    cartActivity.updateTotalAmaount();
                    mAdapter.notifyItemChanged(getAdapterPosition());
                }
            });

            itemView.findViewById(R.id.buttonRemove).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    currentItem.decrement();
                    if(currentItem.getAmount()==0){
                        ShopActivity.cartList.remove(currentItem);
                    }
                    cartActivity.updateTotalAmaount();
                    mAdapter.notifyDataSetChanged();
                }
            });



        }

    }

}
